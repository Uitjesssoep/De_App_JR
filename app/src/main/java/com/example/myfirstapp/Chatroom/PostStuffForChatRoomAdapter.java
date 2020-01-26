package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostStuffForChatRoomAdapter extends RecyclerView.Adapter<PostStuffForChatRoomAdapter.ChatViewHolder> {

    public Context mContext;
    public List<PostStuffForChat> mMessages;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatRoomAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteIconClick(int position);
        void onUserNameClick (int position);
        void onUpvoteClick (int position);
        void onDownvoteClick (int position);
    }

    public PostStuffForChatRoomAdapter(Context mContext, List<PostStuffForChat> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    public void setOnItemClickListener(PostStuffForChatRoomAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public ChatViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
        }

    }

}
