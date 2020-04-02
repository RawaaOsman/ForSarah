package com.example.iamonyourway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
// Add an import statement for the client library.
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MapsActivityForDriver extends FragmentActivity implements OnMapReadyCallback ,LocationListener, RoutingListener, TaskLoadedCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    Location mLastLocation ,mCurrentLocation ;
    MarkerOptions start , end;
    Polyline currentPath;

    private static final int REQUEST_CODE = 101;
    private final String TAG = "MapsActivityForDriver";

    private Button mlogout,mDirections;
    private String riderID="";
    private boolean isLoggingOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_for_driver);
        //Rawaa
        mDirections = findViewById(R.id.allDirections);
        polylines = new ArrayList<>();

      /*  start = new MarkerOptions().position(new LatLng(15.6569061,32.5447223)).title("Location 1");
        end = new MarkerOptions().position(new LatLng(15.6468091,32.5346233)).title("Location 2");

        mDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(start.getPosition(),end.getPosition(),"driving");
                new FetchURL(MapsActivityForDriver.this).execute(url,"driving");

            }
        });*/


       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    callPermissions();

        // Initialize Places.

        // Create a new Places client instance.
        // PlacesClient placesClient = Places.createClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mlogout = (Button) findViewById(R.id.logout);
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FirebaseAuth.getInstance().signOut();
                isLoggingOut =true;
                disconnectDriver();
                Intent goToFirstPage =new Intent(MapsActivityForDriver.this,MainActivity.class);
                startActivity(goToFirstPage);
                finish();
                return;
            }
        });
        getAssignedRider();
    }
    //getUrl function for directions
    private String getUrl(LatLng start ,LatLng end, String directionMode){
        String startPoint = "origin=" + start.latitude + "," + start.longitude;
        String endPoint = "destination=" + end.longitude + "," + end.longitude;
        String mode = "mode=" + directionMode;
        String parameters = startPoint + "&" + endPoint + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+ output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void getAssignedRider(){
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignRiderRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID).child("RiderTripID");
        assignRiderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                        riderID = dataSnapshot.getValue().toString();

                        getAssignedRiderPickupLocation();
                }
                else {
                    erasePolyLines();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getAssignedRiderPickupLocation(){

        DatabaseReference assignRiderPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("riderRequest").child(riderID).child("l");
        assignRiderPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng pickupLatLng = new LatLng(locationLat, locationLng);
                    mMap.addMarker(new MarkerOptions().position(pickupLatLng).title("Pickup location"));

                    getRouteToMarker(pickupLatLng);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRouteToMarker(LatLng pickupLatLng) {

        if ((ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    mLastLocation = location;
                }
            }
        });


                    Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), pickupLatLng)
                .build();
        routing.execute();
        Toast.makeText(this, "Done ya Rawah "+ "the lat"+mLastLocation.getLatitude()+ "the lng"+mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
    }

    private void fetchLastLocation() {
        if ((ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    mLastLocation = location;
                    Toast.makeText(getApplicationContext(), mLastLocation.getLatitude() +
                            "" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivityForDriver.this);
                }
            }
        });
    }

    public void callPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        String MsgContent = "Location permission is required to get driver location";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location permission")
                .setSettingsDialogTitle("Warning!");
        Permissions.check(this, permissions, MsgContent, options, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        // do your task.
                        requestLocationUpdates();
                    }
                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                        callPermissions();
                    }
                });
    }

    public void requestLocationUpdates(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PermissionChecker.PERMISSION_GRANTED) {

            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
                   // MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I Am Here");
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18)); //Range of values from 1 to 21
                  //  mMap.addMarker(markerOptions);

                    Log.e(TAG,"Lat: " + locationResult.getLastLocation().getLatitude()+ "Log: " + locationResult.getLastLocation().getLongitude());
                    //Saving latitude&longitude of driver who is available and using the app now, in firebase
                    String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid(); //will give me the id of current user
                    DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("driversAvailable");
                    DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("DriversWorking");
                    GeoFire geoFireAvailable =new GeoFire(refAvailable);
                    GeoFire geoFireWorking =new GeoFire(refWorking);

                    switch (riderID){
                        case "":
                            geoFireWorking.removeLocation(driverID);
                            geoFireAvailable.setLocation(driverID,new GeoLocation(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));

                            break;

                        default:
                            geoFireAvailable.removeLocation(driverID);
                            geoFireWorking.setLocation(driverID,new GeoLocation(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));

                            break;
                    }

                     }
            }, getMainLooper());
        } else callPermissions();
}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        return;
        }
       // buildGoogleApiClient();
       // mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true);
      //  mCurrentLocation = ;
       //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.addMarker(start);
        mMap.addMarker(end);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    mMap.setMyLocationEnabled(true);
                    fetchLastLocation();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
         LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I Am Here");
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11)); //Range of values from 1 to 21
        mMap.addMarker(markerOptions);
        //Saving latitude&longitude of driver who is available and using the app now, in firebase
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid(); //will give me the id of current user
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
        GeoFire geoFire =new GeoFire(ref);
        geoFire.setLocation(driverID,new GeoLocation(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isLoggingOut) {
            disconnectDriver();
        }
    }

    private void disconnectDriver(){
        //when driver close the app ,then---> Remove latitude&longitude of driver from firebase
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid(); //will give me the id of current user
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
        GeoFire geoFire =new GeoFire(ref);
        geoFire.removeLocation(driverID);
    }


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorAccent,R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

private void erasePolyLines(){
        for (Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
}

    @Override
    public void onTaskDone(Object... values) {
        if(currentPath != null)
            currentPath.remove();

        currentPath = mMap.addPolyline((PolylineOptions) values[0]);

    }
}
