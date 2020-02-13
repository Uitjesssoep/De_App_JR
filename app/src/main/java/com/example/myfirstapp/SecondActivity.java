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
import androidx.fragment.app.Fragment;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.MainActivity;
import com.example.myfirstapp.Chatroom.Chat_Room_MakeOrSearch_Activity;
import com.example.myfirstapp.Imageposts.ImagesFeed;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.example.myfirstapp.Users.UserListToFollow;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    SharedPrefNightMode sharedPrefNightMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

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
            finish();
        }
    }


    private void Checked(){
        //bottom navigation view dingen
        Intent home = new Intent(SecondActivity.this, Layout_Manager_BottomNav_Activity.class);
        home.putExtra("Type", "StartUp");
        startActivity(home);
        finish();
    }

}
