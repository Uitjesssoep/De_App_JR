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


public class HisAboutTab extends Fragment {


    public HisAboutTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_his_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final String HisUID = "???";

        final DatabaseReference LoadCounters = FirebaseDatabase.getInstance().getReference("users").child(HisUID);
        LoadCounters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Counters").hasChild("AccountVisits")){
                    TextView AccountVisits = getView().findViewById(R.id.tvHisAccountVisitCount);
                    String VisitCountString = dataSnapshot.child("Counters").child("AccountVisits").getValue().toString();
                    AccountVisits.setText("Account visits: " + VisitCountString);
                }

                if(dataSnapshot.child("Counters").hasChild("UpvoteCount")){
                    TextView UpvoteCount = getView().findViewById(R.id.tvHisUpvoteCount);
                    String UpvoteCountString = dataSnapshot.child("Counters").child("UpvoteCount").getValue().toString();
                    UpvoteCount.setText("Upvotes: " + UpvoteCountString);
                }

                if(dataSnapshot.child("Counters").hasChild("DownvoteCount")){
                    TextView DownvoteCount = getView().findViewById(R.id.tvHisDownvoteCount);
                    String DownvoteCountString = dataSnapshot.child("Counters").child("DownvoteCount").getValue().toString();
                    DownvoteCount.setText("Downvotes: " + DownvoteCountString);
                }

                if(dataSnapshot.hasChild("followers")){
                    TextView Followers = getView().findViewById(R.id.tvHisFollowersCount);
                    int FollowersCountString = (int) dataSnapshot.child("followers").getChildrenCount();
                    Followers.setText("Followers: " + FollowersCountString);
                }

                if(dataSnapshot.child("Counters").hasChild("PostCount")){
                    TextView PostCount = getView().findViewById(R.id.tvHisPostCount);
                    String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                    PostCount.setText("Posts: " + PostCountString);
                }

                if (dataSnapshot.hasChild("MyComments")){
                    TextView Followers = getView().findViewById(R.id.tvHisCommentCount);
                    int CommentCountString = (int) dataSnapshot.child("MyComments").getChildrenCount();
                    Followers.setText("Comments: " + CommentCountString);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
