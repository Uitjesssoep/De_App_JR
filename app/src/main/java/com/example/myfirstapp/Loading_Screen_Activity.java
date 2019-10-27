package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Loading_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading__screen_);


            final Handler h = new Handler();


            Runnable r = new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(Loading_Screen_Activity.this, Account_Info_Activity.class));

                    finish();

                }
            };


            h.postDelayed(r, 5000); // will be delayed for 1.5 seconds

    }
}
