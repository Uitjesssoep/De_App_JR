package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class General_Feed_Activity extends AppCompatActivity {


    private RecyclerView GeneralFeed;
    private List<PostStuffForText> postStuffForTextList;
    private PostStuffForTextAdapter postStuffForTextAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference posts = FirebaseDatabase.getInstance().getReference("General_Text_Posts");

    private String key;

    private void SetupUI() {

        GeneralFeed = findViewById(R.id.rvFeedScreen);
        GeneralFeed.setLayoutManager(new LinearLayoutManager(this));

        postStuffForTextList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general__feed);

        SetupUI();

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PostStuffForText postStuffForText = postSnapshot.getValue(PostStuffForText.class);
                    postStuffForTextList.add(postStuffForText);
                }

                postStuffForTextAdapter = new PostStuffForTextAdapter(General_Feed_Activity.this, postStuffForTextList);
                GeneralFeed.setAdapter(postStuffForTextAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
