package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;

import java.util.List;

public class PostStuffForChatRoomAdapter extends RecyclerView.Adapter<PostStuffForChatRoomAdapter.ChatViewHolder> {

    public Context mContext;
    public List<PostStuffForChatRoom> mMessages;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatRoomAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onUserNameClick (int position);
    }

    public PostStuffForChatRoomAdapter(Context mContext, List<PostStuffForChatRoom> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    public void setOnItemClickListener(PostStuffForChatRoomAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chat_message_item, parent, false);
        return new PostStuffForChatRoomAdapter.ChatViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        PostStuffForChatRoom uploadCurrent = mMessages.get(position);

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
