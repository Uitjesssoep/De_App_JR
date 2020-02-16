package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Imageposts.ImagePostViewing;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PostStuffForChatRoomGroupAdapter extends RecyclerView.Adapter<PostStuffForChatRoomGroupAdapter.ImageViewHolder> {

    private List<PostStuffForChatRoom> mUploads;
    private Context mContext;
    private String Username;
    private Calendar cal;
    private SimpleDateFormat dateFormat;
    private String Date;

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
        final PostStuffForChatRoom uploadCurrent = mUploads.get(position);
        holder.Message.setText(uploadCurrent.getmMessage());

        cal = Calendar.getInstance();
        cal.setTimeInMillis(uploadCurrent.getmDate());
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
        Date = dateFormat.format(cal.getTime());

        StringBuilder str = new StringBuilder(Date);
        str.replace(5, 12, "");
        str.replace(11, 16, "");
        str.replace(8, 9, "-");
        String Date = str.toString();
        holder.Date.setText(Date);
        String UID = uploadCurrent.getmUID();

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

        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (uploadCurrent.getmUID().equals(MyUID)){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.itemView.getLayoutParams());
            params.gravity = Gravity.RIGHT;
            holder.itemView.setBackgroundResource(R.drawable.edittext_mychatitem);
        }else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.itemView.getLayoutParams());
            params.gravity = Gravity.LEFT;
            holder.itemView.setBackgroundResource(R.drawable.edittext_chatitem);
        }
        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImagePostViewing.class);
                intent.putExtra("ImageUri", uploadCurrent.getmImageUrl());
                intent.putExtra("UID", uploadCurrent.getmUID());
                intent.putExtra("Date", uploadCurrent.getmDate());
                intent.putExtra("Message", uploadCurrent.getmMessage());
                intent.putExtra("key", "no key");
                mContext.startActivity(intent);
            }
        });


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
