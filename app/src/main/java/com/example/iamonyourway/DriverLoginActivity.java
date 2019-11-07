package com.example.iamonyourway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DriverLoginActivity extends AppCompatActivity {
//Rawaa start coding from here -_-
    private EditText mEmail, mPassword;
    private Button mLogin, mRegisteration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        //Rawaa -_-
        mLogin = (Button) findViewById(R.id.login) ;
        mRegisteration = (Button) findViewById(R.id.registeration) ;
        mEmail = (EditText) findViewById(R.id.email) ;
        mPassword = (EditText) findViewById(R.id.password) ;
    }
}
