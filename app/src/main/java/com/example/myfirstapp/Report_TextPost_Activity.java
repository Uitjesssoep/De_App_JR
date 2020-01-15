package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report_TextPost_Activity extends AppCompatActivity {

    private TextView ReportThisPost;
    private EditText ReportReason;
    private Button SendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__text_post_);

        SetupUI();

    }

    private void SetupUI() {

        ReportThisPost = findViewById(R.id.tvReportWhatTextPost);
        ReportReason = findViewById(R.id.etReasonReportTextPost);
        SendReport = findViewById(R.id.btnSendTextReport);

        String Title = getIntent().getExtras().get("Titel").toString();
        String Username = getIntent().getExtras().get("User").toString();

        ReportThisPost.setText("Reporting the following post:" + "'" + Title + "'" + "uploaded by" + Username);

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

        }
        else{

            DatabaseReference NoReason = FirebaseDatabase.getInstance().getReference("Reports").child(PostKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("UserWhoReported").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            NoReason.child("Reason").setValue(Reason);

        }

    }
}
