package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myfirstapp.AccountActivities.MainActivity;

public class Splash_Screen_Activity extends AppCompatActivity {

    SharedPrefNightMode sharedPrefNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));

    }
}
