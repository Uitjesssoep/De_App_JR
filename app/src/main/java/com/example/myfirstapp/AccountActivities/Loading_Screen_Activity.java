package com.example.myfirstapp.AccountActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.R;

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


            h.postDelayed(r, 5000); // is delayed voor 1,5 seconden

    }

    public void onBackPressed(){
        Toast.makeText(Loading_Screen_Activity.this, "Can't return now, please wait...", Toast.LENGTH_SHORT).show();
    }
}
