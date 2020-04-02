package com.example.iamonyourway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
//import com.google.firebase.quickstart.auth.R;

public class DriverLoginActivity extends AppCompatActivity {

    //Rawaa start coding from here -_- // My First try
    // public static final String TAG = "TAG";
    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;
    private ProgressBar NotificationBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    // String userID;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        //Rawaa -_- old code
        // mAuth = FirebaseAuth.getInstance();
        //mFirestore = FirebaseFirestore.getInstance();
            /*firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent gotoDriverRegistration = new Intent(DriverLoginActivity.this, DriverLoginActivity.class);
                    // startActivities(gotoDriverRegisteration);
                    startActivity(gotoDriverRegistration);
                    finish();
                    return;
                }
            }
        };*/

        // assigning values for the previuse variables
        mLogin = (Button) findViewById(R.id.login) ;
        mRegistration = (Button) findViewById(R.id.registration) ;
        mEmail = (EditText) findViewById(R.id.email) ;
        mPassword = (EditText) findViewById(R.id.password) ;
        NotificationBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //Activate LOGIN button
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailField = mEmail.getText().toString().trim() ;
                final String passwordField = mPassword.getText().toString().trim() ;
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
                }//End of Verification
                //Turn on the Progress bar
                NotificationBar.setVisibility(View.VISIBLE);
                //Then, Authenticate the user
                mAuth.signInWithEmailAndPassword(emailField,passwordField).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DriverLoginActivity.this, "Welcome, now you are log in " , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MapsActivityForDriver.class)); //get me the same user who get in the DriverLoginPage
                        } else {
                            Toast.makeText(DriverLoginActivity.this, "What went wrong:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            NotificationBar.setVisibility(View.GONE);//Turn off the Progress bar
                        }
                    }
                });
            }
        }); //End of LOGIN button functionality

        //Activate Registration button
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDriverRegistration = new Intent(DriverLoginActivity.this, DriverSignUpActivity.class);
                startActivity(gotoDriverRegistration);
                finish();
                return;
            }
        });//End of REGISTRATION button functionality

        //Rawaa -_- old code
        /*mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String email = mEmail.getText().toString();
               // final String password = mPassword.getText().toString();

                //Verification of User data entering
                final String emailField = mEmail.getText().toString().trim() ;
                final String passwordField = mPassword.getText().toString().trim() ;
                if(emailField.isEmpty()){
                    mEmail.setError("YOUR EMAIL IS REQUIRED");
                    mEmail.requestFocus();
                    return;
                }
                if(passwordField.isEmpty()){
                    mPassword.setError("YOUR PASSWORD IS REQUIRED");
                    mPassword.requestFocus();
                    return;
                }
                if(passwordField.length() < 10){
                    mPassword.setError("YOUR PASSWORD SHOULD BE AT LEAST 10 CHARACTERS LONG");
                    mPassword.requestFocus();
                    return;
                }//End of Verification

                NotificationBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailField,passwordField).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        NotificationBar.setVisibility(View.GONE);
                        if(!task.isSuccessful()){
                            Toast.makeText(DriverLoginActivity.this, "Sign Up Fieled",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }else { //The registration is successful
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference docReference = mFirestore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("UEmail",emailField);
                            user.put("UPassword",passwordField);
                            docReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess :" + userID + " User Account is created successfully");
                                }
                            });
                            User userObjOfUserClass = new User(emailField,passwordField);
                            FirebaseFirestore.getInstance();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //DatabaseRefernce ;
                           // updateUI(user);
                        }
                    }
                });
            }//OnClick method End
        });*/
    } // OnCreate method End
    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){ // if true, this means that user is already signed in
            Toast.makeText(DriverLoginActivity.this, "you are already in",Toast.LENGTH_SHORT).show();
        }

        //Rawaa -_- old code
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        //mAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    //Rawaa -_- old code
   /* private void updateUI(FirebaseUser user) {
       hideProgressDialog();
        if (user != null) {

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            mEmail.setText(R.string.signed_out);
            mPassword.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }*/

}
