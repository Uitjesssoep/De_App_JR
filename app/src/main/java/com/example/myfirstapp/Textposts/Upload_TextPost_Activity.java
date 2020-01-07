package com.example.myfirstapp.Textposts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
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
    private CheckBox DisableComments, VisibleForEveryone;
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
        DisableComments = findViewById(R.id.cbDisableComments);
        VisibleForEveryone = findViewById(R.id.cbVisibleForEveryone);
        Post = findViewById(R.id.btnPostTextPost);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUID = user.getUid();

        GeneralTextPosts = FirebaseDatabase.getInstance().getReference("General_Text_Posts");
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

                TitleContent = Title.getText().toString();
                TextContent = Content.getText().toString();

                if(TitleContent.isEmpty() || TextContent.isEmpty()){
                    Toast.makeText(Upload_TextPost_Activity.this, "Please fill in a title and the content", Toast.LENGTH_SHORT).show();
                }

                else{

                    if(VisibleForEveryone.isChecked() && DisableComments.isChecked()){
                        BothChecked();
                    }

                    if((VisibleForEveryone.isChecked()==false) && (DisableComments.isChecked()==false)){
                        BothNotChecked();
                    }

                    if(VisibleForEveryone.isChecked() && (DisableComments.isChecked()==false)){
                        VCheckedDNotChecked();
                    }

                    if((VisibleForEveryone.isChecked()==false) && DisableComments.isChecked()){
                        VNotCheckedDChecked();
                    }

                }

            }
        });

    }

    private void BothChecked() {
        VCheckedDNotChecked();
    }

    private void BothNotChecked(){
        VCheckedDNotChecked();
    }

    private void VCheckedDNotChecked(){

        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(MyUID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                user_name_gebruiker.setText(userProfile.getUserName());

                usernameString = user_name_gebruiker.getText().toString();


                temp_key = GeneralTextPosts.push().getKey();
                PostStuffForText postStuffForText = new PostStuffForText(TitleContent, usernameString, TextContent, MyUID, temp_key, Date);
                GeneralTextPosts.child(temp_key).setValue(postStuffForText);

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
        BothChecked();
    }

}
