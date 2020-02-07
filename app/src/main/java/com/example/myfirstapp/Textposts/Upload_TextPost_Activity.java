package com.example.myfirstapp.Textposts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Upload_TextPost_Activity extends AppCompatActivity {


    private EditText Title, Content;
    private CheckBox Anon;
    private String TitleContent, TextContent, usernameString;
    private ImageButton Exit;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference GeneralTextPosts, PersonalTextPosts;
    private String MyUID, temp_key, Date;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;


    private void SetupUI() {

        Title = findViewById(R.id.etTitleTextPost);
        Content = findViewById(R.id.etContentTextPost);
        Anon = findViewById(R.id.cbPostAnonText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUID = user.getUid();

        GeneralTextPosts = FirebaseDatabase.getInstance().getReference("General_Posts");
        PersonalTextPosts = FirebaseDatabase.getInstance().getReference("Personal_Text_Posts").child(MyUID);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__text_post);

        SetupUI();
        SetupDesign();
    }

    private void SetupDesign() {

            //voor het geven van kleur aan de status bar:
            Window window = Upload_TextPost_Activity.this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(Upload_TextPost_Activity.this, R.color.slighly_darker_mainGreen));

            //action bar ding

            Toolbar toolbar = findViewById(R.id.action_bar_maketextpost);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_makecomment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post_comment:
                PostTextPost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void PostTextPost() {

        TitleContent = Title.getText().toString().trim();
        TextContent = Content.getText().toString().trim();

        if(TitleContent.isEmpty()){
            Toast.makeText(Upload_TextPost_Activity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }

        else{
            if(Anon.isChecked()){
                BothChecked();
            }

            if((Anon.isChecked()==false)){
                BothNotChecked();
            }
        }
    }

    private void BothChecked() {

        String anonString = "[anonymous]";

        final DatabaseReference PostCounter = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        PostCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("PostCount")){

                    String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                    int PostCountInt = Integer.parseInt(PostCountString);
                    PostCountInt = Integer.valueOf(PostCountInt + 1);
                    String NewPostCountString = Integer.toString(PostCountInt);
                    PostCounter.child("Counters").child("PostCount").setValue(NewPostCountString);

                }

                else{
                    PostCounter.child("Counters").child("PostCount").setValue("1");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

                temp_key = GeneralTextPosts.push().getKey();
                StuffForPost StuffForPost = new StuffForPost(TitleContent, anonString, TextContent, MyUID, temp_key, Date, "Text");
                GeneralTextPosts.child(temp_key).setValue(StuffForPost);

                Intent VNoD = new Intent(Upload_TextPost_Activity.this, Layout_Manager_BottomNav_Activity.class);
                VNoD.putExtra("Key", temp_key);
                VNoD.putExtra("Type", "TextMake");
                VNoD.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(VNoD);
                finish();

    }

    private void BothNotChecked(){
        VCheckedDNotChecked();
    }

    private void VCheckedDNotChecked(){

        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(MyUID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                usernameString = userProfile.getUserName();

                final DatabaseReference PostCounter = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
                PostCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("PostCount")){

                            String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                            int PostCountInt = Integer.parseInt(PostCountString);
                            PostCountInt = Integer.valueOf(PostCountInt + 1);
                            String NewPostCountString = Integer.toString(PostCountInt);
                            PostCounter.child("Counters").child("PostCount").setValue(NewPostCountString);

                        }

                        else{
                            PostCounter.child("Counters").child("PostCount").setValue("1");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                temp_key = GeneralTextPosts.push().getKey();
                StuffForPost StuffForPost = new StuffForPost(TitleContent, usernameString, TextContent, MyUID, temp_key, Date, "Text");
                GeneralTextPosts.child(temp_key).setValue(StuffForPost);

                Intent VNoD = new Intent(Upload_TextPost_Activity.this, Layout_Manager_BottomNav_Activity.class);
                VNoD.putExtra("Key", temp_key);
                VNoD.putExtra("Type", "TextMake");
                VNoD.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(VNoD);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Upload_TextPost_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void VNotCheckedDChecked(){

        String anonString = "[anonymous]";

        final DatabaseReference PostCounter = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        PostCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("PostCount")){

                    String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                    int PostCountInt = Integer.parseInt(PostCountString);
                    PostCountInt = Integer.valueOf(PostCountInt + 1);
                    String NewPostCountString = Integer.toString(PostCountInt);
                    PostCounter.child("Counters").child("PostCount").setValue(NewPostCountString);

                }

                else{
                    PostCounter.child("Counters").child("PostCount").setValue("1");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        temp_key = GeneralTextPosts.push().getKey();
        StuffForPost StuffForPost = new StuffForPost(TitleContent, anonString, TextContent, MyUID, temp_key, Date, "Text");
        GeneralTextPosts.child(temp_key).setValue(StuffForPost);

        Intent VNoD = new Intent(Upload_TextPost_Activity.this, Layout_Manager_BottomNav_Activity.class);
        VNoD.putExtra("Key", temp_key);
        VNoD.putExtra("Type", "TextMake");
        VNoD.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(VNoD);
        finish();

    }

}
