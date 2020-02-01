package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myfirstapp.AccountActivities.MainActivity;
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
                startActivity(new Intent(Choose_FeedType_Activity.this, Layout_Manager_BottomNav_Activity.class));
            }
        });

    }

    private void SetupUI() {

        Media_Button = findViewById(R.id.btnMediaFeed);
        Text_Button = findViewById(R.id.btnTextFeed);

        //voor het geven van kleur aan de status bar:

        Window window = Choose_FeedType_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Choose_FeedType_Activity.this, R.color.statusBarColorLogin));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }
}
