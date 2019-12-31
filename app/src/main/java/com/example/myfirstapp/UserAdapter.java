package com.example.myfirstapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter {

    private List list;
    public UserAdapter(List list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView Username;
        public ImageView ProfilePicture;
        public Button Follow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.tvUser_name);
            ProfilePicture = itemView.findViewById(R.id.ivProfilePictureUserList);
            Follow = itemView.findViewById(R.id.btFollow);

        }
    }

}