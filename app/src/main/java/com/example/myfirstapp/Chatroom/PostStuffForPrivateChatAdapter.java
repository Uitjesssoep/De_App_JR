package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostStuffForPrivateChatAdapter extends RecyclerView.Adapter<PostStuffForPrivateChatAdapter.ViewHolder> {
    private List<PostStuffForChatRoom> MessagesList = new ArrayList();
    private String Username;
    public Context mContext;
    public List<PostStuffMakePrivateChat> mPost;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PostStuffForChatAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    private FirebaseUser firebaseUser;

    public PostStuffForPrivateChatAdapter(Context mContext, List<PostStuffMakePrivateChat> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public PostStuffForPrivateChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_private_list_item, parent, false);
        return new PostStuffForPrivateChatAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        DatabaseReference LastSeenRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        LastSeenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserProfileToDatabase userProfileToDatabase = postSnapshot.getValue(UserProfileToDatabase.class);
                    if (userProfileToDatabase.isOnline()) {
                        holder.LastSeen.setText("Online");
                        holder.LastSeen.setTextColor(Color.BLUE);
                    } else {
                        long Timestamp = userProfileToDatabase.getTimestamp();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Timestamp);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
                        String Date = dateFormat.format(calendar.getTime());
                        holder.LastSeen.setText("Last seen: " + Date);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final PostStuffMakePrivateChat uploadCurrent = mPost.get(position);
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String UID = uploadCurrent.getmUID();

        //  final List<PostStuffForChatRoom> MessagesList = new ArrayList<>();
        DatabaseReference myDatabase2 = FirebaseDatabase.getInstance().getReference("Messages").child(MyUID).child(uploadCurrent.getmUID());
        myDatabase2.orderByChild("mDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostStuffForChatRoom postStuffForChatRoom = dataSnapshot.getValue(PostStuffForChatRoom.class);
                MessagesList.add(postStuffForChatRoom);
                holder.LastMessage.setText(MessagesList.get(MessagesList.size() - 1).getmMessage());
                final String Date;
                final Calendar cal;
                final long Timestamp;
                Timestamp = MessagesList.get(MessagesList.size() - 1).getmDate();
                cal = Calendar.getInstance();
                cal.setTimeInMillis(Timestamp);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
                Date = dateFormat.format(cal.getTime());
                StringBuilder str = new StringBuilder(Date);
                str.replace(5, 12, "");
                str.replace(11, 16, "");
                str.replace(8, 9, "-");
                String mDate = str.toString();
                holder.TimeLastMessage.setText(mDate);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Username = dataSnapshot.child("userName").getValue().toString();
                holder.Username.setText(Username);

                Picasso.get()
                        .load(dataSnapshot.child("profilePicture").getValue().toString())
                        .placeholder(R.drawable.neutral_profile_picture_nobackground)
                        .fit()
                        .centerCrop()
                        .into(holder.ProfilePicOtherUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Username, LastMessage, TimeLastMessage, LastSeen;
        public ImageView ProfilePicOtherUser;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameToChatWith);
            LastMessage = itemView.findViewById(R.id.tvLastMessagePrivateChat);
            TimeLastMessage = itemView.findViewById(R.id.tvTimeOfLastMessage);
            ProfilePicOtherUser = itemView.findViewById(R.id.ivProfilePictureChatList);
            LastSeen = itemView.findViewById(R.id.tvLastSeenPrivateChat);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
