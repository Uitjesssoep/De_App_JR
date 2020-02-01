package com.example.myfirstapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAboutTab extends Fragment {


    public MyAboutTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference LoadCounters = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        LoadCounters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Counters")){

                    if(dataSnapshot.child("Counters").hasChild("AccountVisits")){
                        TextView AccountVisits = getView().findViewById(R.id.tvAccountVisitCount);
                        String VisitCountString = dataSnapshot.child("Counters").child("AccountVisits").getValue().toString();
                        AccountVisits.setText("Account visits: " + VisitCountString);
                    }

                    if(dataSnapshot.child("Counters").hasChild("UpvoteCount")){
                        TextView UpvoteCount = getView().findViewById(R.id.tvUpvoteCount);
                        String UpvoteCountString = dataSnapshot.child("Counters").child("UpvoteCount").getValue().toString();
                        UpvoteCount.setText("Upvotes: " + UpvoteCountString);
                    }

                    if(dataSnapshot.child("Counters").hasChild("DownvoteCount")){
                        TextView DownvoteCount = getView().findViewById(R.id.tvDownvoteCount);
                        String DownvoteCountString = dataSnapshot.child("Counters").child("DownvoteCount").getValue().toString();
                        DownvoteCount.setText("Downvotes: " + DownvoteCountString);
                    }

                    if(dataSnapshot.hasChild("followers")){
                        TextView Followers = getView().findViewById(R.id.tvFollowersCount);
                        String FollowersCountString = "...";
                        Followers.setText("Followers: " + FollowersCountString);
                    }

                    if(dataSnapshot.child("Counters").hasChild("PostCount")){
                        TextView PostCount = getView().findViewById(R.id.tvPostCount);
                        String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                        PostCount.setText("Posts: " + PostCountString);
                    }

                    if (dataSnapshot.hasChild("MyComments")){
                        TextView Followers = getView().findViewById(R.id.tvCommentCount);
                        String CommentCountString = "...";
                        Followers.setText("Comments: " + CommentCountString);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
