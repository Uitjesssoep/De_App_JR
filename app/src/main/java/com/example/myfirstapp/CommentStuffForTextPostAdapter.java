package com.example.myfirstapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentStuffForTextPostAdapter extends RecyclerView.Adapter<CommentStuffForTextPostAdapter.ViewHolder>{

    public Context mContext;
    public List<CommentStuffForTextPost> mComment;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onUserNameClick(int position);
        void onLikeClick(int position);
        void onDislikeClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public CommentStuffForTextPostAdapter(Context mContext, List<CommentStuffForTextPost> mComment){
        this.mContext = mContext;
        this.mComment = mComment;
    }

    public CommentStuffForTextPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_comment_item_layout, parent, false);
        return new CommentStuffForTextPostAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentStuffForTextPostAdapter.ViewHolder holder, int position) {
        CommentStuffForTextPost uploadCurrent2 = mComment.get(position);
        holder.Username.setText(uploadCurrent2.getUser_name());
        holder.Date.setText(uploadCurrent2.getDate());
        holder.Content.setText(uploadCurrent2.getContent());

        String KeyComments = uploadCurrent2.getKey();
        String KeyOGPosts = uploadCurrent2.getOldKey();
        final String MyUID = firebaseAuth.getCurrentUser().getUid();

        final String TAG = "KeysCommentDelete";
        Log.e(TAG, KeyComments + "    " + KeyOGPosts);


        final DatabaseReference DeleteVisible = FirebaseDatabase.getInstance()
                .getReference("General_Text_Post")
                .child(KeyOGPosts)
                .child("Comments")
                .child(KeyComments)
                .child("content");

        DeleteVisible.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*DeleteVisible.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.getValue().toString());
                String Test = dataSnapshot.getValue().toString();
                if(Test == MyUID){
                    DeleteComment();
                }
                else{
                    holder.DeleteComment.setVisibility(View.GONE);
                }
            }

            private void DeleteComment() {
                DeleteVisible.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, Date, Content, LikeCount, DislikeCount;
        private ImageButton Upvote, Downvote, DeleteComment;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostComment);
            Date = itemView.findViewById(R.id.tvDatumCommentTextPost);
            Content = itemView.findViewById(R.id.tvContentCommentTextPost);
            LikeCount = itemView.findViewById(R.id.tvUpvoteCountCommentTextPost);
            DislikeCount = itemView.findViewById(R.id.tvDownvoteCountCommentTextPost);
            Upvote = itemView.findViewById(R.id.ibUpvoteCommentTextPost);
            Downvote = itemView.findViewById(R.id.ibDownvoteCommentTextPost);
            DeleteComment = itemView.findViewById(R.id.ibDeleteIconComments);

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
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onLikeClick(position);
                        }
                    }
                }
            });

            Downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDislikeClick(position);
                        }
                    }
                }
            });
        }
    }

}
