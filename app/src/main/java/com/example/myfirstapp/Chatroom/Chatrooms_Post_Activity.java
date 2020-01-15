package com.example.myfirstapp.Chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Notifications.Data;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.example.myfirstapp.Textposts.Upload_TextPost_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Chatrooms_Post_Activity extends AppCompatActivity {

    private EditText Title;
    private Button Post;

    private String Date, MyUID;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private DatabaseReference ChatroomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms__post_);

        SetupUI();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TitleString = Title.getText().toString().trim();

                if(TitleString.isEmpty()){
                    Toast.makeText(Chatrooms_Post_Activity.this, "You must have a title", Toast.LENGTH_LONG);
                }
                else{
                    UploadChatroomToDatabase();
                }

            }
        });
    }

    private void UploadChatroomToDatabase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(MyUID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfileToDatabase userProfileToDatabase = dataSnapshot.getValue(UserProfileToDatabase.class);

                String User_name = userProfileToDatabase.getUserName().toString();
                String temp_key = ChatroomDatabase.push().getKey();
                String TitleDatabase = Title.getText().toString().trim();

                PostStuffForChat postStuffForChat = new PostStuffForChat(TitleDatabase, User_name, MyUID, temp_key, Date);
                ChatroomDatabase.child(temp_key).setValue(postStuffForChat);

                Intent intent = new Intent(Chatrooms_Post_Activity.this, SecondActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetupUI() {

        Title = findViewById(R.id.etTitleChatroom);
        Post = findViewById(R.id.btnPostChatRoom);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());

        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        ChatroomDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms");

    }
}
