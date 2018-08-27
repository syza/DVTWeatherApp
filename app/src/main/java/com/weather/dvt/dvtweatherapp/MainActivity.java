package com.weather.dvt.dvtweatherapp;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.weather.dvt.dvtweatherapp.fragments.Forest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //REQUESTING USER LOCATION PERMISSION
        requestPermission();

        //HIDING THE ACTIVITY ACTION BAR
        getSupportActionBar().hide();

        //ATTACHING THE FOREST FRAGMENT TO THE MAIN MAIN ACTIVITY
        getFragmentManager().beginTransaction().add(R.id.mainContaioner,new Forest()).commit();

    }

    //##################################################################################################################
    //ALLOWING THE APP TO ACCESS USER LOCATION PERMISSION
    //##################################################################################################################
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


}
