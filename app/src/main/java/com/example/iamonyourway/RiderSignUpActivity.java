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

import com.firebase.geofire.GeoFire;
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

public class RiderSignUpActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, mFName, mAdd, mBirthdate, mPhone, mNational;
    private Button mLogout, mRegistration;
    private ProgressBar NotificationBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    public static final String TAG = "TAG";
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_sign_up);
        // assigning values for the preveuse variables
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mLogout = (Button) findViewById(R.id.logout);
        mRegistration = (Button) findViewById(R.id.registration);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mFName = (EditText) findViewById(R.id.fullName);
        mAdd = (EditText) findViewById(R.id.address);
        mBirthdate = (EditText) findViewById(R.id.birthdate);
        mPhone = (EditText) findViewById(R.id.phone);
        mNational = (EditText) findViewById(R.id.nationality);

        NotificationBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //check if the user is already sign in, if true--> redirect him to the mainActivity(Home page) or DriverLoginPage??!!
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(RiderSignUpActivity.this, "YOU ARE ALREADY SIGNED IN", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MapActivityForRider.class));
            finish();
        }

        //Activate Registration button
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailField = mEmail.getText().toString().trim();
                final String passwordField = mPassword.getText().toString().trim();
                final String fNameField = mFName.getText().toString();
                final String addField = mAdd.getText().toString();
                final String phoneField = mPhone.getText().toString();
                final String birthdateField = mBirthdate.getText().toString();
                final String nationalityField = mNational.getText().toString();
                //Verification of User data entering
                if (TextUtils.isEmpty(emailField)) {
                    mEmail.setError("YOUR EMAIL IS REQUIRED");
                    mEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passwordField)) {
                    mPassword.setError("YOUR PASSWORD IS REQUIRED");
                    mPassword.requestFocus();
                    return;
                }
                if (passwordField.length() < 10) {
                    mPassword.setError("YOUR PASSWORD SHOULD BE AT LEAST 10 CHARACTERS LONG");
                    mPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phoneField)) {
                    mPhone.setError("YOUR MOBILE NUMBER IS REQUIRED");
                    mPhone.requestFocus();
                    return;
                }
                if (phoneField.length() < 10) {
                    mPhone.setError("YOUR MOBILE NUMBER SHOULD BE AT LEAST 10 NUMBER");
                    mPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(fNameField)) {
                    mFName.setError("YOUR NAME IS REQUIRED");
                    mFName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(addField)) {
                    mAdd.setError("YOUR ADDRESS IS REQUIRED");
                    mAdd.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(birthdateField)) {
                    mBirthdate.setError("YOUR DATE OF BIRTH IS REQUIRED");
                    mBirthdate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(nationalityField)) {
                    mNational.setError("YOUR NATIONALITY IS REQUIRED");
                    mNational.requestFocus();
                    return;
                }
                //End of Verification

                NotificationBar.setVisibility(View.VISIBLE);//Turn on the Progress bar

                //After i insure that user entered data correctly, i will send his data to Firebase
                mAuth.createUserWithEmailAndPassword(emailField, passwordField).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RiderSignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Riders").child(userID);
                            databaseReference.setValue(true);
                            //DocumentReference docReference = mFirestore.collection("Users.Rider").document(userID);
                            //Map<String,Object> user = new HashMap<>();
                            //user.put("UEmail",emailField);
                            //user.put("UPassword",passwordField);
                        /*docReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess :" + userID + " User Account is created successfully");
                            }
                        }); */
                            startActivity(new Intent(getApplicationContext(), RiderSignUpActivity.class)); //get me the same user who get in the DriverLoginPage
                        } else {
                            Toast.makeText(RiderSignUpActivity.this, "What went wrong:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

                /*//when driver press logout button ,then---> Remove the RiderID from firebase
                String riderID = FirebaseAuth.getInstance().getCurrentUser().getUid(); //will give me the id of current user
                userID = mAuth.getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Riders").child(userID);
                GeoFire geoFire =new GeoFire(ref);
                geoFire.removeLocation(riderID); */

                Intent gotoMainActivity = new Intent(RiderSignUpActivity.this, MainActivity.class);
                startActivity(gotoMainActivity);

                mAuth.removeAuthStateListener(firebaseAuthListener);

                finish();
                return;
            }
        });//End of LOGOUT button functionality
    }
    }
