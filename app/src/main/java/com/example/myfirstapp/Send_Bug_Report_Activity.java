package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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

    SharedPrefNightMode sharedPrefNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__bug__report_);

        SetupUI();
        SetupDesign();
    }

    private void SendReport() {

        if(HowOftenString.isEmpty()){
            HowOftenString = "User did not answer this question";
        }

        final DatabaseReference Report = FirebaseDatabase.getInstance().getReference("BugReports");
        final String temp_key = Report.push().getKey();

        if(Username.isChecked()){
            if(AllowContact.isChecked()){

                DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
                GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String UserName = dataSnapshot.getValue().toString();

                        Report.child(temp_key).child("Where").setValue(WhereString);
                        Report.child(temp_key).child("HowOften_Recreate").setValue(HowOftenString);
                        Report.child(temp_key).child("Description").setValue(DecribeString);
                        Report.child(temp_key).child("Username").setValue(UserName);
                        Report.child(temp_key).child("Allow_Contact").setValue("yes");

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Send_Bug_Report_Activity.this);
                        dialog.setTitle("Success!");
                        dialog.setMessage("Thank you for your feedback! We will try to fix any problem as soon as possible.");
                        dialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

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

                        Report.child(temp_key).child("Where").setValue(WhereString);
                        Report.child(temp_key).child("HowOften_Recreate").setValue(HowOftenString);
                        Report.child(temp_key).child("Description").setValue(DecribeString);
                        Report.child(temp_key).child("Username").setValue(UserName);
                        Report.child(temp_key).child("Allow_Contact").setValue("no");

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Send_Bug_Report_Activity.this);
                        dialog.setTitle("Success!");
                        dialog.setMessage("Thank you for your feedback! We will try to fix any problem as soon as possible.");
                        dialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
        else{
            if(AllowContact.isChecked()){

                Report.child(temp_key).child("Where").setValue(WhereString);
                Report.child(temp_key).child("HowOften_Recreate").setValue(HowOftenString);
                Report.child(temp_key).child("Description").setValue(DecribeString);
                Report.child(temp_key).child("Username").setValue("not given");
                Report.child(temp_key).child("Allow_Contact").setValue("yes");

                AlertDialog.Builder dialog = new AlertDialog.Builder(Send_Bug_Report_Activity.this);
                dialog.setTitle("Success!");
                dialog.setMessage("Thank you for your feedback! We will try to fix any problem as soon as possible.");
                dialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
            else{

                Report.child(temp_key).child("Where").setValue(WhereString);
                Report.child(temp_key).child("HowOften_Recreate").setValue(HowOftenString);
                Report.child(temp_key).child("Description").setValue(DecribeString);
                Report.child(temp_key).child("Username").setValue("not given");
                Report.child(temp_key).child("Allow_Contact").setValue("no");

                AlertDialog.Builder dialog = new AlertDialog.Builder(Send_Bug_Report_Activity.this);
                dialog.setTitle("Success!");
                dialog.setMessage("Thank you for your feedback! We will try to fix any problem as soon as possible.");
                dialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

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
