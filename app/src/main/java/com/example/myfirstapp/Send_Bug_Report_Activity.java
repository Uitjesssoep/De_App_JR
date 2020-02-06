package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myfirstapp.Textposts.Make_Comment_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Send_Bug_Report_Activity extends AppCompatActivity {

    private ImageButton Exit;
    private EditText Where, HowOften, Describe;
    private CheckBox Username, AllowContact;
    private String WhereString, HowOftenString, DecribeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__bug__report_);

        SetupUI();
        SetupDesign();
    }

    private void SendReport() {

        if(HowOftenString.isEmpty()){
            HowOftenString = "User did not answer this question";
        }

        if(Username.isChecked()){
            if(AllowContact.isChecked()){

                DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
                GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UserName = dataSnapshot.getValue().toString();

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "jul.rob.app@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
                        intent.putExtra(Intent.EXTRA_TEXT, "Where:\n" + WhereString + "\n\n\n" + "More than once / recreate it?\n" + HowOftenString + "\n\n\n" + "Description:\n" + DecribeString + "\n\n\n" + "Username\n" + UserName + "\n\n\n" + "Allow contact?\n" + "yes");

                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else{

                DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
                GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UserName = dataSnapshot.getValue().toString();

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "jul.rob.app@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
                        intent.putExtra(Intent.EXTRA_TEXT, "Where:\n" + WhereString + "\n\n\n" + "More than once / recreate it?\n" + HowOftenString + "\n\n\n" + "Description:\n" + DecribeString + "\n\n\n" + "Username\n" + UserName + "\n\n\n" + "Allow contact?\n" + "no");

                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
        else{
            if(AllowContact.isChecked()){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "jul.rob.app@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
                intent.putExtra(Intent.EXTRA_TEXT, "Where:\n" + WhereString + "\n\n\n" + "More than once / recreate it?\n" + HowOftenString + "\n\n\n" + "Description:\n" + DecribeString + "\n\n\n" + "Username\n" + "Username not given" + "\n\n\n" + "Allow contact?\n" + "yes");

                startActivity(intent);

            }
            else{

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "jul.rob.app@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
                intent.putExtra(Intent.EXTRA_TEXT, "Where:\n" + WhereString + "\n\n\n" + "More than once / recreate it?\n" + HowOftenString + "\n\n\n" + "Description:\n" + DecribeString + "\n\n\n" + "Username\n" + "Username not given" + "\n\n\n" + "Allow contact?\n" + "no");

                startActivity(intent);

            }
        }
    }

    private void CheckIfAllIsGood() {
        Where.setBackgroundResource(R.drawable.edittext_roundedcorners_login);
        Describe.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        WhereString = Where.getText().toString();
        HowOftenString = HowOften.getText().toString();
        DecribeString = Describe.getText().toString();

        if(WhereString.isEmpty() && DecribeString.isEmpty()){
            Describe.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            Where.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
        }
        else{
            if(WhereString.isEmpty()){
                Where.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            }
            else{
                if(DecribeString.isEmpty()){
                    Describe.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                }
                else {
                    SendReport();
                }
            }
        }
    }

    private void SetupUI() {
        Where = findViewById(R.id.etWhatPageProblem);
        HowOften = findViewById(R.id.etAbleToRecreate);
        Describe = findViewById(R.id.etDescripeBug);
        Username = findViewById(R.id.cbSendUsernameBug);
        AllowContact = findViewById(R.id.cbAllowContactBug);
    }

    private void SetupDesign() {
        Window window = Send_Bug_Report_Activity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Send_Bug_Report_Activity.this, R.color.slighly_darker_mainGreen));

        Toolbar toolbar = findViewById(R.id.action_bar_bugreport);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = toolbar.findViewById(R.id.exitmakecommenttextpost);
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
        inflater.inflate(R.menu.menu_actionbar_sendbugreport, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_report:
                CheckIfAllIsGood();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
