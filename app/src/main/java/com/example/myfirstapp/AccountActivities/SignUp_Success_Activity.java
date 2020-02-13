package com.example.myfirstapp.AccountActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;

public class SignUp_Success_Activity extends AppCompatActivity {

    private Button Continue;
    SharedPrefNightMode sharedPrefNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__success_);

        Continue = findViewById(R.id.btnContinueForgotPasswordSuccess2);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp_Success_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
