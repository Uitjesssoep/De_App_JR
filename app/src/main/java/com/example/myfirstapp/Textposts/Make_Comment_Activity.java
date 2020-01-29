package com.example.myfirstapp.Textposts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Make_Comment_Activity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference DatabaseCommentStuff;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String Date;
    private String CommentMessage, temp_key, Type;

    private EditText CommentSubstance;
    private ImageView Image;
    private ImageButton Exit;
    private TextView Title, Content, ShowMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_comment_post_);

        SetupUI();

        SetupDesign();

    }

    private void PostComment() {

        final String key = getIntent().getExtras().get("Key").toString();

        DatabaseCommentStuff = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Comments");

        CommentMessage = CommentSubstance.getText().toString().trim();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());

        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (CommentMessage.isEmpty()) {
            Toast.makeText(Make_Comment_Activity.this, "Can't post an empty comment", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference GetUserName = firebaseDatabase.getReference("users").child(MyUID);
            GetUserName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);

                    String userName = userProfile.getUserName();

                    temp_key = DatabaseCommentStuff.push().getKey();
                    CommentStuffForTextPost commentStuffForTextPost = new CommentStuffForTextPost(CommentMessage, Date, userName, temp_key, MyUID, key);
                    DatabaseCommentStuff.child(temp_key).setValue(commentStuffForTextPost);

                    Intent intent = new Intent(Make_Comment_Activity.this, Text_Post_Viewing_Activity.class);
                    intent.putExtra("Key", key);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = Make_Comment_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Make_Comment_Activity.this, R.color.slighly_darker_mainGreen));

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

                PostComment();

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void SetupUI() {

        CommentSubstance = findViewById(R.id.etAddACommentToPostTextPost);
        Title = findViewById(R.id.tvTitleOfTextPostForComment);
        Image = findViewById(R.id.ivContentImagePostForContent);
        Content = findViewById(R.id.tvContentPostForComment);
        ShowMore = findViewById(R.id.tvShowContentPostForComment);
        Image.setVisibility(View.GONE);
        Content.setVisibility(View.GONE);

        final String PostKey = getIntent().getExtras().get("Key").toString();
        DatabaseReference getTitle = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey);
        getTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String TitlePost = dataSnapshot.child("title").getValue().toString();
                Title.setText(TitlePost);
                Type = dataSnapshot.child("type").getValue().toString();

                String ContentPost = dataSnapshot.child("content").getValue().toString();
                if (dataSnapshot.child("type").getValue().equals("Image")) {
                    Picasso.get().load(ContentPost).fit().centerCrop().into(Image);
                } else {
                    Content.setText(ContentPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String WhatText = ShowMore.getText().toString();
                if (WhatText.equals("Show more")) {
                    ShowMore.setText("Show less");
                    if (Type.equals("Image")) {
                        Image.setVisibility(View.VISIBLE);
                        Content.setVisibility(View.GONE);
                    } else {
                        Image.setVisibility(View.GONE);
                        Content.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowMore.setText("Show more");
                    Image.setVisibility(View.GONE);
                    Content.setVisibility(View.GONE);

                }

            }
        });

    }
}