package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfirstapp.Imageposts.ImagesFeed;
import com.example.myfirstapp.Textposts.General_Feed_Activity;

public class Choose_FeedType_Activity extends AppCompatActivity {

    private Button Media_Button, Text_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__feed_type);
        SetupUI();

        Media_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_FeedType_Activity.this, ImagesFeed.class));
            }
        });

        Text_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_FeedType_Activity.this, General_Feed_Activity.class));
            }
        });

    }

    private void SetupUI() {

        Media_Button = findViewById(R.id.btnMediaFeed);
        Text_Button = findViewById(R.id.btnTextFeed);

    }
}
