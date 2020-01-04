package com.example.myfirstapp.Textposts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForTextAdapter extends RecyclerView.Adapter<PostStuffForTextAdapter.ViewHolder>{

    public Context mContext;
    public List<PostStuffForText> mPost;
    public int CommentCountAdapter, LikeCountAdapter, DislikeCountAdapter;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUserNameClick(int position);
        void onUpvoteClick(int position);
        void onDownvoteClick(int position);
        void onDeleteIconClick(int position);
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PostStuffForText uploadCurrent = mPost.get(position);
        holder.Username.setText(uploadCurrent.getUser_name());
        holder.Title.setText(uploadCurrent.getTitle());
        holder.KeyHolder.setText(uploadCurrent.getKey());
        holder.Date.setText(uploadCurrent.getDate());
        holder.DeleteTextPost.setImageResource(R.drawable.delete_icon);

        String KeyYeah = uploadCurrent.getKey().toString();
        final DatabaseReference CommentCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Comments");
        CommentCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               CommentCountAdapter = (int) dataSnapshot.getChildrenCount();
               holder.CommentCount.setText("Number of comments: " + CommentCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Dislikes");
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

        //gaan kijken of post van jou is om te kijken of ie delete icon moet laten zien:
        final String KeyPost = uploadCurrent.getKey().toString();
        final DatabaseReference DeleteIconCheck = FirebaseDatabase.getInstance().getReference("General_Text_Posts");

        DeleteIconCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(KeyPost)){

                    final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                    final String PostUIDCheck = dataSnapshot.child(KeyPost).child("uid").getValue().toString();

                    if(MyUIDCheck.equals(PostUIDCheck)){
                        //zou moeten werken gewoon??
                    }
                    else{
                        holder.DeleteTextPost.setVisibility(View.GONE);
                    }

                }
                else{
                    //post is gedelete als het goed is
                }

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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById (R.id.tvUsernameTextPostItem);
            LikeCount = itemView.findViewById (R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById (R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById (R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById (R.id.tvTitleTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);

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

            DeleteTextPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteIconClick(position);
                        }
                    }
                }
            });

        }
    }
}
