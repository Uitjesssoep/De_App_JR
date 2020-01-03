package com.example.myfirstapp.Textposts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.crypto.spec.DESKeySpec;

public class CommentStuffForTextPostAdapter extends RecyclerView.Adapter<CommentStuffForTextPostAdapter.ViewHolder>{

    public Context mContext;
    public List<CommentStuffForTextPost> mComment;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onUserNameClick(int position);
        void onDeleteCommentClick(int position);
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
        holder.DeleteComment.setImageResource(R.drawable.delete_icon);

        String KeyComments = uploadCurrent2.getKey();
        String KeyOGPosts = uploadCurrent2.getOldKey();
        final String MyUID = firebaseAuth.getCurrentUser().getUid();

        final String TAG = "KeysCommentDelete";
        Log.e(TAG, "Uid " + MyUID);

        Log.e(TAG, "Keys "+ KeyComments + "    " + KeyOGPosts);


        final DatabaseReference DeleteVisible = FirebaseDatabase.getInstance()
                .getReference("General_Text_Posts")
                .child(KeyOGPosts)
                .child("Comments")
                .child(KeyComments);


        DeleteVisible.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.e(TAG, "UID uit database "+ dataSnapshot.child("uid").getValue().toString());
                    String Test = dataSnapshot.child("uid").getValue().toString();

                    final String MyUID2 = firebaseAuth.getCurrentUser().getUid();
                    Log.e(TAG, "UID uit auth " + MyUID2);

                    if(Test.equals(MyUID2)){
                        DeleteCommentIconVisible();
                        Log.e(TAG, "deleting");
                    }
                    else{
                        holder.DeleteComment.setVisibility(View.GONE);
                        Log.e(TAG, "niet deleten");
                    }
            }

            private void DeleteCommentIconVisible() {
                Log.e(TAG, "deletecommentvisible is bereikt");
                holder.DeleteComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeleteVisible.removeValue();
                        Log.e(TAG, "delete value");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, Date, Content;
        private ImageButton DeleteComment;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostComment);
            Date = itemView.findViewById(R.id.tvDatumCommentTextPost);
            Content = itemView.findViewById(R.id.tvContentCommentTextPost);
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

            DeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteCommentClick(position);
                        }
                    }
                }
            });

        }
    }

}
