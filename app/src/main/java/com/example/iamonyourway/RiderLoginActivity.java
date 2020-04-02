<<<<<<< HEAD
package com.example.iamonyourway;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
//Rawaa's Imports:

import android.text.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;



public class RiderLoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_rider_login);
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
                            Toast.makeText(RiderLoginActivity.this, "Welcome, now you are log in " , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MapActivityForRider.class)); //get me the same user who get in the DriverLoginPage
                        } else {
                            Toast.makeText(RiderLoginActivity.this, "What went wrong:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                Intent gotoDriverRegistration = new Intent(RiderLoginActivity.this, RiderSignUpActivity.class);
                startActivity(gotoDriverRegistration);
                finish();
                return;
            }
        });//End of REGISTRATION button functionality
    } // OnCreate method End
    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){ // if true, this means that user is already signed in
            Toast.makeText(RiderLoginActivity.this, "you are already in",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
=======
package com.example.iamonyourway;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
//Rawaa's Imports:

import android.text.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;



public class RiderLoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_rider_login);
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
                            Toast.makeText(RiderLoginActivity.this, "Welcome, now you are log in " , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MapActivityForRider.class)); //get me the same user who get in the DriverLoginPage
                        } else {
                            Toast.makeText(RiderLoginActivity.this, "What went wrong:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                Intent gotoDriverRegistration = new Intent(RiderLoginActivity.this, RiderSignUpActivity.class);
                startActivity(gotoDriverRegistration);
                finish();
                return;
            }
        });//End of REGISTRATION button functionality
    } // OnCreate method End
    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){ // if true, this means that user is already signed in
            Toast.makeText(RiderLoginActivity.this, "you are already in",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
>>>>>>> 6de8af1298de65d4e250adcf7af579c36228500b
