package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

        private EditText Name;
        private EditText Password;
        private Button Login;
        private TextView RegisterText;
        private TextView PasswordResetText;

        private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotFirstTime(); // was eerst van belang voor iets, maar dat heb ik aangepast en ik was te lui om t weer terug te verplaatsen naar 'oncreate' dus ik heb t in 'notfirsttime' gelaten

    }


    private void NotFirstTime(){

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        PasswordResetText = (TextView)findViewById(R.id.tvForgotPassword);
        RegisterText = (TextView)findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }


        Login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        }));

        RegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        PasswordResetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Forgot_Password_Activity.class));
            }
        });

    }


    private void validate(String userName, String userPassword) {


        String username = Name.getText().toString();
        String password = Password.getText().toString();


        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Please enter all the details", Toast.LENGTH_LONG).show();
        }

        else{

            firebaseAuth.signInWithEmailAndPassword(userName.trim(), userPassword.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkEmailVerification();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login failed, please check your details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verificationemail = firebaseUser.isEmailVerified();

        if(verificationemail){
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(MainActivity.this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }


    }

}
