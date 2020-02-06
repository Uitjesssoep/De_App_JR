package com.example.myfirstapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.Chatroom.Chatrooms_Post_Activity;
import com.example.myfirstapp.Imageposts.Upload_Images_Activity;
import com.example.myfirstapp.Textposts.Upload_TextPost_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MakeFragment extends Fragment {

    private FloatingActionButton ImageFAB, TextFAB, ChatFAB;

    public MakeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageFAB = getView().findViewById(R.id.fabImagePostMake);
        TextFAB = getView().findViewById(R.id.fabTextPostMake);
        ChatFAB = getView().findViewById(R.id.fabChatRoomMake);

        ImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Upload_Images_Activity.class);
                startActivity(intent);
            }
        });

        TextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Upload_TextPost_Activity.class);
                startActivity(intent);
            }
        });

        ChatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Chatrooms_Post_Activity.class);
                startActivity(intent);
            }
        });
    }
}
