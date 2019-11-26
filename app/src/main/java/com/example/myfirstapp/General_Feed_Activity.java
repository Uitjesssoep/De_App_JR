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
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference posts = FirebaseDatabase.getInstance().getReference("General_Text_Posts");
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;

    private String key, MyUID;
    private Boolean Liked = false, Disliked = false, LikedCheck = false, DislikedCheck = false;
    private int LikeCount, DislikeCount, CommentCount;

    private void SetupUI() {

        GeneralFeed = findViewById(R.id.rvFeedScreen);
        GeneralFeed.setLayoutManager(new LinearLayoutManager(this));

        postStuffForTextList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

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


                postStuffForTextAdapter.setOnItemClickListener(new PostStuffForTextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        key = postStuffForTextList.get(position).getKey().toString();
                        Intent Test2 = new Intent(getApplicationContext(), Text_Post_Viewing_Activity.class);
                        Test2.putExtra("Key", key);
                        startActivity(Test2);
                        finish();
                    }

                    @Override
                    public void onUserNameClick(int position) {
                        key = postStuffForTextList.get(position).getKey().toString();
                        Intent GoToProfile = new Intent(General_Feed_Activity.this, Account_Info_OtherUser_Activity.class);
                        GoToProfile.putExtra("Key", key);
                        startActivity(GoToProfile);
                    }

                    @Override
                    public void onUpvoteClick(int position) {
                        key = postStuffForTextList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Dislikes");

                        if(DislikedCheck){
                            DatabaseDislike.child(MyUID).removeValue();
                            Liked = true;

                            DatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(Liked) {

                                        if (dataSnapshot.hasChild(MyUID)) {

                                            DatabaseLike.child(MyUID).removeValue();
                                            Liked = false;

                                        } else {

                                            DatabaseLike.child(MyUID).setValue("RandomLike");
                                            Liked = false;

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Liked = true;

                            DatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(Liked) {

                                        if (dataSnapshot.hasChild(MyUID)) {

                                            DatabaseLike.child(MyUID).removeValue();
                                            Liked = false;

                                        } else {

                                            DatabaseLike.child(MyUID).setValue("RandomLike");
                                            Liked = false;

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onDownvoteClick(int position) {
                        key = postStuffForTextList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Dislikes");

                        if(LikedCheck){
                            DatabaseLike.child(MyUID).removeValue();

                            Disliked = true;

                            DatabaseDislike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(Disliked) {

                                        if (dataSnapshot.hasChild(MyUID)) {

                                            DatabaseDislike.child(MyUID).removeValue();
                                            Disliked = false;

                                        } else {

                                            DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                            Disliked = false;

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Disliked = true;

                            DatabaseDislike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(Disliked) {

                                        if (dataSnapshot.hasChild(MyUID)) {

                                            DatabaseDislike.child(MyUID).removeValue();
                                            Disliked = false;

                                        } else {

                                            DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                            Disliked = false;

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
