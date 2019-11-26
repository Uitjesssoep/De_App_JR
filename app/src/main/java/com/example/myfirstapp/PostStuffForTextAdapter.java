package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostStuffForTextAdapter extends RecyclerView.Adapter<PostStuffForTextAdapter.ViewHolder>{

    public Context mContext;
    public List<PostStuffForText> mPost;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUserNameClick(int position);
        void onUpvoteClick(int position);
        void onDownvoteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private FirebaseUser firebaseUser;

    public PostStuffForTextAdapter(Context mContext, List<PostStuffForText> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForTextAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostStuffForText uploadCurrent = mPost.get(position);
        holder.Username.setText(uploadCurrent.getUser_name());
        holder.Title.setText(uploadCurrent.getTitle());
        holder.KeyHolder.setText(uploadCurrent.getKey());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, KeyHolder;
        public ImageButton Upvote, Downvote;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById (R.id.tvUsernameTextPostItem);
            LikeCount = itemView.findViewById (R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById (R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById (R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById (R.id.tvTitleTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            Username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUserNameClick(position);
                        }
                    }
                }
            });

            Upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUpvoteClick(position);
                        }
                    }
                }
            });

            Downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDownvoteClick(position);
                        }
                    }
                }
            });

        }
    }
}
