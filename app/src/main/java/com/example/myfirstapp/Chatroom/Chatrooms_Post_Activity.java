package com.example.myfirstapp.Chatroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;
import com.google.android.gms.tasks.OnSuccessListener;
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

    SharedPrefNightMode sharedPrefNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

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
                final ProgressDialog dialog = new ProgressDialog(Chatrooms_Post_Activity.this);
                dialog.setTitle("Uploading chatroom");
                dialog.setMessage("Please wait");
                dialog.show();
                hideKeyboard(Chatrooms_Post_Activity.this);
                AnonChecked();
            }

            if((Anon.isChecked()==false)){
                final ProgressDialog dialog = new ProgressDialog(Chatrooms_Post_Activity.this);
                dialog.setTitle("Uploading chatroom");
                dialog.setMessage("Please wait");
                dialog.show();
                hideKeyboard(Chatrooms_Post_Activity.this);
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
                final String temp_key = ChatroomDatabase.push().getKey();
                String TitleDatabase = Title.getText().toString().trim();

                PostStuffForChat postStuffForChat = new PostStuffForChat(TitleDatabase, User_name, MyUID, temp_key, System.currentTimeMillis());
                ChatroomDatabase.child(temp_key).setValue(postStuffForChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Chatrooms_Post_Activity.this, Layout_Manager_BottomNav_Activity.class);
                        intent.putExtra("Type", "ChatMake");
                        intent.putExtra("Key", temp_key);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
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
                final String temp_key = ChatroomDatabase.push().getKey();
                String TitleDatabase = Title.getText().toString().trim();

                PostStuffForChat postStuffForChat = new PostStuffForChat(TitleDatabase, User_name, MyUID, temp_key, System.currentTimeMillis());
                ChatroomDatabase.child(temp_key).setValue(postStuffForChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Chatrooms_Post_Activity.this, Layout_Manager_BottomNav_Activity.class);
                        intent.putExtra("Type", "ChatMake");
                        intent.putExtra("Key", temp_key);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
