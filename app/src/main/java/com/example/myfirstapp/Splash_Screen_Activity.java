package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash_Screen_Activity extends AppCompatActivity {

    private static int splash_time_out = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash__screen);

        new Handler().postDelayed(new Runnable(){
            @Override
                    public void run(){
                Intent Terug = new Intent(Splash_Screen_Activity.this, MainActivity.class);
                startActivity(Terug);
                finish();
            }
        }, splash_time_out);

    }

    public void OnBackPressed(){

    }

}
