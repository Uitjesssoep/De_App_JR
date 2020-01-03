package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfirstapp.Textposts.Upload_TextPost_Activity;

public class Choose_PostType_Activity extends AppCompatActivity {

    private Button Media_Button, Text_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__post_type);

        SetupUI();

        Media_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_PostType_Activity.this, Upload_Images_Activity.class));
            }
        });

        Text_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_PostType_Activity.this, Upload_TextPost_Activity.class));
            }
        });

    }

    private void SetupUI() {

        Media_Button = (Button)findViewById(R.id.btnMediaPost);
        Text_Button = (Button)findViewById(R.id.btnTextPost);

    }
}
