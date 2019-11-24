package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {


    private Button AccountInfoButton, SearchUsersButton, PostButton, GeneralFeedButton;

    private FirebaseAuth firebaseAuth;

    public static boolean firstSetup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        firebaseAuth = FirebaseAuth.getInstance();

        checkEmailVerification();

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
    }

}
