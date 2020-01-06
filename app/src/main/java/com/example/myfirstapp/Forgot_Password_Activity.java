package com.example.myfirstapp;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {


    private EditText EmailResetPassword;
    private Button ResetPasswordEmailSend;
    private FirebaseAuth firebaseAuth;
    private TextView ErrorEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password_);


        //voor het weghalen van de actionbar

        getSupportActionBar().hide();


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

        EmailResetPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
        ErrorEmail.setVisibility(View.INVISIBLE);
        ErrorEmail.setText("Please fill in all the details");


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
                                Toast.makeText(Forgot_Password_Activity.this, "The email has been sent, please check your inbox", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(Forgot_Password_Activity.this, MainActivity.class));
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
