package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Set;

public class General_Feed_Activity extends AppCompatActivity {


    private ListView GeneralFeed;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_posts = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference posts = FirebaseDatabase.getInstance().getReference("General_Text_Posts");


    private void SetupUI() {

    //    GeneralFeed = findViewById(R.id.lvGeneralFeed);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_posts);
        GeneralFeed.setAdapter(arrayAdapter);

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

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_posts.clear();
                list_of_posts.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        GeneralFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), Text_Post_Viewing_Activity.class);
                intent.putExtra("Key", ((TextView)view).getText().toString() );
                startActivity(intent);

            }
        });


    }


}
