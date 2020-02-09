package com.example.myfirstapp.AccountActivities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {


    private EditText EmailResetPassword;
    private Button ResetPasswordEmailSend;
    private FirebaseAuth firebaseAuth;
    private TextView ErrorEmail, BackLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password_);

        setTheme(R.style.AppTheme);

        //voor het geven van kleur aan de status bar:

        Window window = Forgot_Password_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Forgot_Password_Activity.this, R.color.statusBarColorLogin));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        EmailResetPassword = (EditText)findViewById(R.id.etPasswordResetEmail);
        ResetPasswordEmailSend = (Button)findViewById(R.id.btnResetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        ErrorEmail = findViewById(R.id.tvPasswordErrorForgot2);
        BackLogin = findViewById(R.id.tvBackToLoginForgot);

        EmailResetPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
        ErrorEmail.setVisibility(View.INVISIBLE);
        ErrorEmail.setText("Please fill in all the details");

        BackLogin.setPaintFlags(BackLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        BackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent(Forgot_Password_Activity.this, MainActivity.class);
                startActivity(Login);
                finish();
            }
        });


        ResetPasswordEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremailreset = EmailResetPassword.getText().toString().trim();

                EmailResetPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                ErrorEmail.setVisibility(View.INVISIBLE);
                ErrorEmail.setText("Please fill in all the details");

                if(useremailreset.isEmpty()){
                    EmailResetPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    ErrorEmail.setVisibility(View.VISIBLE);
                    ErrorEmail.setText("Please enter your email");
                }

                else{
                    firebaseAuth.sendPasswordResetEmail(useremailreset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String useremailreset2 = EmailResetPassword.getText().toString().trim();
                                Toast.makeText(Forgot_Password_Activity.this, "The email has been sent, please check your inbox", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Forgot_Password_Activity.this, ForgotPassword_Sucess_Activity.class);
                                intent.putExtra("entered_email", useremailreset2);
                                startActivity(intent);
                                finish();
                            }

                            else{
                                EmailResetPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                                ErrorEmail.setVisibility(View.VISIBLE);
                                ErrorEmail.setText("Please enter a registered email");
                            }
                        }
                    });
                }
            }
        });
    }
}
