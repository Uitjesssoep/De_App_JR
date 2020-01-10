package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.Textposts.PostStuffForText;
import com.example.myfirstapp.Textposts.PostStuffForTextAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForChatAdapter {

    public Context mContext;
    public List<PostStuffForChat> mPost;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
    }

    public void setOnItemClickListener(PostStuffForChatAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    private FirebaseUser firebaseUser;

    public PostStuffForChatAdapter(Context mContext, List<PostStuffForChat> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    //@Override
    public PostStuffForChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForChatAdapter.ViewHolder(view, mListener);
    }

    //@Override
    public void onBindViewHolder(@NonNull final PostStuffForChatAdapter.ViewHolder holder, int position) {
        PostStuffForChat uploadCurrent = mPost.get(position);
        holder.Username.setText(uploadCurrent.getUser_name());
        holder.Title.setText(uploadCurrent.getTitle());
        holder.KeyHolder.setText(uploadCurrent.getKey());
        holder.Date.setText(uploadCurrent.getDate());

        String KeyYeah = uploadCurrent.getKey().toString();

        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Dislikes");
        LikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                holder.LikeCount.setText("" + LikeCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DislikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DislikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                holder.DislikeCount.setText("" + DislikeCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //kijken of de user deleted is
        final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("user_name");
        final String PostUID = uploadCurrent.getUID().toString();

        UserUIDCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(PostUID)) {

                } else {

                    ChangeUsername.setValue("[deleted_user]");
                    holder.Username.setText("[deleted_user]");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //gaan kijken of post van jou is om te kijken of ie delete icon moet laten zien:
        final String KeyPost = uploadCurrent.getKey().toString();
        final DatabaseReference DeleteIconCheck = FirebaseDatabase.getInstance().getReference("Chatrooms");

        DeleteIconCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(KeyPost)) {

                    final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                    final String PostUIDCheck = dataSnapshot.child(KeyPost).child("uid").getValue().toString();

                    if (MyUIDCheck.equals(PostUIDCheck)) {
                        //zou moeten werken gewoon??
                    } else {
                        holder.DeleteTextPost.setVisibility(View.GONE);
                    }

                } else {
                    //post is gedelete als het goed is
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String MyUID = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Likes");
        CheckIfUpvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {
                    holder.Upvote.setImageResource(R.drawable.pijl_omhoog_geklikt);
                } else {
                    holder.Upvote.setImageResource(R.drawable.pijl_omhoog_neutraal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Dislikes");
        CheckIfDownvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {
                    holder.Downvote.setImageResource(R.drawable.pijl_omlaag_geklikt);
                } else {
                    holder.Downvote.setImageResource(R.drawable.pijl_omlaag_neutraal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //@Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostItem);
            LikeCount = itemView.findViewById(R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById(R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById(R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById(R.id.tvTitleTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);
        }
    }
}
