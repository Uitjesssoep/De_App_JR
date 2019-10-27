package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {


    private EditText EmailResetPassword;
    private Button ResetPasswordEmailSend;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password_);

        EmailResetPassword = (EditText)findViewById(R.id.etPasswordResetEmail);
        ResetPasswordEmailSend = (Button)findViewById(R.id.btnResetPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        ResetPasswordEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremailreset = EmailResetPassword.getText().toString().trim();

                if(useremailreset.isEmpty()){
                    Toast.makeText(Forgot_Password_Activity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Forgot_Password_Activity.this, "Please make sure you entered a registered email address", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
