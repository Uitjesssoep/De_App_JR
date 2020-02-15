package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForChatRoomGroupAdapter extends RecyclerView.Adapter<PostStuffForChatRoomGroupAdapter.ImageViewHolder> {

    private List<PostStuffForChatRoom> mUploads;
    private Context mContext;
    private String Username;

    public PostStuffForChatRoomGroupAdapter(Context context, List<PostStuffForChatRoom> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chat_message_item, parent, false);
        return new PostStuffForChatRoomGroupAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        PostStuffForChatRoom uploadCurrent = mUploads.get(position);
        holder.Message.setText(uploadCurrent.getmMessage());
        StringBuilder str = new StringBuilder(uploadCurrent.getmDate());
        str.replace(5, 12, "");
        str.replace(11, 16, "");
        str.replace(8, 9, "-");
        String Date = str.toString();
        holder.Date.setText(Date);
        String UID = uploadCurrent.getmUID();

        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (uploadCurrent.getmUID().equals(MyUID)){
            holder.itemView.setBackgroundResource(R.drawable.edittext_mychatitem);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.edittext_chatitem);
        }


        //  Log.e("Check", uploadCurrent.getmUserName());
        FirebaseDatabase.getInstance().getReference("users").child(UID).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Username = dataSnapshot.getValue().toString();
                holder.Username.setText(Username);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
