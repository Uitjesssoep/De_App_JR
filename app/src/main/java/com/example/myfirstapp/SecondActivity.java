package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.MainActivity;
import com.example.myfirstapp.Chatroom.Chat_Room_MakeOrSearch_Activity;
import com.example.myfirstapp.Users.UserListToFollow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {


    private Button AccountInfoButton, SearchUsersButton, PostButton, GeneralFeedButton, UsersButton;

    private FirebaseAuth firebaseAuth;

    public static boolean firstSetup;



    //voor menu in de action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:

                Intent intent = new Intent(SecondActivity.this, App_Settings_Activity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //voor het geven van kleur aan de status bar:

        Window window = SecondActivity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(SecondActivity.this, R.color.slighly_darker_mainGreen));

        firebaseAuth = FirebaseAuth.getInstance();

        checkEmailVerification();


        //action bar shit

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verificationemail = firebaseUser.isEmailVerified();

        if(verificationemail){
            Checked();
        }

        else{
            Toast.makeText(SecondActivity.this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        }
    }


    private void Checked(){
        SearchUsersButton = findViewById(R.id.btnSearchUsersSA);
        AccountInfoButton = findViewById(R.id.btnAccountInfo);
        PostButton = findViewById(R.id.btnPost);
        GeneralFeedButton = findViewById(R.id.btnGeneralFeed);
        UsersButton = findViewById(R.id.btUsers);

        AccountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, Account_Info_Activity.class));
            }
        });

        SearchUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, Chat_Room_MakeOrSearch_Activity.class));
            }
        });
       PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, Choose_PostType_Activity.class));
            }
        });

       GeneralFeedButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(SecondActivity.this, Choose_FeedType_Activity.class));
           }
       });
       UsersButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(SecondActivity.this, UserListToFollow.class));
           }
       });
    }

}
