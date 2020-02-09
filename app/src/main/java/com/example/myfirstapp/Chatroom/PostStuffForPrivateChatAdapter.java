package com.example.myfirstapp.Chatroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForPrivateChatAdapter extends RecyclerView.Adapter<PostStuffForPrivateChatAdapter.ViewHolder> {

    private String Username;
    public Context mContext;
    public List<PostStuffMakePrivateChat> mPost;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onUserNameClick(int position);

        void onUpvoteClick(int position);

        void onDownvoteClick(int position);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForPrivateChatAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostStuffForPrivateChatAdapter.ViewHolder holder, int position) {
        final PostStuffMakePrivateChat uploadCurrent = mPost.get(position);
        final String UID1 = uploadCurrent.getUID1();
        final String UID2 = uploadCurrent.getUID2();
        final String Username1 = uploadCurrent.getUser_name1();
        final String Username2 = uploadCurrent.getUser_name2();
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue().equals(Username1)) {
                    Username = Username1;

                }else {
                    Username = Username2;
                }
                holder.Username.setText(Username);
                holder.Title.setVisibility(View.GONE);
                StringBuilder str = new StringBuilder(uploadCurrent.getDate());
                str.replace(5, 12, "");
                String Date = str.toString();
                holder.Date.setText(Date);
                holder.CommentCount.setVisibility(View.GONE);
                holder.CommentLogo.setVisibility(View.GONE);
                holder.Content.setVisibility(View.GONE);
                holder.Streepje.setTextColor(Color.WHITE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        String KeyYeah = uploadCurrent.getKey();

        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(KeyYeah).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(KeyYeah).child("Dislikes");
        LikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                holder.LikeCount.setText("" + LikeCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DislikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DislikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                holder.DislikeCount.setText("" + DislikeCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //kijken of de user deleted is
       /* final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("Private_Chatrooms").child(KeyYeah).child("user_name");
        final String PostUID = uploadCurrent.getUID();

        UserUIDCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(PostUID)) {

                } else {

                    ChangeUsername.setValue("[deleted_user]");
                    holder.Username.setText("[deleted_user]");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //kijken als user anonymous is dat de username voor de anon zelf wel zichtbaar is

       /* String MyUID2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String PostUID2 = uploadCurrent.getUID();

        if (MyUID2.equals(PostUID2)) {

            DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(MyUID2).child("userName");
            GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String MyUserName = dataSnapshot.getValue().toString();

                    holder.Username.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                    holder.Username.setText(MyUserName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

        }*/

        //final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(KeyYeah).child("Likes");
        CheckIfUpvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {
                    holder.Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_green_24dp);
                } else {
                    holder.Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(KeyYeah).child("Dislikes");
        CheckIfDownvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {
                    holder.Downvote.setImageResource(R.drawable.ic_keyboard_arrow_down_green_24dp);
                } else {
                    holder.Downvote.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }

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

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date, Streepje;
        public ImageButton Upvote, Downvote, DeleteTextPost;
        public ImageView CommentLogo;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Streepje=itemView.findViewById(R.id.tvStreepjeTextPostItem);

            Username = itemView.findViewById(R.id.tvTitleTextPostItem);
            LikeCount = itemView.findViewById(R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById(R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById(R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById(R.id.tvUsernameTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);
            CommentLogo = itemView.findViewById(R.id.ivCommentImageTextPostItem);
            Content = itemView.findViewById(R.id.tvContentTextPostItem);

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

            Username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUserNameClick(position);
                        }
                    }
                }
            });

            Upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUpvoteClick(position);
                        }
                    }
                }
            });

            Downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDownvoteClick(position);
                        }
                    }
                }
            });

        }
    }
}
