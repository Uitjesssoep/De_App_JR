package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Change_Password_Activity;
import com.example.myfirstapp.AccountActivities.Deleting_Account_Activity;
import com.example.myfirstapp.AccountActivities.MainActivity;
import com.example.myfirstapp.AccountActivities.Profile_Settings_Activity;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App_Settings_Activity extends AppCompatActivity {

    private ImageButton Exit;
    private TextView ChangeDisplay, ChangePassword, LogOut, Delete, DataPolicy, ContentPolicy, Credits, SendEmail, ReportBug;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__settings_);

        SetupDesign();

        KnoppenEnzo();

    }

    private void KnoppenEnzo() {

        ChangeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App_Settings_Activity.this, Profile_Settings_Activity.class);
                startActivity(intent);
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App_Settings_Activity.this, Change_Password_Activity.class);
                startActivity(intent);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(App_Settings_Activity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Before logging out, make sure you remember your login details! Are you sure you want to log out?");

                dialog.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(App_Settings_Activity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        firebaseAuth.signOut();
                        startActivity(intent);

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });
        //h

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(App_Settings_Activity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting your account will permanently delete your account and all of your data from the server. \n \n" +
                        "All your account activity will remain (posts, comments, etc.) under the username: [deleted_user]. \n \n" +
                        "Other users will not be able to visit your profile anymore. \n \nOne last confirmation will follow before your account is permanently deleted!");
                dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(App_Settings_Activity.this);
                        dialog.setTitle("Deleting your account");
                        dialog.setMessage("Are you sure you want to delete your account? This action cannot be undone!");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(App_Settings_Activity.this, Deleting_Account_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });

                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                            }
                        });

                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });

        DataPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ContentPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ReportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = App_Settings_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(App_Settings_Activity.this, R.color.slighly_darker_mainGreen));

        //action bar ding

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar_settingsyeah);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exitsettings);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ChangeDisplay = findViewById(R.id.tvChangeInfoSettingsAccount);
        ChangePassword = findViewById(R.id.tvChangePasswordOptionSettingsAccount);
        LogOut = findViewById(R.id.tvLogOutOfAccountSettingsAccount);
        Delete = findViewById(R.id.tvDeleteAccountOptionSettingsAccount);
        DataPolicy = findViewById(R.id.tvDataPolicySettingsAbout);
        ContentPolicy = findViewById(R.id.tvContentPolicySettingsAbout);
        Credits = findViewById(R.id.tvCreditsSettingsAbout);
        SendEmail = findViewById(R.id.tvSendAMailSupportSettings);
        ReportBug = findViewById(R.id.tvReportABugSupportSettings);

    }
}
