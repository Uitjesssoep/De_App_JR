package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUp_Success_Activity extends AppCompatActivity {

    private Button Continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__success_);

        Continue = findViewById(R.id.btnContinueSuccessSignUp);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp_Success_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
