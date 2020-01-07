package com.example.myfirstapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {


    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin, alreadyAccountText, ErrorUsername, ErrorEmail, ErrorPassword;

    String protoname, password, emailget, UID;

    private Boolean AllGood = false;

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

                ErrorUsername.setVisibility(View.INVISIBLE);
                ErrorEmail.setVisibility(View.INVISIBLE);
                ErrorPassword.setVisibility(View.INVISIBLE);

                userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                userPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

                if(AllGood){
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    protoname = userName.getText().toString().trim();
                    String name = "@" + protoname;

                    Intent intent = new Intent(RegistrationActivity.this, Profile_First_Setup.class);
                    intent.putExtra("username", name);
                    intent.putExtra("password", user_password);
                    intent.putExtra("email", user_email);
                    startActivity(intent);

                    /*firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Registration failed, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });

        alreadyAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUsername);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        alreadyAccountText = (TextView)findViewById(R.id.tvAlreadyAccountReg);
        ErrorUsername = findViewById(R.id.tvUsernameErrorReg);
        ErrorEmail = findViewById(R.id.tvEmailErrorReg);
        ErrorPassword = findViewById(R.id.tvPasswordErrorReg);

        //voor het weghalen van de actionbar

        getSupportActionBar().hide();


        //voor het geven van kleur aan de status bar:

        Window window = RegistrationActivity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(RegistrationActivity.this, R.color.statusBarColorLogin));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //underline

        userLogin.setPaintFlags(userLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //verberg error messages

        ErrorUsername.setVisibility(View.INVISIBLE);
        ErrorEmail.setVisibility(View.INVISIBLE);
        ErrorPassword.setVisibility(View.INVISIBLE);

    }

    private void validate(){

        AllGood = false;

        protoname = userName.getText().toString().trim();
        final String name = "@" + protoname;
        password = userPassword.getText().toString().trim();
        emailget = userEmail.getText().toString().trim();

        int passwordlength = password.length();
        int usernamelength = protoname.length();

        if(name.isEmpty() || password.isEmpty() || emailget.isEmpty()){
            userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            userPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);

            ErrorUsername.setVisibility(View.VISIBLE);
            ErrorEmail.setVisibility(View.VISIBLE);
            ErrorPassword.setVisibility(View.VISIBLE);

            ErrorUsername.setText("Please fill in all the details");
            ErrorEmail.setText("Please fill in all the details");
            ErrorPassword.setText("Please fill in all the details");
        }

        else{
            if(!isEmailValid(emailget)){
                userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                ErrorEmail.setVisibility(View.VISIBLE);
                ErrorEmail.setText("Please enter a valid email adress");
            }

            else{
                if(passwordlength < 6 ){
                    ErrorPassword.setVisibility(View.VISIBLE);
                    userPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    ErrorPassword.setText("Make sure your password is at least 6 characters long");
                }

                else{
                    if(usernamelength < 3 ){
                        ErrorUsername.setText("Make sure your username is at least 3 characters long");
                        ErrorUsername.setVisibility(View.VISIBLE);
                        userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    }

                    else{
                        if(usernamelength > 31 ){
                            ErrorUsername.setText("Make sure your username does not exceed the 30 character limit");
                            ErrorUsername.setVisibility(View.VISIBLE);
                            userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                        }

                        else{
                            if(!protoname.matches("[a-zA-Z._0-9]*")){
                                ErrorUsername.setText("Make sure your username does not contain weird symbols");
                                ErrorUsername.setVisibility(View.VISIBLE);
                                userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                            }
                            else{

                                DatabaseReference CheckIfUsernameExists = FirebaseDatabase.getInstance().getReference("Usernames");

                                CheckIfUsernameExists.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(name)){
                                            ErrorUsername.setText("This username is already in use");
                                            ErrorUsername.setVisibility(View.VISIBLE);
                                            userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                                        }
                                        else{

                                            AllGood();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        Toast.makeText(RegistrationActivity.this, "Couldn't collect data from database, please try again later", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private void AllGood() {

        AllGood = true;

    }


    /*private void sendEmailVerification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserDataToDatabase(); // upload de data pas naar de database als de email is gestuurd
                        finish();
                        Intent intent = new Intent(RegistrationActivity.this, Profile_First_Setup.class);
                        startActivity(intent);
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
    }*/


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
