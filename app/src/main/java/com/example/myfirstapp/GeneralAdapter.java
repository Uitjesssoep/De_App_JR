package com.example.myfirstapp;

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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.example.myfirstapp.Textposts.PostStuffForText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<PostStuffForText> mList;
    public Context mContext;
    private String KeyPost, key;
    private Query databaseReference;
    public int CommentCountAdapter, LikeCountAdapter, DislikeCountAdapter;

    private GeneralAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUserNameClick(int position);
        void onUpvoteClick(int position);
        void onDownvoteClick(int position);}

    public GeneralAdapter(Context mContext, List<PostStuffForText> mList){
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        PostStuffForText post = mList.get(position);
        KeyPost = post.getKey();
        databaseReference = FirebaseDatabase.getInstance().getReference().orderByChild("key").equalTo(KeyPost);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    Log.e("Check", key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (key.equals("General_Image_Posts")) {
            return 1;
        } else {
            return 3;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
            return new ImagePostViewHolder(v, mListener);
                   }
        else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
            return new TextPostViewHolder(v, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImagePostViewHolder){
            ((ImagePostViewHolder) holder).bind(mList.get(position));
        }else {

            ((TextPostViewHolder) holder).bind(mList.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return 0;
    }

   private class TextPostViewHolder extends RecyclerView.ViewHolder {
       public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date;
       public ImageButton Upvote, Downvote, DeleteTextPost;

       public TextPostViewHolder(View itemView, final GeneralAdapter.OnItemClickListener listener) {
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

       public void bind(final PostStuffForText textPost) {
           Username.setText(textPost.getUser_name());
           Title.setText(textPost.getTitle());
           KeyHolder.setText(textPost.getKey());
           Content.setText(textPost.getContent());
           Date.setText(textPost.getDate());

           String ContentTemp = Content.getText().toString();
           if (ContentTemp.length() > 147) {
               ContentTemp = ContentTemp.substring(0, ContentTemp.length() - 3);
               Content.setText(ContentTemp.trim() + "...");
           }

           DeleteTextPost.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   //gaan kijken of post van jou is om te kijken of ie delete icon moet laten zien:

                   final String KeyPost = textPost.getKey().toString();
                   final DatabaseReference DeleteIconCheck = FirebaseDatabase.getInstance().getReference("General_Text_Posts");

                   DeleteIconCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           if (dataSnapshot.hasChild(KeyPost)) {

                               final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                               final String PostUIDCheck = dataSnapshot.child(KeyPost).child("uid").getValue().toString();

                               if (MyUIDCheck.equals(PostUIDCheck)) {

                                   DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedPosts");
                                   CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                           if (dataSnapshot.hasChild(KeyPost)) {

                                               PopupMenu popupMenu = new PopupMenu(mContext, DeleteTextPost);
                                               popupMenu.inflate(R.menu.popup_menu_textposts_withunsave);
                                               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   @Override
                                                   public boolean onMenuItemClick(MenuItem menuItem) {

                                                       switch (menuItem.getItemId()) {

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

                                           } else {

                                               PopupMenu popupMenu = new PopupMenu(mContext, DeleteTextPost);
                                               popupMenu.inflate(R.menu.popup_menu_textposts);
                                               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   @Override
                                                   public boolean onMenuItemClick(MenuItem menuItem) {

                                                       switch (menuItem.getItemId()) {

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
                               } else {

                                   DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedPosts");
                                   CheckIfSaved.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                           if (dataSnapshot.hasChild(textPost.getKey())) {

                                               PopupMenu popupMenu = new PopupMenu(mContext, DeleteTextPost);
                                               popupMenu.inflate(R.menu.popup_menu_textposts_without_delete_withunsave);
                                               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   @Override
                                                   public boolean onMenuItemClick(MenuItem menuItem) {

                                                       switch (menuItem.getItemId()) {

                                                           case R.id.reportpost_option_textposts:

                                                               Intent intent = new Intent(mContext, Report_TextPost_Activity.class);
                                                               intent.putExtra("Titel", textPost.getTitle());
                                                               intent.putExtra("User", textPost.getUser_name());
                                                               intent.putExtra("Key", textPost.getKey());
                                                               intent.putExtra("Soort", "post");
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

                                           } else {

                                               PopupMenu popupMenu = new PopupMenu(mContext, DeleteTextPost);
                                               popupMenu.inflate(R.menu.popup_menu_textposts_without_delete);
                                               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   @Override
                                                   public boolean onMenuItemClick(MenuItem menuItem) {

                                                       switch (menuItem.getItemId()) {

                                                           case R.id.reportpost_option_textposts:

                                                               Intent intent = new Intent(mContext, Report_TextPost_Activity.class);
                                                               intent.putExtra("Titel", textPost.getTitle());
                                                               intent.putExtra("User", textPost.getUser_name());
                                                               intent.putExtra("Key", textPost.getKey());
                                                               intent.putExtra("Soort", "post");
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

           String KeyYeah = textPost.getKey().toString();
           final DatabaseReference CommentCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Comments");
           CommentCountInAdapter.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   CommentCountAdapter = (int) dataSnapshot.getChildrenCount();
                   CommentCount.setText("" + CommentCountAdapter);
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
                   LikeCount.setText("" + LikeCountAdapter);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
           DislikeCountInAdapter.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   DislikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                   DislikeCount.setText("" + DislikeCountAdapter);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });


           //kijken of de user deleted is
           final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
           final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("user_name");
           final String PostUID = textPost.getUID().toString();

           UserUIDCheck.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   if (dataSnapshot.hasChild(PostUID)) {

                   } else {

                       ChangeUsername.setValue("[deleted_user]");
                       Username.setText("[deleted_user]");

                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

           //kijken als user anonymous is dat de username voor de anon zelf wel zichtbaar is

           String MyUID2 = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
           String PostUID2 = textPost.getUID().toString();

           if (MyUID2.equals(PostUID2)) {

               DatabaseReference GetUsername = FirebaseDatabase.getInstance().getReference("users").child(MyUID2).child("userName");
               GetUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       final String MyUserName = dataSnapshot.getValue().toString();

                       Username.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                       Username.setText(MyUserName);

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

           } else {

           }


           //Like image set

           final String MyUID = FirebaseAuth.getInstance().getUid().toString();
           DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Likes");
           CheckIfUpvoted.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   if (dataSnapshot.hasChild(MyUID)) {
                       Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_green_24dp);
                   } else {
                       Upvote.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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

                   if (dataSnapshot.hasChild(MyUID)) {
                       Downvote.setImageResource(R.drawable.ic_keyboard_arrow_down_green_24dp);
                   } else {
                       Downvote.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });


       }
   }




    private class ImagePostViewHolder extends RecyclerView.ViewHolder{
        public TextView TitleImage, UsernameImage, DateImage, LikeCountImage, DislikeCountImage, CommentCountImage ;
        public ImageView imageView;
        public ImageButton UpvoteImage, DownvoteImage;

        public ImagePostViewHolder (View itemview, final GeneralAdapter.OnItemClickListener listener){
            super (itemview);
            TitleImage = itemView.findViewById(R.id.tvTitleImageItem);
            UsernameImage = itemView.findViewById(R.id.tvUsernameImageItem);
            DateImage = itemView.findViewById(R.id.tvDateImageItem);
            imageView = itemView.findViewById(R.id.ivUpload);
            LikeCountImage = itemView.findViewById(R.id.tvLikeCounterImageItem2);
            DislikeCountImage = itemView.findViewById(R.id.tvDislikeCounterImageItem);
            CommentCountImage = itemView.findViewById(R.id.tvCommentCountImageItem);
            UpvoteImage = itemView.findViewById(R.id.ibLikeUpImageItem);
            DownvoteImage = itemView.findViewById(R.id.ibLikeDownImageItem);

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

            UsernameImage.setOnClickListener(new View.OnClickListener() {
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

            UpvoteImage.setOnClickListener(new View.OnClickListener() {
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

            DownvoteImage.setOnClickListener(new View.OnClickListener() {
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
        public void bind(PostStuffForText imagePost){
            TitleImage.setText(imagePost.getTitle());
            UsernameImage.setText(imagePost.getUser_name());
            DateImage.setText(imagePost.getDate());
            Picasso.get()
                    .load(imagePost.getContent())
                    .fit()
                    .centerCrop()
                    .into(imageView);

            String KeyYeah = imagePost.getKey().toString();
            final DatabaseReference UserUIDCheck = FirebaseDatabase.getInstance().getReference("users");
            final DatabaseReference ChangeUsername = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(KeyYeah).child("user_name");
            final String PostUID = imagePost.getUID().toString();
            final DatabaseReference LikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(KeyYeah).child("Likes");
            final DatabaseReference DislikeCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(KeyYeah).child("Dislikes");
            LikeCountInAdapter.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                    LikeCountImage.setText("" + LikeCountAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DislikeCountInAdapter.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DislikeCountAdapter = (int) dataSnapshot.getChildrenCount();
                    DislikeCountImage.setText("" + DislikeCountAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            final String MyUID = FirebaseAuth.getInstance().getUid().toString();
            DatabaseReference CheckIfUpvoted = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(KeyYeah).child("Likes");
            CheckIfUpvoted.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(MyUID)){
                        UpvoteImage.setImageResource(R.drawable.pijl_omhoog_geklikt);
                    }
                    else{
                        UpvoteImage.setImageResource(R.drawable.pijl_omhoog_neutraal);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference CheckIfDownvoted = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(KeyYeah).child("Dislikes");
            CheckIfDownvoted.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(MyUID)){
                        DownvoteImage.setImageResource(R.drawable.pijl_omlaag_geklikt);
                    }
                    else{
                        DownvoteImage.setImageResource(R.drawable.pijl_omlaag_neutraal);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            UserUIDCheck.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(PostUID)){

                    }

                    else{

                        ChangeUsername.setValue("[deleted_user]");
                        UsernameImage.setText("[deleted_user]");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



}
