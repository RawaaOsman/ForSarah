package com.example.iamonyourway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent gotoDriverRegisteration = new Intent(MainActivity.this, DriverLoginActivity.class);
               // startActivities(gotoDriverRegisteration);
                startActivity(gotoDriverRegisteration);
                finish();
                return;
            }
        });
    }
}
