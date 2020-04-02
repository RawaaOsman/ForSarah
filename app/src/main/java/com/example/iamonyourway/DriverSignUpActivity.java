
package com.example.iamonyourway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
//Rawaa's Imports:

import android.text.TextUtils;
//import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

public class DriverSignUpActivity extends AppCompatActivity {
    private EditText mEmail, mPassword, mFName, mAdd, mJob, mBirthdate, mPhone, mNational, mCarType, mCarModel, mCarColor, mCarNumber, mCarManufacture, mDriverLicense;
    private Button mLogout, mRegistration;
    private ProgressBar NotificationBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    public static final String TAG = "TAG";
    String driverID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);

// assigning values for the preveuse variables
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mLogout = (Button) findViewById(R.id.logout) ;
        mRegistration = (Button) findViewById(R.id.registration) ;
        mEmail = (EditText) findViewById(R.id.email) ;
        mPassword = (EditText) findViewById(R.id.password) ;
        mFName = (EditText) findViewById(R.id.fullName) ;
        mAdd = (EditText) findViewById(R.id.address) ;
        mJob = (EditText) findViewById(R.id.job) ;
        mBirthdate = (EditText) findViewById(R.id.birthdate) ;
        mPhone = (EditText) findViewById(R.id.phone) ;
        mNational = (EditText) findViewById(R.id.nationality) ;
        mCarType = (EditText) findViewById(R.id.carType) ;
        mCarModel = (EditText) findViewById(R.id.carModel) ;
        mCarColor = (EditText) findViewById(R.id.carColor) ;
        mCarNumber = (EditText) findViewById(R.id.carNumber) ;
        mCarManufacture = (EditText) findViewById(R.id.manufactureDate) ;
        mDriverLicense = (EditText) findViewById(R.id.driverLicense) ;

        NotificationBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //check if the user is already sign in, if true--> redirect him to the mainActivity(Home page) or DriverLoginPage??!!
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(DriverSignUpActivity.this,"YOU ARE ALREADY SIGNED IN",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MapsActivityForDriver.class));
            finish();
        }

        //Activate Registration button
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailField = mEmail.getText().toString().trim() ;
                final String passwordField = mPassword.getText().toString().trim() ;
                final String fNameField = mFName.getText().toString() ;
                final String jobField = mJob.getText().toString() ;
                final String addField = mAdd.getText().toString() ;
                final String phoneField = mPhone.getText().toString() ;
                final String birthdateField = mBirthdate.getText().toString() ;
                final String nationalityField = mNational.getText().toString() ;
                final String carModelField = mCarModel.getText().toString() ;
                final String carTypeField = mCarType.getText().toString() ;
                final String carColorField = mCarColor.getText().toString() ;
                final String carNumberField = mCarNumber.getText().toString() ;
                final String carManufactureField = mCarManufacture.getText().toString() ;
                final String driverLicenceField = mDriverLicense.getText().toString() ;
                //Verification of User data entering
                if(TextUtils.isEmpty(emailField)){
                    mEmail.setError("YOUR EMAIL IS REQUIRED");
                    mEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(passwordField)){
                    mPassword.setError("YOUR PASSWORD IS REQUIRED");
                    mPassword.requestFocus();
                    return;
                }
                if(passwordField.length() < 10){
                    mPassword.setError("YOUR PASSWORD SHOULD BE AT LEAST 10 CHARACTERS LONG");
                    mPassword.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(phoneField)){
                    mPhone.setError("YOUR MOBILE NUMBER IS REQUIRED");
                    mPhone.requestFocus();
                    return;
                }
                if(phoneField.length() < 10){
                    mPhone.setError("YOUR MOBILE NUMBER SHOULD BE AT LEAST 10 NUMBER");
                    mPhone.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(fNameField)){
                    mFName.setError("YOUR NAME IS REQUIRED");
                    mFName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(addField)){
                    mAdd.setError("YOUR ADDRESS IS REQUIRED");
                    mAdd.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(jobField)){
                    mJob.setError("YOUR JOB IS REQUIRED");
                    mJob.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(birthdateField)){
                        mBirthdate.setError("YOUR DATE OF BIRTH IS REQUIRED");
                    mBirthdate.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(carModelField)){
                    mCarModel.setError("YOUR CAR MODEL IS REQUIRED");
                    mCarModel.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(carColorField)){
                    mCarColor.setError("YOUR CAR COLOR IS REQUIRED");
                    mCarColor.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(carTypeField)){
                    mCarType.setError("YOUR CAR TYPE IS REQUIRED");
                    mCarType.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(carNumberField)){
                    mCarNumber.setError("YOUR CAR NUMBER IS REQUIRED");
                    mCarNumber.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(nationalityField)){
                    mNational.setError("YOUR NATIONALITY IS REQUIRED");
                    mNational.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(driverLicenceField)){
                    mDriverLicense.setError("YOUR DRIVE LICENSE NUMBER IS REQUIRED");
                    mDriverLicense.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(carManufactureField)){
                    mCarManufacture.setError("YOUR Car Manufacturing Date IS REQUIRED");
                    mCarManufacture.requestFocus();
                    return;
                }
                //End of Verification

                NotificationBar.setVisibility(View.VISIBLE);//Turn on the Progress bar

                //After i insure that user entered data correctly, i will send his data to Firebase
                mAuth.createUserWithEmailAndPassword(emailField,passwordField).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DriverSignUpActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();
                        driverID = mAuth.getCurrentUser().getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverID);
                        databaseReference.setValue(true);
                        //DocumentReference docReference = mFirestore.collection("Users.Driver").document(userID);
                        //Map<String,Object> user = new HashMap<>();
                        //user.put("UEmail",emailField);
                        //user.put("UPassword",passwordField);
                        /*docReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess :" + userID + " User Account is created successfully");
                            }
                        }); */
                        startActivity(new Intent(getApplicationContext(),DriverLoginActivity.class)); //get me the same user who get in the DriverLoginPage
                    }else {
                        Toast.makeText(DriverSignUpActivity.this, "What went wrong:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        NotificationBar.setVisibility(View.GONE);//Turn off the Progress bar
                    }
                    }
                });

            }
        }); //End of REGISTRATION button functionality

        //Activate LOGOUT button
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMainActivity = new Intent(DriverSignUpActivity.this, MainActivity.class);
                startActivity(gotoMainActivity);
                finish();
                return;
            }
        });//End of LOGOUT button functionality

    }//End of OnCreate method
}

