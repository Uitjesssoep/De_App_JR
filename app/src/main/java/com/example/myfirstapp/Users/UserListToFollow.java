package com.example.myfirstapp.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserListToFollow extends AppCompatActivity {

    private Button Follow;
    private TextView Username;
    private ImageView ProfilePicture;
    private DatabaseReference databaseReferenceUIDlist, databaseReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabaseUIDlist, firebaseDatabase;
    private ProgressBar ProgressCircle;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private RecyclerView.LayoutManager layoutManager;
    private List<Users> list;
    private List<String> UIDlist;
    private UserAdapter adapter;
    private String UIDlistString, test, UIDlistString2;

    private void SetupUI() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        ProgressCircle = findViewById(R.id.progress_circle3);
        recyclerView = findViewById(R.id.recycler_viewUserList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReferenceUIDlist = firebaseDatabase.getReference("users");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        UIDlist = new ArrayList<>();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_to_follow);
        SetupUI();;
        databaseReference = firebaseDatabase.getReference("users");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Users users = postSnapshot.getValue(Users.class);
                        list.add(users);
                        Log.d("list", list.toString());

                    }

                    adapter = new UserAdapter(UserListToFollow.this, list);
                    recyclerView.setAdapter(adapter);
                    ProgressCircle.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserListToFollow.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    ProgressCircle.setVisibility(View.INVISIBLE);
                }
            });

    }}