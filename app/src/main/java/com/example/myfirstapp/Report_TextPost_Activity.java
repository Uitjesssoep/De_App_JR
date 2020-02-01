package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report_TextPost_Activity extends AppCompatActivity {

    private TextView ReportThisPost, OnlyReportOnce, ReportTitle;
    private EditText ReportReason;
    private Button SendReport;
    private ImageButton Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__text_post_);

        SetupUI();

        SetupDesign();

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = Report_TextPost_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Report_TextPost_Activity.this, R.color.slighly_darker_mainGreen));

        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar_comment);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exitmakecommenttextpost);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void SetupUI() {

        ReportThisPost = findViewById(R.id.tvReportWhatTextPost);
        ReportReason = findViewById(R.id.etReasonReportTextPost);
        SendReport = findViewById(R.id.btnSendTextReport);
        OnlyReportOnce = findViewById(R.id.tvOnlyReportOneText);
        ReportTitle = findViewById(R.id.tvReportTextPost);

        String Title = getIntent().getExtras().get("Titel").toString();
        String Username = getIntent().getExtras().get("User").toString();
        String Soort = getIntent().getExtras().get("Soort").toString();

        ReportThisPost.setText("Reporting a " + Soort + " uploaded by " + Username);
        ReportTitle.setText("Report " + Soort);
        OnlyReportOnce.setText("You can only report a " + Soort + " once! If you have already reported this " + Soort + ", you will override your previous report when you report it again!");

        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendTheReport();

            }
        });

    }

    private void SendTheReport() {

        String Reason = ReportReason.getText().toString().trim();
        String PostKey = getIntent().getExtras().get("Key").toString();

        if(Reason.isEmpty()){

            DatabaseReference NoReason = FirebaseDatabase.getInstance().getReference("Reports").child(PostKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("UserWhoReported").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("Reason").setValue("No reason given");

            Intent intent = new Intent(Report_TextPost_Activity.this, Layout_Manager_BottomNav_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else{

            DatabaseReference NoReason = FirebaseDatabase.getInstance().getReference("Reports").child(PostKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("UserWhoReported").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("Reason").setValue(Reason);

            Intent intent2 = new Intent(Report_TextPost_Activity.this, Layout_Manager_BottomNav_Activity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent2);

        }

    }
}
