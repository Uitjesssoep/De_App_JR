package com.example.myfirstapp.AccountActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.example.myfirstapp.SharedPrefNightMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

        private EditText Name;
        private EditText Password;
        private Button Login;
        private TextView RegisterText, AccountYetText;
        private TextView PasswordResetText, IncorrectDetail1, IncorrectDetail2;

        private FirebaseAuth firebaseAuth;

        SharedPrefNightMode sharedPrefNightMode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else {
            setTheme(R.style.AppTheme);
            setLightStatusBar(MainActivity.this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //voor het geven van kleur aan de status bar:


        NotFirstTime(); // was eerst van belang voor iets, maar dat heb ik aangepast en ik was te lui om t weer terug te verplaatsen naar 'oncreate' dus ik heb t in 'notfirsttime' gelaten

    }

    private void NotFirstTime(){

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        PasswordResetText = (TextView)findViewById(R.id.tvForgotPassword);
        RegisterText = (TextView)findViewById(R.id.tvRegister);
        AccountYetText = findViewById(R.id.tvDontHaveAccountMain);
        IncorrectDetail1 = findViewById(R.id.tvIncorrectDetail1Main);
        IncorrectDetail2 = findViewById(R.id.tvIncorrectDetail2Main);

        //underline sign up tekst

        RegisterText.setPaintFlags(RegisterText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        IncorrectDetail1.setVisibility(View.INVISIBLE);
        IncorrectDetail2.setVisibility(View.INVISIBLE);


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
                IncorrectDetail1.setVisibility(View.INVISIBLE);
                IncorrectDetail2.setVisibility(View.INVISIBLE);
                Name.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                Password.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
            }
        });

        AccountYetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                IncorrectDetail1.setVisibility(View.INVISIBLE);
                IncorrectDetail2.setVisibility(View.INVISIBLE);
                Name.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                Password.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
            }
        });

        PasswordResetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Forgot_Password_Activity.class));
                IncorrectDetail1.setVisibility(View.INVISIBLE);
                IncorrectDetail2.setVisibility(View.INVISIBLE);
                Name.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                Password.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
            }
        });

    }


    private void validate(String userName, String userPassword) {


        String username = Name.getText().toString();
        String password = Password.getText().toString();


        if(username.isEmpty() || password.isEmpty()){
            Name.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            IncorrectDetail1.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            IncorrectDetail2.setVisibility(View.VISIBLE);
        }

        else{

            firebaseAuth.signInWithEmailAndPassword(userName.trim(), userPassword.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkEmailVerification();
                    }
                    else{
                        Name.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                        IncorrectDetail1.setVisibility(View.VISIBLE);
                        Password.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                        IncorrectDetail2.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verificationemail = firebaseUser.isEmailVerified();

        if(verificationemail){
            Intent intent = new Intent(MainActivity.this, Layout_Manager_BottomNav_Activity.class);
            intent.putExtra("Type", "StartUp");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        else{
            Toast.makeText(MainActivity.this, "Please verify your email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
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
