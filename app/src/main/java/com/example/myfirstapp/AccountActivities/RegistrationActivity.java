package com.example.myfirstapp.AccountActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
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
import com.example.myfirstapp.SharedPrefNightMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {


    private EditText userName, userPassword, userEmail, userPasswordRepeat;
    private Button regButton;
    private TextView userLogin, alreadyAccountText, ErrorUsername, ErrorEmail, ErrorPassword, ErrorPasswordRepeat;

    SharedPrefNightMode sharedPrefNightMode;

    String protoname, password, emailget, UID;

    String Profilepictureundefined = "Profilepicture hasn't been chosen yet";
    String fullnameUndefined = "Full name not yet registered";
    String birthdateUndefined = "Date of birth not yet registered";

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else {
            setTheme(R.style.AppTheme);

            setLightStatusBar(RegistrationActivity.this);
        }

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
                ErrorPasswordRepeat.setVisibility(View.INVISIBLE);

                userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                userPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                userPasswordRepeat.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

                validate();

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
        ErrorPasswordRepeat = findViewById(R.id.tvPasswordErrorRegRepeat);
        userPasswordRepeat = findViewById(R.id.etUserPasswordRepeat);

        //underline

        userLogin.setPaintFlags(userLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //verberg error messages

        ErrorUsername.setVisibility(View.INVISIBLE);
        ErrorEmail.setVisibility(View.INVISIBLE);
        ErrorPassword.setVisibility(View.INVISIBLE);
        ErrorPasswordRepeat.setVisibility(View.INVISIBLE);

    }

    private void validate(){

        protoname = userName.getText().toString().trim();
        final String name = "@" + protoname;
        password = userPassword.getText().toString().trim();
        emailget = userEmail.getText().toString().trim();

        String passwordRepeat = userPasswordRepeat.getText().toString().trim();

        int passwordlength = password.length();
        int usernamelength = protoname.length();

        if(name.isEmpty()){
            userName.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            ErrorUsername.setVisibility(View.VISIBLE);
            ErrorUsername.setText("Please fill in all the details");
        }

        else{

            if(emailget.isEmpty()){
                userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                ErrorEmail.setText("Please fill in all the details");
                ErrorEmail.setVisibility(View.VISIBLE);
            }
            else{
                if(passwordlength < 6 ){
                    ErrorPassword.setVisibility(View.VISIBLE);
                    userPassword.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    ErrorPassword.setText("Make sure your password is at least 6 characters long");
                }
                else {

                    if(passwordRepeat.isEmpty()){
                        userPasswordRepeat.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                        ErrorPasswordRepeat.setVisibility(View.VISIBLE);
                        ErrorPasswordRepeat.setText("Please fill in all the details");
                    }
                    else{

                        if(!passwordRepeat.equals(password)){
                            userPasswordRepeat.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                            ErrorPasswordRepeat.setVisibility(View.VISIBLE);
                            ErrorPasswordRepeat.setText("The two passwords do not match");
                        }
                        else{

                            if(!isEmailValid(emailget)){
                                userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                                ErrorEmail.setVisibility(View.VISIBLE);
                                ErrorEmail.setText("Please enter a valid email adress");
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

                                                        firebaseAuth.fetchSignInMethodsForEmail(emailget).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                                if(task.getResult().getSignInMethods().size() == 0){

                                                                    AllGood();

                                                                }
                                                                else{
                                                                    userEmail.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                                                                    ErrorEmail.setVisibility(View.VISIBLE);
                                                                    ErrorEmail.setText("This email has already been used to register an account");
                                                                }
                                                            }
                                                        });
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
            }
        }
    }

    private void AllGood() {

            String user_email = userEmail.getText().toString().trim();
            String user_password = userPassword.getText().toString().trim();

            protoname = userName.getText().toString().trim();
            String name = "@" + protoname;

            Intent intent = new Intent(RegistrationActivity.this, Profile_First_Setup.class);
            intent.putExtra("username", name);
            intent.putExtra("password", user_password);
            intent.putExtra("email", user_email);
            startActivity(intent);

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
