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

    private String TAG = "test";
    private Context mContext;
    private List<PostStuffForChatRoom> mMessages;
    public int LikeCountAdapter, DislikeCountAdapter;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onUserNameClick(int position);
    }

    public PostStuffForChatRoomAdapter(Context mContext, List<PostStuffForChatRoom> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chat_message_item, parent, false);
        Log.e(TAG, "onCreateViewHolder: ");
        return new PostStuffForChatRoomAdapter.ChatViewHolder(v, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, int position) {
        PostStuffForChatRoom uploadCurrent = mMessages.get(position);
        holder.Username.setText(uploadCurrent.getmUserName());
        StringBuilder str = new StringBuilder(uploadCurrent.getmDate());
        str.replace(7, 12, "");
        String Date = str.toString();
        holder.Date.setText(uploadCurrent.getmDate());
        holder.Message.setText(uploadCurrent.getmMessage());
        Log.e(TAG, "onBindViewHolder: ");


    }

    public void setOnItemClickListener(PostStuffForChatRoomAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView Username, Date, Message;

        public ChatViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            Username = itemView.findViewById(R.id.tvUserNameMessage);
            Date = itemView.findViewById(R.id.tvDateMessage);
            Message = itemView.findViewById(R.id.tvMessage);
            Log.e(TAG, "ChatViewHolder: ");

        }

    }

}
