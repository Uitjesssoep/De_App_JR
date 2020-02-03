package com.example.myfirstapp.Textposts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
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
    private CheckBox Anon, Followers;
    private Button Post;
    private String TitleContent, TextContent, usernameString;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference GeneralTextPosts, PersonalTextPosts;
    private String MyUID, temp_key, Date;
    private TextView user_name_gebruiker;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;


    private void SetupUI() {

        Title = findViewById(R.id.etTitleTextPost);
        Content = findViewById(R.id.etContentTextPost);
        Anon = findViewById(R.id.cbPostAnonText);
        Followers = findViewById(R.id.cbOnlyFollowersText);
        Post = findViewById(R.id.btnPostTextPost);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUID = user.getUid();

        GeneralTextPosts = FirebaseDatabase.getInstance().getReference("General_Posts");
        PersonalTextPosts = FirebaseDatabase.getInstance().getReference("Personal_Text_Posts").child(MyUID);

        user_name_gebruiker = findViewById(R.id.tvUserNameHiddenDing);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__text_post);

        SetupUI();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TitleContent = Title.getText().toString().trim();
                TextContent = Content.getText().toString().trim();

                if(TitleContent.isEmpty() || TextContent.isEmpty()){
                    Toast.makeText(Upload_TextPost_Activity.this, "Please fill in a title and the content", Toast.LENGTH_SHORT).show();
                }

                else{

                    if(Followers.isChecked() && Anon.isChecked()){
                        BothChecked();
                    }

                    if((Followers.isChecked()==false) && (Anon.isChecked()==false)){
                        BothNotChecked();
                    }

                    if(Followers.isChecked() && (Anon.isChecked()==false)){
                        VCheckedDNotChecked();
                    }

                    if((Followers.isChecked()==false) && Anon.isChecked()){
                        VNotCheckedDChecked();
                    }

                }

            }
        });

    }

    private void BothChecked() {

                String anonString = "[anonymous]";

                temp_key = GeneralTextPosts.push().getKey();
                StuffForPost StuffForPost = new StuffForPost(TitleContent, anonString, TextContent, MyUID, temp_key, Date, "Text");
                GeneralTextPosts.child(temp_key).setValue(StuffForPost);

                Intent VNoD = new Intent(Upload_TextPost_Activity.this, SecondActivity.class);
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
                user_name_gebruiker.setText(userProfile.getUserName());

                usernameString = user_name_gebruiker.getText().toString();

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

                Intent VNoD = new Intent(Upload_TextPost_Activity.this, SecondActivity.class);
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

        Intent VNoD = new Intent(Upload_TextPost_Activity.this, SecondActivity.class);
        startActivity(VNoD);
        finish();

    }

}
