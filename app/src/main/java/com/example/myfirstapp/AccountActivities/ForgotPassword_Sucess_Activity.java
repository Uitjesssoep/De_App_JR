package com.example.myfirstapp.AccountActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;

public class ForgotPassword_Sucess_Activity extends AppCompatActivity {


    private Button Continue;
    private TextView EmailSent;
    SharedPrefNightMode sharedPrefNightMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password__sucess_);

        Continue = findViewById(R.id.btnContinueForgotPasswordSuccess);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Continue = new Intent(ForgotPassword_Sucess_Activity.this, MainActivity.class);
                startActivity(Continue);
                finish();
            }
        });

        EmailSent = findViewById(R.id.tvExplainingForgotPasswordSuccess);
        String entered_email = getIntent().getExtras().get("entered_email").toString();
        EmailSent.setText("An email has been sent to\n"+ entered_email + "\n" + "containing a link to reset your password.");

    }
}
