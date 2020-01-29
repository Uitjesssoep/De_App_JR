package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;

import java.util.List;

public class PostStuffForChatRoomAdapter extends RecyclerView.Adapter<PostStuffForChatRoomAdapter.ChatViewHolder> {

    public String TAG="test";
    public Context mContext;
    public List<PostStuffForChatRoom> mMessages;
    public int LikeCountAdapter, DislikeCountAdapter;

    private OnItemClickListener mListener;
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
        Log.e(TAG, "onCreateViewHolder: ");
        return new PostStuffForChatRoomAdapter.ChatViewHolder(v, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        PostStuffForChatRoom postStuffForChatRoom = mMessages.get(position);
        holder.Username.setText(postStuffForChatRoom.getUserName());
        StringBuilder str = new StringBuilder(postStuffForChatRoom.getDate());
        str.replace(7, 12, "");
        String Date = str.toString();
        holder.Date.setText(postStuffForChatRoom.getDate());
        holder.Message.setText(postStuffForChatRoom.getMessage());
        Log.e(TAG, "onBindViewHolder: ");


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView Username, Date, Message;

        public ChatViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            Username= itemView.findViewById(R.id.tvUserNameMessage);
            Date = itemView.findViewById(R.id.tvDateMessage);
            Message = itemView.findViewById(R.id.tvMessage);
            Log.e(TAG, "ChatViewHolder: ");

        }

    }

}
