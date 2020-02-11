package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForPrivateChatAdapter extends RecyclerView.Adapter<PostStuffForPrivateChatAdapter.ViewHolder> {

    private String Username;
    public Context mContext;
    public List<PostStuffMakePrivateChat> mPost;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(PostStuffForChatAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    private FirebaseUser firebaseUser;

    public PostStuffForPrivateChatAdapter(Context mContext, List<PostStuffMakePrivateChat> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public PostStuffForPrivateChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForPrivateChatAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostStuffForPrivateChatAdapter.ViewHolder holder, int position) {
        final PostStuffMakePrivateChat uploadCurrent = mPost.get(position);
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String UID = uploadCurrent.getmUID();

        FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Username = dataSnapshot.getValue().toString();
                holder.Username.setText(Username);
                holder.Title.setVisibility(View.GONE);
                StringBuilder str = new StringBuilder(uploadCurrent.getmDate());
                str.replace(5, 12, "");
                String Date = str.toString();
                holder.Date.setText(Date);
                holder.CommentCount.setVisibility(View.GONE);
                holder.CommentLogo.setVisibility(View.GONE);
                holder.Content.setVisibility(View.GONE);
                holder.Streepje.setTextColor(Color.WHITE);
                holder.LikeCount.setVisibility(View.GONE);
                holder.DislikeCount.setVisibility(View.GONE);
                holder.CommentCount.setVisibility(View.GONE);
                holder.CommentLogo.setVisibility(View.GONE);
                holder.Downvote.setVisibility(View.GONE);
                holder.Upvote.setVisibility(View.GONE);
                holder.DeleteTextPost.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date, Streepje;
        public ImageButton Upvote, Downvote, DeleteTextPost;
        public ImageView CommentLogo;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Streepje=itemView.findViewById(R.id.tvStreepjeTextPostItem);

            Username = itemView.findViewById(R.id.tvTitleTextPostItem);
            LikeCount = itemView.findViewById(R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById(R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById(R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById(R.id.tvUsernameTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);
            CommentLogo = itemView.findViewById(R.id.ivCommentImageTextPostItem);
            Content = itemView.findViewById(R.id.tvContentTextPostItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
