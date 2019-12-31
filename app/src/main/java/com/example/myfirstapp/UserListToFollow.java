package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserListToFollow extends AppCompatActivity {

    private Button Follow;
    private TextView Username;
    private ImageView ProfilePicture;


    private void SetupUI(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_to_follow);
    }
}

