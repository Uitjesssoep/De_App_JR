package com.example.myfirstapp.Textposts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Edit_PC_Activity;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Report_TextPost_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentStuffForTextPostAdapter extends RecyclerView.Adapter<CommentStuffForTextPostAdapter.ViewHolder>{

    public Context mContext;
    public List<CommentStuffForTextPost> mComment;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private int LikeCountAdapter, DislikeCountAdapter;


    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
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

    public void delete(int position) {
        mComment.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentStuffForTextPostAdapter.ViewHolder holder, final int position) {
        final CommentStuffForTextPost uploadCurrent2 = mComment.get(position);
        holder.Username.setText(uploadCurrent2.getUser_name());
        holder.Date.setText(uploadCurrent2.getDate());
        holder.Content.setText(uploadCurrent2.getContent());


        //Check if exists
        final String ThePostKey684 = uploadCurrent2.getOldKey();
        final String TheCommentKey684 = uploadCurrent2.getKey();
        final DatabaseReference CheckIfExists = FirebaseDatabase.getInstance().getReference("General_Posts");
        CheckIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(ThePostKey684)){
                    String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference DeleteAtMyComments = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("MyComments");
                    DeleteAtMyComments.child(TheCommentKey684).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //kijken of de user deleted is
        final String KeyComments2 = uploadCurrent2.getKey();
        final String KeyOGPosts2 = uploadCurrent2.getOldKey();
        final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("user_name");
        final String CommentUID = uploadCurrent2.getUID().toString();

        UserUIDCheck.addListenerForSingleValueEvent(new ValueEventListener() {
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

        final String PostUID = uploadCurrent2.getUID();
        final String MyUID684 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String PostUsername = uploadCurrent2.getUser_name();

        if(PostUID.equals(MyUID684)){

            if(PostUsername.equals("[deleted_comment_user]")){

                DatabaseReference GetMyUsername = FirebaseDatabase.getInstance().getReference("users").child(MyUID684).child("userName");
                GetMyUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String MyUsername = dataSnapshot.getValue().toString();
                        holder.Username.setText(MyUsername);
                        holder.Username.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else{
                holder.Username.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }

        }

        holder.DeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String KeyPost = uploadCurrent2.getOldKey();
                final String KeyComment = uploadCurrent2.getKey();
                final String CommentUID = uploadCurrent2.getUID();
                final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(MyUID.equals(CommentUID)){

                    DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedComments");
                    CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(KeyComment)){

                                PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteComment);
                                popupMenu.inflate(R.menu.popup_menu_textposts_withunsave);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){

                                            case R.id.delete_option_textposts:

                                                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                dialog.setTitle("Delete your comment?");
                                                dialog.setMessage("Deleting this comment cannot be undone! The comment itself will remain, only its content will be removed. Are you sure you want to delete it?");

                                                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        final DatabaseReference RemoveComment = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyPost).child("Comments").child(KeyComment);
                                                        RemoveComment.removeValue();

                                                        final DatabaseReference RemoveCommentFromMyProfile = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("MyComments").child(KeyComment);
                                                        RemoveCommentFromMyProfile.removeValue();

                                                        delete(position);

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

                                            case R.id.edit_option_textposts:

                                                    Intent intent = new Intent(mContext, Edit_PC_Activity.class);
                                                    intent.putExtra("Type", "Comment");
                                                    intent.putExtra("PostKey", uploadCurrent2.getOldKey());
                                                    intent.putExtra("CommentKey", uploadCurrent2.getKey());
                                                    mContext.startActivity(intent);

                                                break;

                                            case R.id.unsavepost_option_textposts:

                                                final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                SaveThePost.child("SavedComments").child(KeyComment).removeValue();

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
                                popupMenu.inflate(R.menu.popup_menu_textposts);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){

                                            case R.id.delete_option_textposts:

                                                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                dialog.setTitle("Delete your comment?");
                                                dialog.setMessage("Deleting this comment cannot be undone! The comment itself will remain, only its content will be removed. Are you sure you want to delete it?");

                                                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        final DatabaseReference RemoveComment = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyPost).child("Comments").child(KeyComment);
                                                        RemoveComment.removeValue();

                                                        final DatabaseReference RemoveCommentFromMyProfile = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("MyComments").child(KeyComment);
                                                        RemoveCommentFromMyProfile.removeValue();

                                                        notifyDataSetChanged();

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

                                            case R.id.edit_option_textposts:

                                                Intent intent = new Intent(mContext, Edit_PC_Activity.class);
                                                intent.putExtra("Type", "Comment");
                                                intent.putExtra("PostKey", uploadCurrent2.getOldKey());
                                                intent.putExtra("CommentKey", uploadCurrent2.getKey());
                                                mContext.startActivity(intent);

                                                break;

                                            case R.id.savepost_option_textposts:

                                                String CommentMessage = uploadCurrent2.getContent();
                                                String Date = uploadCurrent2.getDate();
                                                String userName = uploadCurrent2.getUser_name();
                                                String CommentKey = uploadCurrent2.getKey();
                                                String TheUID = uploadCurrent2.getUID();
                                                String PostKey = uploadCurrent2.getOldKey();

                                                DatabaseReference SaveComment = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("SavedComments");
                                                CommentStuffForTextPost commentStuffForTextPost = new CommentStuffForTextPost(CommentMessage, Date, userName, CommentKey, TheUID, PostKey);
                                                SaveComment.child(CommentKey).setValue(commentStuffForTextPost);

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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                else{

                    DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedComments");
                    CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(KeyComment)){

                                PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteComment);
                                popupMenu.inflate(R.menu.popup_menu_textposts_without_delete_withunsave);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){

                                            case R.id.reportpost_option_textposts:

                                                Intent intent= new Intent(mContext, Report_TextPost_Activity.class);
                                                intent.putExtra("Titel", uploadCurrent2.getContent());
                                                intent.putExtra("User", uploadCurrent2.getUser_name());
                                                intent.putExtra("Key", uploadCurrent2.getKey());
                                                intent.putExtra("Soort", "comment");
                                                mContext.startActivity(intent);

                                                break;

                                            case R.id.unsavepost_option_textposts:

                                                final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                SaveThePost.child("SavedComments").child(KeyComment).removeValue();

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
                                popupMenu.inflate(R.menu.popup_menu_textposts_without_delete);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){

                                            case R.id.reportpost_option_textposts:

                                                Intent intent= new Intent(mContext, Report_TextPost_Activity.class);
                                                intent.putExtra("Titel", uploadCurrent2.getContent());
                                                intent.putExtra("User", uploadCurrent2.getUser_name());
                                                intent.putExtra("Key", uploadCurrent2.getKey());
                                                intent.putExtra("Soort", "comment");
                                                mContext.startActivity(intent);

                                                break;

                                            case R.id.savepost_option_textposts:

                                                String CommentMessage = uploadCurrent2.getContent();
                                                String Date = uploadCurrent2.getDate();
                                                String userName = uploadCurrent2.getUser_name();
                                                String CommentKey = uploadCurrent2.getKey();
                                                String TheUID = uploadCurrent2.getUID();
                                                String PostKey = uploadCurrent2.getOldKey();

                                                DatabaseReference SaveComment = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("SavedComments");
                                                CommentStuffForTextPost commentStuffForTextPost = new CommentStuffForTextPost(CommentMessage, Date, userName, CommentKey, TheUID, PostKey);
                                                SaveComment.child(CommentKey).setValue(commentStuffForTextPost);

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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Dislikes");
        LikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeCountAdapter = (int) dataSnapshot.getChildrenCount();

                DatabaseReference SetLikeCount = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2);
                SetLikeCount.child("LikeCount").setValue(LikeCountAdapter);

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

                DatabaseReference SetDislikeCount = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2);
                SetDislikeCount.child("DislikeCount").setValue(DislikeCountAdapter);

                holder.DislikeCount.setText("" + DislikeCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String MyUID = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Likes");
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

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("General_Posts").child(KeyOGPosts2).child("Comments").child(KeyComments2).child("Dislikes");
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
