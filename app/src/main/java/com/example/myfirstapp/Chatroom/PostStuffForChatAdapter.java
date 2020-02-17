package com.example.myfirstapp.Chatroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Report_TextPost_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PostStuffForChatAdapter extends RecyclerView.Adapter<PostStuffForChatAdapter.ViewHolder> {

    public Context mContext;
    public List<PostStuffForChat> mPost;
    public int LikeCountAdapter, DislikeCountAdapter;

    private PostStuffForChatAdapter.OnItemClickListener mListener;
    private String Date;
    private Calendar cal;
    private SimpleDateFormat dateFormat;

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

    public PostStuffForChatAdapter(Context mContext, List<PostStuffForChat> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public PostStuffForChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new PostStuffForChatAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostStuffForChatAdapter.ViewHolder holder, int position) {
        final PostStuffForChat uploadCurrent = mPost.get(position);
        holder.Username.setText(uploadCurrent.getUser_name());
        holder.Title.setText(uploadCurrent.getTitle());
        holder.KeyHolder.setText(uploadCurrent.getKey());

        cal = Calendar.getInstance();
        cal.setTimeInMillis(uploadCurrent.getDate());
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
        Date = dateFormat.format(cal.getTime());

        StringBuilder str = new StringBuilder(Date);
        str.replace(5, 12, "");
        str.replace(11, 16, "");
        str.replace(8, 9, "-");
        String mDate = str.toString();

        holder.Date.setText(mDate);
        holder.CommentCount.setVisibility(View.GONE);
        holder.CommentLogo.setVisibility(View.GONE);
        holder.Content.setVisibility(View.GONE);
        holder.IV.setVisibility(View.GONE);

       /* SpannableString spannableString = new SpannableString(uploadCurrent.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), 1,3,0);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(mContext, "Geklikt", Toast.LENGTH_SHORT);
            }
        };
        spannableString.setSpan(clickableSpan, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.Title.setText(spannableString);
        holder.Title.setMovementMethod(LinkMovementMethod.getInstance());*/
        holder.DeleteTextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gaan kijken of post van jou is om te kijken of ie delete icon moet laten zien:

                final String KeyPost = uploadCurrent.getKey().toString();
                DatabaseReference DeleteIconCheck = FirebaseDatabase.getInstance().getReference("Chatrooms");
                DeleteIconCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(KeyPost)) {

                            final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                            final String PostUIDCheck = dataSnapshot.child(KeyPost).child("uid").getValue().toString();

                            if (MyUIDCheck.equals(PostUIDCheck)) {

                                DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedChatrooms");
                                CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(KeyPost)) {

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_withunsave);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()) {

                                                        case R.id.delete_option_textposts:

                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                            dialog.setTitle("Delete your chatroom?");
                                                            dialog.setMessage("Deleting this chatroom cannot be undone! Are you sure you want to delete it?");

                                                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyPost);
                                                                    DeleteThePost.removeValue();

                                                                    Intent intent = new Intent(mContext, Layout_Manager_BottomNav_Activity.class);
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

                                                        case R.id.edit_option_textposts:

                                                            break;


                                                        case R.id.unsavepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            SaveThePost.child("SavedChatrooms").child(KeyPost).removeValue();

                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                    return false;
                                                }
                                            });
                                            popupMenu.show();

                                        } else {

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()) {

                                                        case R.id.delete_option_textposts:

                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                                            dialog.setTitle("Delete your chatroom?");
                                                            dialog.setMessage("Deleting this chatroom cannot be undone! Are you sure you want to delete it?");

                                                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyPost);
                                                                    DeleteThePost.removeValue();

                                                                    Intent intent = new Intent(mContext, Layout_Manager_BottomNav_Activity.class);
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

                                                        case R.id.edit_option_textposts:

                                                            break;


                                                        case R.id.savepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                            SaveThePost.child("SavedChatrooms").child(KeyPost).setValue("added");

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
                            } else {

                                DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedChatrooms");
                                CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(uploadCurrent.getKey())) {

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_without_delete_withunsave);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()) {

                                                        case R.id.reportpost_option_textposts:

                                                            Intent intent = new Intent(mContext, Report_TextPost_Activity.class);
                                                            intent.putExtra("Titel", uploadCurrent.getTitle());
                                                            intent.putExtra("User", uploadCurrent.getUser_name());
                                                            intent.putExtra("Key", uploadCurrent.getKey());
                                                            intent.putExtra("Soort", "chatroom");
                                                            mContext.startActivity(intent);

                                                            break;

                                                        case R.id.unsavepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            SaveThePost.child("SavedChatrooms").child(KeyPost).removeValue();

                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                    return false;
                                                }
                                            });
                                            popupMenu.show();

                                        } else {

                                            PopupMenu popupMenu = new PopupMenu(mContext, holder.DeleteTextPost);
                                            popupMenu.inflate(R.menu.popup_menu_textposts_without_delete);
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {

                                                    switch (menuItem.getItemId()) {

                                                        case R.id.reportpost_option_textposts:

                                                            Intent intent = new Intent(mContext, Report_TextPost_Activity.class);
                                                            intent.putExtra("Titel", uploadCurrent.getTitle());
                                                            intent.putExtra("User", uploadCurrent.getUser_name());
                                                            intent.putExtra("Key", uploadCurrent.getKey());
                                                            intent.putExtra("Soort", "chatroom");
                                                            mContext.startActivity(intent);

                                                            break;

                                                        case R.id.savepost_option_textposts:

                                                            final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            SaveThePost.child("SavedChatrooms").child(KeyPost).setValue("added");

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

                        } else {
                            //post is gedelete als het goed is
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        final String KeyYeah = uploadCurrent.getKey().toString();

        final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Chatrooms");
        final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("Chatrooms");
        LikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(KeyYeah)) {
                    LikeCountAdapter = (int) dataSnapshot.child(KeyYeah).child("Likes").getChildrenCount();

                    DatabaseReference SetLikeCount = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah);
                    SetLikeCount.child("LikeCount").setValue(LikeCountAdapter);

                    holder.LikeCount.setText("" + LikeCountAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DislikeCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(KeyYeah)) {
                    DislikeCountAdapter = (int) dataSnapshot.child(KeyYeah).child("Dislikes").getChildrenCount();

                    DatabaseReference SetDislikeCount = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah);
                    SetDislikeCount.child("DislikeCount").setValue(DislikeCountAdapter);

                    holder.DislikeCount.setText("" + DislikeCountAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //kijken of de user deleted is
        final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("user_name");
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
        });

        //kijken als user anonymous is dat de username voor de anon zelf wel zichtbaar is

        String MyUID2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        }

        final String MyUID = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Likes");
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

        DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("Chatrooms").child(KeyYeah).child("Dislikes");
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

        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;
        public ImageView CommentLogo;
        public ImageView IV;

        public ViewHolder(@NonNull View itemView, final PostStuffForChatAdapter.OnItemClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tvUsernameTextPostItem);
            LikeCount = itemView.findViewById(R.id.tvLikeCounterTextPostItem);
            DislikeCount = itemView.findViewById(R.id.tvDislikeCounterTextPostItem);
            CommentCount = itemView.findViewById(R.id.tvCommentCountTextPostItem);
            Title = itemView.findViewById(R.id.tvTitleTextPostItem);
            KeyHolder = itemView.findViewById(R.id.tvKeyHiddenTextPostItem);
            Date = itemView.findViewById(R.id.tvPostDateTextPostItem);
            Upvote = itemView.findViewById(R.id.ibLikeUpTextPostItem);
            Downvote = itemView.findViewById(R.id.ibLikeDownTextPostItem);
            DeleteTextPost = itemView.findViewById(R.id.ibDeleteIconTextPostItem);
            CommentLogo = itemView.findViewById(R.id.ivCommentImageTextPostItem);
            Content = itemView.findViewById(R.id.tvContentTextPostItem);
            IV = itemView.findViewById(R.id.ivImagePostItem);

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
