package com.example.myfirstapp.Textposts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.answers.FirebaseAnalyticsEvent;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Report_TextPost_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.List;

import javax.crypto.spec.DESKeySpec;

public class CommentStuffForTextPostAdapter extends RecyclerView.Adapter<CommentStuffForTextPostAdapter.ViewHolder>{

    public Context mContext;
    public List<CommentStuffForTextPost> mComment;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private int LikeCountAdapter, DislikeCountAdapter;


    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onUserNameClick(int position);
        void onUpvoteClick(int position);
        void onDownvoteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public CommentStuffForTextPostAdapter(Context mContext, List<CommentStuffForTextPost> mComment){
        this.mContext = mContext;
        this.mComment = mComment;
    }

    public CommentStuffForTextPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_comment_item_layout, parent, false);
        return new CommentStuffForTextPostAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentStuffForTextPostAdapter.ViewHolder holder, int position) {
        final CommentStuffForTextPost uploadCurrent2 = mComment.get(position);
        holder.Username.setText(uploadCurrent2.getUser_name());
        holder.Date.setText(uploadCurrent2.getDate());
        holder.Content.setText(uploadCurrent2.getContent());


        //kijken of de user deleted is
        final String KeyComments2 = uploadCurrent2.getKey();
        final String KeyOGPosts2 = uploadCurrent2.getOldKey();
        final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("user_name");
        final String CommentUID = uploadCurrent2.getUID().toString();

        UserUIDCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(CommentUID)){

                }

                else{

                    ChangeUsername.setValue("[deleted_user]");
                    holder.Username.setText("[deleted_user]");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.DeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String KeyPost = uploadCurrent2.getOldKey();
                final String KeyComment = uploadCurrent2.getKey();
                final String CommentUID = uploadCurrent2.getUID();
                final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(MyUID.equals(CommentUID)){

                    PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteComment);
                    popupMenu.inflate(R.menu.popup_menu_comments_delete);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){

                                case R.id.delete_option_comments:

                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                    dialog.setTitle("Delete your comment?");
                                    dialog.setMessage("Deleting this comment cannot be undone! The comment itself will remain, only its content will be removed. Are you sure you want to delete it?");

                                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            final DatabaseReference RemoveComment = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyPost).child("Comments").child(KeyComment);
                                            RemoveComment.child("content").setValue("[deleted_comment]");
                                            RemoveComment.child("user_name").setValue("[deleted_comment_user]");

                                            Intent intent = new Intent(mContext, General_Feed_Activity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            mContext.startActivity(intent);


                                        }
                                    });

                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.dismiss();

                                        }
                                    });

                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();

                                    break;

                                default:
                                    break;

                            }

                            return false;
                        }
                    });
                    popupMenu.show();

                }

                else{

                    PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteComment);
                    popupMenu.inflate(R.menu.popup_menu_comments_report);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){

                                case R.id.report_option_comments:

                                    Intent intent= new Intent(mContext, Report_TextPost_Activity.class);
                                    intent.putExtra("Titel", uploadCurrent2.getContent());
                                    intent.putExtra("User", uploadCurrent2.getUser_name());
                                    intent.putExtra("Key", uploadCurrent2.getKey());
                                    intent.putExtra("Soort", "comment");
                                    mContext.startActivity(intent);

                                    break;

                                default:
                                    break;

                            }

                            return false;
                        }
                    });
                    popupMenu.show();

                }


            }
        });


        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Dislikes");
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

        final String MyUID = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Likes");
        CheckIfUpvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(MyUID)){
                    holder.Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_green_24dp);
                }
                else{
                    holder.Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Dislikes");
        CheckIfDownvoted.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(MyUID)){
                    holder.Downvote.setImageResource(R.drawable.ic_keyboard_arrow_down_green_24dp);
                }
                else{
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
        return mComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, Date, Content, LikeCount, DislikeCount;
        private ImageButton DeleteComment, Upvote, Downvote;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostComment);
            Date = itemView.findViewById(R.id.tvDatumCommentTextPost);
            Content = itemView.findViewById(R.id.tvContentCommentTextPost);
            DeleteComment = itemView.findViewById(R.id.ibDeleteIconComments);
            LikeCount = itemView.findViewById(R.id.tvLikeCounterComment);
            DislikeCount = itemView.findViewById(R.id.tvDislikeCounterComment);
            Upvote = itemView.findViewById(R.id.ibLikeUpComment);
            Downvote = itemView.findViewById(R.id.ibLikeDownComment);

            Username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUserNameClick(position);
                        }
                    }
                }
            });

            Upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUpvoteClick(position);
                        }
                    }
                }
            });

            Downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDownvoteClick(position);
                        }
                    }
                }
            });

        }
    }

}
