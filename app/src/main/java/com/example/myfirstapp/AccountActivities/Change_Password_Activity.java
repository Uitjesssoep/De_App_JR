package com.example.myfirstapp.AccountActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Change_Password_Activity extends AppCompatActivity {


    private EditText NewPasswordPasswordChange;
    private FirebaseUser firebaseUser;
    private ImageButton Exit;
    private TextInputLayout NewPWLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        NewPasswordPasswordChange = (EditText)findViewById(R.id.etNewChangePassword);
        NewPWLayout = findViewById(R.id.inputlayoutPasswordUpdate);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SetupDesign();

    }

    private void SetupDesign() {

            //voor het geven van kleur aan de status bar:

            Window window = Change_Password_Activity.this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(Change_Password_Activity.this, R.color.slighly_darker_mainGreen));

            //action bar ding

            Toolbar toolbar = findViewById(R.id.action_bar_changepassword);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            Exit = (ImageButton) toolbar.findViewById(R.id.exitpasswordchange);
            Exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_savesettings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){


            case R.id.action_save_settings:

                ClickSave();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void ClickSave() {

        NewPWLayout.setError(null);

        NewPasswordPasswordChange.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        String NewPasswordString = NewPasswordPasswordChange.getText().toString().trim();
        int NewPasswordLength = NewPasswordString.length();


        if(NewPasswordString.isEmpty()){

            NewPasswordPasswordChange.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            NewPWLayout.setError("This field cannot be empty");

        }
        else {

            if(NewPasswordLength < 6){
                NewPasswordPasswordChange.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                NewPWLayout.setError("Make sure your password is at least 6 characters long");
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

    }
}
