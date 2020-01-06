package com.example.myfirstapp;

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
import com.google.firebase.auth.FirebaseUser;

public class Change_Password_Activity extends AppCompatActivity {


    private EditText NewPasswordPasswordChange;
    private Button SaveNewPassword;
    private FirebaseUser firebaseUser;
    private TextView PasswordError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        PasswordError.setVisibility(View.INVISIBLE);

        NewPasswordPasswordChange = (EditText)findViewById(R.id.etNewPasswordPasswordChange);
        SaveNewPassword = (Button)findViewById(R.id.btnSaveNewPassword);
        PasswordError = findViewById(R.id.tvPasswordErrorForgot);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        SaveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewPasswordPasswordChange.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
                PasswordError.setVisibility(View.INVISIBLE);

                String NewPasswordString = NewPasswordPasswordChange.getText().toString().trim();
                int NewPasswordLength = NewPasswordString.length();


                if(NewPasswordLength < 6){
                    PasswordError.setVisibility(View.VISIBLE);
                    NewPasswordPasswordChange.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    PasswordError.setText("Make sure your password is at least 6 characters long");
                }

                else{

                    firebaseUser.updatePassword(NewPasswordString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Change_Password_Activity.this, "Password succesfully updated", Toast.LENGTH_SHORT);
                                finish();
                            }

                            else{
                                Toast.makeText(Change_Password_Activity.this, "Couldn't update password, please try again later", Toast.LENGTH_SHORT);
                            }

                        }
                    });
                }

            }
        });

    }
}
