package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {


    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;

    String protoname, name, password, emailget, UID;

    String Profilepictureundefined = "Profilepicture hasn't been chosen yet";
    String fullnameUndefined = "Full name not yet registered";
    String birthdateUndefined = "Date of birth not yet registered";

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Registration failed, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUsername);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
    }

    private Boolean validate(){
        Boolean result = false;


        protoname = userName.getText().toString().trim();
        name = "@" + protoname;
        password = userPassword.getText().toString().trim();
        emailget = userEmail.getText().toString().trim();

        int passwordlength = password.length();
        int usernamelength = name.length();

        if(name.isEmpty() || password.isEmpty() || emailget.isEmpty()){
            Toast.makeText(RegistrationActivity.this, "Please enter all the details", Toast.LENGTH_LONG).show();
        }

        else{
            if(!isEmailValid(emailget)){
                Toast.makeText(RegistrationActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
            }

            else{
                if(passwordlength < 6 ){
                    Toast.makeText(this, "Please make sure your password is at least 6 characters long", Toast.LENGTH_LONG).show();
                }

                else{
                    if(usernamelength < 3 ){
                        Toast.makeText(this, "Please make sure your username is at least 3 characters long", Toast.LENGTH_LONG).show();
                    }

                    else{
                        if(usernamelength > 31 ){
                            Toast.makeText(this, "Please make sure your username is not more than 30 characters long", Toast.LENGTH_LONG).show();
                        }

                        else{
                            if(!protoname.matches("[a-zA-Z._0-9]*")){
                                Toast.makeText(this, "Please make sure your username only consists of letters (a-z, A-Z) and/or numbers (0-9) and/or an underscore (_) and/or a dot (.)", Toast.LENGTH_LONG).show();
                            }
                            else{
                                result = true;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }


    private void sendEmailVerification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserDataToDatabase(); // upload de data pas naar de database als de email is gestuurd
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, Profile_First_Setup.class));
                    }

                    else{
                        Toast.makeText(RegistrationActivity.this, "Verification mail has not been send, please try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    private void sendUserDataToDatabase (){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
        this.UID=firebaseAuth.getUid();
        UserProfileToDatabase userProfile = new UserProfileToDatabase(Profilepictureundefined, this.UID, name, emailget, fullnameUndefined, birthdateUndefined);
        myRef.setValue(userProfile);
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
