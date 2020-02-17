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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Imageposts.ImagePostViewing;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PostStuffForChatRoomAdapterNúmeroDos extends RecyclerView.Adapter<PostStuffForChatRoomAdapterNúmeroDos.ImageViewHolder> {

    private List<PostStuffForChatRoom> mUploads;
    private Context mContext;
    private String ImageUri;
    private Calendar cal;
    private String Date;
    private SimpleDateFormat dateFormat;


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
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final PostStuffForChatRoom uploadCurrent = mUploads.get(position);

        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.SeenM.setVisibility(View.GONE);
        Log.e("Seen?", uploadCurrent.getmSeen().toString());
        if (uploadCurrent.getmSeen()) {
            holder.SeenM.setVisibility(View.VISIBLE);
        }
        String mUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages").child(MyUID).child(mUID).child(uploadCurrent.getKey()).child("mSeen");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((boolean) dataSnapshot.getValue()) {
                    holder.SeenM.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (uploadCurrent.getmUID().equals(MyUID)) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.itemView.getLayoutParams());
            params.gravity = Gravity.RIGHT;

            holder.MessageM.setText(uploadCurrent.getmMessage());

            cal = Calendar.getInstance();
            cal.setTimeInMillis(uploadCurrent.getmDate());
            dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
            Date = dateFormat.format(cal.getTime());

            StringBuilder str = new StringBuilder(Date);
            str.replace(5, 12, "");
            str.replace(11, 16, "");
            str.replace(8, 9, "-");
            String Date = str.toString();
            holder.DateM.setText(Date);

            String UID = uploadCurrent.getmUID();

            //  Log.e("Check", uploadCurrent.getmUserName());
            FirebaseDatabase.getInstance().getReference("users").child(UID).child("userName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String Username = dataSnapshot.getValue().toString();
                    holder.UsernameM.setText(Username);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.OtherConstraint.setVisibility(View.GONE);
            holder.Message.setVisibility(View.GONE);
            holder.Username.setVisibility(View.GONE);
            holder.Date.setVisibility(View.GONE);
            holder.Image.setVisibility(View.GONE);

            holder.MineConstraint.setVisibility(View.VISIBLE);
            holder.UsernameM.setVisibility(View.GONE);
            holder.DateM.setVisibility(View.VISIBLE);

            String Type = uploadCurrent.getmType();
            if (Type.equals("image")) {
                Log.e("TYPE=IMAGE", uploadCurrent.getmImageUrl());
                Picasso.get().load(uploadCurrent.getmImageUrl()).into(holder.ImageM);
                holder.ImageM.setVisibility(View.VISIBLE);
                String Empty = "";
                if (Empty.equals(uploadCurrent.getmMessage())) {
                    holder.MessageM.setVisibility(View.GONE);
                } else {
                    holder.MessageM.setVisibility(View.VISIBLE);
                }
            } else {
                holder.ImageM.setVisibility(View.GONE);
                holder.MessageM.setVisibility(View.VISIBLE);
            }


        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.itemView.getLayoutParams());
            params.gravity = Gravity.LEFT;

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

            //  Log.e("Check", uploadCurrent.getmUserName());
            FirebaseDatabase.getInstance().getReference("users").child(UID).child("userName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String Username = dataSnapshot.getValue().toString();
                    holder.Username.setText(Username);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.OtherConstraint.setVisibility(View.VISIBLE);
            holder.Username.setVisibility(View.GONE);
            holder.Date.setVisibility(View.VISIBLE);

            holder.MineConstraint.setVisibility(View.GONE);
            holder.MessageM.setVisibility(View.GONE);
            holder.UsernameM.setVisibility(View.GONE);
            holder.DateM.setVisibility(View.GONE);
            holder.ImageM.setVisibility(View.GONE);

            String Type = uploadCurrent.getmType();
            if (Type.equals("image")) {
                Log.e("TYPE=IMAGE", uploadCurrent.getmImageUrl());
                Picasso.get().load(uploadCurrent.getmImageUrl()).into(holder.Image);
                holder.Image.setVisibility(View.VISIBLE);
                String Empty = "";
                if (Empty.equals(uploadCurrent.getmMessage())) {
                    holder.Message.setVisibility(View.GONE);
                } else {
                    holder.Message.setVisibility(View.VISIBLE);
                }
            } else {
                holder.Image.setVisibility(View.GONE);
                holder.Message.setVisibility(View.VISIBLE);
            }
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
        holder.ImageM.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView Message, Username, Date, MessageM, UsernameM, DateM, SeenM;
        public ImageView Image, ImageM;
        public ConstraintLayout OtherConstraint, MineConstraint;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.tvMessage);
            Username = itemView.findViewById(R.id.tvUserNameMessage);
            Date = itemView.findViewById(R.id.tvDateMessage);
            Image = itemView.findViewById(R.id.ivImageInPrivateChat);

            MessageM = itemView.findViewById(R.id.tvMessageM);
            UsernameM = itemView.findViewById(R.id.tvUserNameMessageM);
            DateM = itemView.findViewById(R.id.tvDateMessageM);
            ImageM = itemView.findViewById(R.id.ivImageInPrivateChatM);
            SeenM = itemView.findViewById(R.id.tvSeenMessageM);

            OtherConstraint = itemView.findViewById(R.id.otheruserchatitem);
            MineConstraint = itemView.findViewById(R.id.minechatitem);
        }
    }


}
