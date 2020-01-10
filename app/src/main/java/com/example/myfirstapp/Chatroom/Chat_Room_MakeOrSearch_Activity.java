package com.example.myfirstapp.Chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Chat_Room_MakeOrSearch_Activity extends AppCompatActivity {

    private RecyclerView RoomList;
    private ArrayAdapter<PostStuffForChat> postStuffForChatList;
    private PostStuffForChatAdapter postStuffForChatAdapter;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Chatrooms");

    private String MyUID, key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room__make_or_search_);

        SetupUI();

    }

    private void SetupUI() {

        RoomList = findViewById(R.id.rvChatroomFeed);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RoomList.setLayoutManager(new LinearLayoutManager(this));

        //postStuffForChatList = new ArrayList<>();

    }
}
