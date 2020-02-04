package com.example.myfirstapp.Chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import android.widget.Toast;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
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
    private ImageButton Exit;
    private String Date, MyUID, TitleContent;
    private Calendar calendar;
    private CheckBox Anon;
    private SimpleDateFormat dateFormat;
    private DatabaseReference ChatroomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms__post_);

        SetupUI();
        SetupDesign();
    }

    private void SetupUI() {
        Title = findViewById(R.id.etTitleChatroom);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());
        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        ChatroomDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms");
        Anon = findViewById(R.id.cbPostAnonChatroom);
    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:
        Window window = Chatrooms_Post_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Chatrooms_Post_Activity.this, R.color.slighly_darker_mainGreen));

        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar_makechatroom);
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
                PostChatroom();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void PostChatroom() {

        TitleContent = Title.getText().toString().trim();

        if(TitleContent.isEmpty()){
            Toast.makeText(Chatrooms_Post_Activity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }

        else{
            if(Anon.isChecked()){
                AnonChecked();
            }

            if((Anon.isChecked()==false)){
                AnonNotChecked();
            }
        }
    }

    private void AnonNotChecked() {

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

                Intent intent = new Intent(Chatrooms_Post_Activity.this, Layout_Manager_BottomNav_Activity.class);
                intent.putExtra("Type", "Chat");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void AnonChecked() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfileToDatabase = dataSnapshot.getValue(UserProfileToDatabase.class);

                String User_name = "[anonymous]";
                String temp_key = ChatroomDatabase.push().getKey();
                String TitleDatabase = Title.getText().toString().trim();

                PostStuffForChat postStuffForChat = new PostStuffForChat(TitleDatabase, User_name, MyUID, temp_key, Date);
                ChatroomDatabase.child(temp_key).setValue(postStuffForChat);

                Intent intent = new Intent(Chatrooms_Post_Activity.this, Layout_Manager_BottomNav_Activity.class);
                intent.putExtra("Type", "Chat");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
