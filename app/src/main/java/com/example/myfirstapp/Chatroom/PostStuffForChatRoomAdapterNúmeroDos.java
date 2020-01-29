package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;

import java.util.List;

public class PostStuffForChatRoomAdapterNúmeroDos extends RecyclerView.Adapter<PostStuffForChatRoomAdapterNúmeroDos.ImageViewHolder> {

    private List<PostStuffForChatRoom> mUploads;
    private Context mContext;

    public PostStuffForChatRoomAdapterNúmeroDos(Context context, List<PostStuffForChatRoom> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chat_message_item, parent, false);
        return new PostStuffForChatRoomAdapterNúmeroDos.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        PostStuffForChatRoom uploadCurrent = mUploads.get(position);
        holder.Message.setText(uploadCurrent.getmMessage());
        holder.Date.setText(uploadCurrent.getmDate());
        holder.Username.setText(uploadCurrent.getUserName());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView Message, Username, Date;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.tvMessage);
            Username = itemView.findViewById(R.id.tvUserNameMessage);
            Date = itemView.findViewById(R.id.tvDateMessage);
        }
    }


}
