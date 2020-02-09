package com.example.myfirstapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapp.Chatroom.ChatPrivateWithUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAboutTab extends Fragment {

    private TextView AccountCreated;
    private TextView tvToMySaved;
    private ImageView btnToMySaved;

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

        AccountCreatedVoid();
        ToMySaved();

        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference LoadCounters = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        LoadCounters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child("Counters").hasChild("AccountVisits")){
                        TextView AccountVisits = getView().findViewById(R.id.tvAccountVisitCount);
                        String VisitCountString = dataSnapshot.child("Counters").child("AccountVisits").getValue().toString();
                        AccountVisits.setText("Account visits: " + VisitCountString);
                    }

                    if(dataSnapshot.hasChild("followers")){
                        TextView Followers = getView().findViewById(R.id.tvFollowersCount);
                        int FollowersCountString = (int) dataSnapshot.child("followers").getChildrenCount();
                        Followers.setText("Followers: " + FollowersCountString);
                    }

                    if(dataSnapshot.child("Counters").hasChild("PostCount")){
                        TextView PostCount = getView().findViewById(R.id.tvPostCount);
                        String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                        PostCount.setText("Posts: " + PostCountString);
                    }

                    if (dataSnapshot.hasChild("MyComments")){
                        TextView Followers = getView().findViewById(R.id.tvCommentCount);
                        int CommentCountString = (int) dataSnapshot.child("MyComments").getChildrenCount();
                        Followers.setText("Comments: " + CommentCountString);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ToMySaved() {

        tvToMySaved = getView().findViewById(R.id.tvToMySavedAccountViewing);
        btnToMySaved = getView().findViewById(R.id.ivToMySavedAccountViewing);

        btnToMySaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySaved_Activity.class);
                startActivity(intent);
            }
        });

        tvToMySaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySaved_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void AccountCreatedVoid() {

        AccountCreated = getView().findViewById(R.id.tvAccountCreatedOnDate2);
        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference GetCreated = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("userBirthdate");
        GetCreated.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Date = dataSnapshot.getValue().toString();
                AccountCreated.setText(Date);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
