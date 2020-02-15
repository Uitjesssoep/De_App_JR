package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

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
        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.Message.setText(uploadCurrent.getmMessage());
        StringBuilder str = new StringBuilder(uploadCurrent.getmDate());
        str.replace(5, 12, "");
        str.replace(11, 16, "");
        str.replace(8, 9, "-");
        String Date = str.toString();
        holder.Date.setText(Date);
       // holder.Image.setImageResource(R.drawable.app_logo_200);
        String Type = uploadCurrent.getmType();
       if (Type.equals("image")){
           Log.e("TYPE=IMAGE", uploadCurrent.getmImageUrl());
            Picasso.get().load(uploadCurrent.getmImageUrl()).into(holder.Image);
            holder.Image.setVisibility(View.VISIBLE);
            holder.Message.setVisibility(View.GONE);
        }
       else {
           holder.Image.setVisibility(View.GONE);
           holder.Message.setVisibility(View.VISIBLE);
       }
      //  Log.e("Check", uploadCurrent.getmUserName());
        holder.Username.setVisibility(View.GONE);
        if (uploadCurrent.getmUID().equals(MyUID)){
            holder.itemView.setBackgroundResource(R.drawable.edittext_mychatitem);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.edittext_chatitem);
        }
        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView Message, Username, Date;
        public ImageView Image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.tvMessage);
            Username = itemView.findViewById(R.id.tvUserNameMessage);
            Date = itemView.findViewById(R.id.tvDateMessage);
            Image = itemView.findViewById(R.id.ivImageInPrivateChat);
        }
    }


}
