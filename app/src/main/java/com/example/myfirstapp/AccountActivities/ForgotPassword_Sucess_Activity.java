package com.example.myfirstapp.AccountActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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

        setLightStatusBar(ForgotPassword_Sucess_Activity.this);

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

    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColorLogin)); // optional
        }
    }

}
