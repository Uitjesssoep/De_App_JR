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

public class ForgotPassword_Sucess_Activity extends AppCompatActivity {


    private Button Continue;
    private TextView EmailSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password__sucess_);

        setTheme(R.style.AppTheme);

        //voor het geven van kleur aan de status bar:

        Window window = ForgotPassword_Sucess_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(ForgotPassword_Sucess_Activity.this, R.color.statusBarColorLogin));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


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
