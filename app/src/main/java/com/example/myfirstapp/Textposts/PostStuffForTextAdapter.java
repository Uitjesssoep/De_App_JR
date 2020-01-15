package com.example.myfirstapp.Textposts;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.Report_TextPost_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostStuffForTextAdapter extends RecyclerView.Adapter<PostStuffForTextAdapter.ViewHolder>{

    public Context mContext;
    public List<PostStuffForText> mPost;
    public int CommentCountAdapter, LikeCountAdapter, DislikeCountAdapter;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUserNameClick(int position);
        void onUpvoteClick(int position);
        void onDownvoteClick(int position);
        //void onDeleteIconClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private FirebaseUser firebaseUser;

    public PostStuffForTextAdapter(Context mContext, List<PostStuffForText> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForTextAdapter.ViewHolder(view, mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PostStuffForText uploadCurrent = mPost.get(position);
        holder.Username.setText(uploadCurrent.getUser_name());
        holder.Title.setText(uploadCurrent.getTitle());
        holder.KeyHolder.setText(uploadCurrent.getKey());
        holder.Content.setText(uploadCurrent.getContent());
        holder.Date.setText(uploadCurrent.getDate());

        String ContentTemp = holder.Content.getText().toString();
        if(ContentTemp.length() > 147){
            ContentTemp = ContentTemp.substring(0, ContentTemp.length() - 3);
            holder.Content.setText(ContentTemp.trim() + "...");
        }

        holder.DeleteTextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gaan kijken of post van jou is om te kijken of ie delete icon moet laten zien:

                final String KeyPost = uploadCurrent.getKey().toString();
                final DatabaseReference DeleteIconCheck = FirebaseDatabase.getInstance().getReference("General_Text_Posts");

                DeleteIconCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(KeyPost)){

                            final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                            final String PostUIDCheck = dataSnapshot.child(KeyPost).child("uid").getValue().toString();

                            if(MyUIDCheck.equals(PostUIDCheck)){

                                DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedPosts");
                                CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(KeyPost)){

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_withunsave);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()){

                                                        case R.id.delete_option_textposts:

                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                            dialog.setTitle("Delete your post?");
                                                            dialog.setMessage("Deleting this post cannot be undone! Are you sure you want to delete it?");

                                                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                    final DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyPost);
                                                                    DeleteThePost.removeValue();

                                                                    Intent intent = new Intent(mContext, General_Feed_Activity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


                                                        case R.id.unsavepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                            SaveThePost.child("SavedPosts").child(KeyPost).removeValue();

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

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()){

                                                        case R.id.delete_option_textposts:

                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                            dialog.setTitle("Delete your post?");
                                                            dialog.setMessage("Deleting this post cannot be undone! Are you sure you want to delete it?");

                                                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                    final DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyPost);
                                                                    DeleteThePost.removeValue();

                                                                    Intent intent = new Intent(mContext, General_Feed_Activity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


                                                        case R.id.savepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                            SaveThePost.child("SavedPosts").child(KeyPost).setValue("added");

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

                                DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedPosts");
                                CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(uploadCurrent.getKey())){

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_without_delete_withunsave);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()){

                                                        case R.id.reportpost_option_textposts:

                                                            Intent intent= new Intent(mContext, Report_TextPost_Activity.class);
                                                            intent.putExtra("Titel", uploadCurrent.getTitle());
                                                            intent.putExtra("User", uploadCurrent.getUser_name());
                                                            intent.putExtra("Key", uploadCurrent.getKey());
                                                            mContext.startActivity(intent);

                                                            break;

                                                        case R.id.unsavepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                            SaveThePost.child("SavedPosts").child(KeyPost).removeValue();

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

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_without_delete);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()){

                                                        case R.id.reportpost_option_textposts:

                                                            Intent intent= new Intent(mContext, Report_TextPost_Activity.class);
                                                            intent.putExtra("Titel", uploadCurrent.getTitle());
                                                            intent.putExtra("User", uploadCurrent.getUser_name());
                                                            intent.putExtra("Key", uploadCurrent.getKey());
                                                            mContext.startActivity(intent);

                                                            break;

                                                        case R.id.savepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                            SaveThePost.child("SavedPosts").child(KeyPost).setValue("added");

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
                        else{
                            //post is gedelete als het goed is
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        String KeyYeah = uploadCurrent.getKey().toString();
        final DatabaseReference CommentCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Comments");
        CommentCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               CommentCountAdapter = (int) dataSnapshot.getChildrenCount();
               holder.CommentCount.setText("" + CommentCountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Likes");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Dislikes");
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
        final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("user_name");
        final String PostUID = uploadCurrent.getUID().toString();

        UserUIDCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(PostUID)){

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

        //kijken als user anonymous is dat de username voor de anon zelf wel zichtbaar is

        String MyUID2 = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        String PostUID2 = uploadCurrent.getUID().toString();

        if(MyUID2.equals(PostUID2)){

            DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(MyUID2).child("userName");
            GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String MyUserName = dataSnapshot.getValue().toString();

                    holder.Username.setText(MyUserName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{

        }


        final String MyUID = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Likes");
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

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Dislikes");
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
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById (R.id.tvUsernameTextPostItem);
            LikeCount = itemView.findViewById (R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById (R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById (R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById (R.id.tvTitleTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);
            Content = itemView.findViewById(R.id.tvContentTextPostItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
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

            /*DeleteTextPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteIconClick(position);
                        }
                    }
                }
            });*/

        }

    }

}
