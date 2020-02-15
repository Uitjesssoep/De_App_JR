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
import com.squareup.picasso.Picasso;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_private_list_item, parent, false);
        return new PostStuffForPrivateChatAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostStuffForPrivateChatAdapter.ViewHolder holder, int position) {
        final PostStuffMakePrivateChat uploadCurrent = mPost.get(position);
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String UID = uploadCurrent.getmUID();

        FirebaseDatabase.getInstance().getReference().child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Username = dataSnapshot.child("userName").getValue().toString();
                holder.Username.setText(Username);

                Picasso.get()
                        .load(dataSnapshot.child("profilePicture").getValue().toString())
                        .placeholder(R.drawable.neutral_profile_picture_nobackground)
                        .fit()
                        .centerCrop()
                        .into(holder.ProfilePicOtherUser);
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

        public TextView Username, LastMessage, TimeLastMessage;
        public ImageView ProfilePicOtherUser;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameToChatWith);
            LastMessage = itemView.findViewById(R.id.tvLastMessagePrivateChat);
            TimeLastMessage = itemView.findViewById(R.id.tvTimeOfLastMessage);
            ProfilePicOtherUser = itemView.findViewById(R.id.ivProfilePictureChatList);

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
