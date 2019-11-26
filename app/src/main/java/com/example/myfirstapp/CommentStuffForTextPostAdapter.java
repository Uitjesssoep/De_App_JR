package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CommentStuffForTextPostAdapter extends RecyclerView.Adapter<CommentStuffForTextPostAdapter.ViewHolder>{

    public Context mContext;
    public List<CommentStuffForTextPost> mComment;


    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onUserNameClick(int position);
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
    public void onBindViewHolder(@NonNull CommentStuffForTextPostAdapter.ViewHolder holder, int position) {
        CommentStuffForTextPost uploadCurrent2 = mComment.get(position);
        holder.Username.setText(uploadCurrent2.getUser_name());
        holder.Date.setText(uploadCurrent2.getDate());
        holder.Content.setText(uploadCurrent2.getContent());

        String KeyHoera = uploadCurrent2.getKey().toString();
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, Date, Content, LikeCount, DislikeCount;
        private ImageButton Upvote, Downvote;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostComment);
            Date = itemView.findViewById(R.id.tvDatumCommentTextPost);
            Content = itemView.findViewById(R.id.tvContentCommentTextPost);
            LikeCount = itemView.findViewById(R.id.tvUpvoteCountCommentTextPost);
            DislikeCount = itemView.findViewById(R.id.tvDownvoteCountCommentTextPost);
            Upvote = itemView.findViewById(R.id.ibUpvoteCommentTextPost);
            Downvote = itemView.findViewById(R.id.ibDownvoteCommentTextPost);

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
        }
    }

}
