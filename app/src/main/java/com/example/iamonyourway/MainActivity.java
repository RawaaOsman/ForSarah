package com.example.iamonyourway;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    //Rawaa start Coding from here -_-
    private Button mRider, mDriver ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDriver = (Button) findViewById(R.id.Driver) ;
        mRider = (Button) findViewById(R.id.Rider) ;

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDriverLogin = new Intent(MainActivity.this, DriverLoginActivity.class);
                // startActivities(gotoDriverRegisteration);
                startActivity(gotoDriverLogin);
                finish();
                return;
            }
        });

        mRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRiderLogin = new Intent(MainActivity.this, RiderLoginActivity.class);
                startActivity(gotoRiderLogin);
                finish();
                return;
            }
        });
    }
}
