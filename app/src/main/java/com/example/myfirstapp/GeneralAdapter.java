package com.example.myfirstapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Textposts.PostStuffForText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<PostStuffForText> mList;
    public Context mContext;
    private String KeyPost, key, TAG = "Check";
    private Query databaseReference;

    private GeneralAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onUserNameClick(int position);

        void onUpvoteClick(int position);

        void onDownvoteClick(int position);
    }

    public void setOnItemClickListener(GeneralAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public GeneralAdapter(Context mContext, List<PostStuffForText> mList) {
        Log.e(TAG, "GeneralAdapter");
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getItemViewType(final int position) {
        Log.e("Check", "Tot getitemviewtype gekomen");
        PostStuffForText post = mList.get(position);
        KeyPost = post.getKey();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("General_Image_Posts").child(KeyPost).child("title");
        //  databaseReference = FirebaseDatabase.getInstance().getReference().orderByChild("key").equalTo(KeyPost);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getValue().toString();
                    Log.e("Check", key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // if (key.equals("image")) {
       //     return 1;
      //  }
        return 2;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            Log.e(TAG, "onCreateViewHolder images");
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
            return new ImagePostViewHolder(view, mListener);
        }
        Log.e(TAG, "onCreateViewHolder text");
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new TextPostViewHolder(v, mListener);



        /*switch (viewType) {
            case 1:
                Log.e(TAG, "onCreateViewHolder images");
                View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
                return new ImagePostViewHolder(view, mListener);
            case 2:
                Log.e(TAG, "onCreateViewHolder text");
                View v = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
                return new TextPostViewHolder(v, mListener);
            default: throw new IllegalArgumentException();
        }*/
    }

 /*   @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 1: {
                ImagePostViewHolder viewHolder = (ImagePostViewHolder) holder;
                PostStuffForText item = mList.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.TitleImage.setText(item.getTitle());
                viewHolder.UsernameImage.setText(item.getUser_name());
                viewHolder.DateImage.setText(item.getDate());
                Picasso.get()
                        .load(item.getContent())
                        .fit()
                        .centerCrop()
                        .into(viewHolder.imageView);
            }
            break;
            case 2: {
                TextPostViewHolder viewHolder = (TextPostViewHolder) holder;
                PostStuffForText item = mList.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.Username.setText(item.getUser_name());
                viewHolder.Title.setText(item.getTitle());
                viewHolder.KeyHolder.setText(item.getKey());
                viewHolder.Content.setText(item.getContent());
                viewHolder.Date.setText(item.getDate());
            }
            break;
            default:
                break;
        }
    }*/


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder invoked");
        PostStuffForText post = mList.get(position);
        KeyPost = post.getKey();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("General_Text_Posts");
        //  databaseReference = FirebaseDatabase.getInstance().getReference().orderByChild("key").equalTo(KeyPost);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    Log.e("Check", key);
                    if(key.equals("General_Image_Posts")){
                        ImagePostViewHolder viewHolder = (ImagePostViewHolder) holder;
                        PostStuffForText item = mList.get(position);
                        viewHolder.setIsRecyclable(false);
                        viewHolder.TitleImage.setText(item.getTitle());
                        viewHolder.UsernameImage.setText(item.getUser_name());
                        viewHolder.DateImage.setText(item.getDate());
                        Picasso.get()
                                .load(item.getContent())
                                .fit()
                                .centerCrop()
                                .into(viewHolder.imageView);
                    }else{
                        TextPostViewHolder viewHolder1 = (TextPostViewHolder) holder;
                        PostStuffForText item1 = mList.get(position);
                        viewHolder1.setIsRecyclable(false);
                        viewHolder1.Username.setText(item1.getUser_name());
                        viewHolder1.Title.setText(item1.getTitle());
                        viewHolder1.KeyHolder.setText(item1.getKey());
                        viewHolder1.Content.setText(item1.getContent());
                        viewHolder1.Date.setText(item1.getDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });





     /*   Log.e(TAG, "onBindViewHolder invoked");
        switch (getItemViewType(position)) {
            case 1:
                ImagePostViewHolder viewHolder = (ImagePostViewHolder) holder;
                PostStuffForText item = mList.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.TitleImage.setText(item.getTitle());
                viewHolder.UsernameImage.setText(item.getUser_name());
                viewHolder.DateImage.setText(item.getDate());
                Picasso.get()
                        .load(item.getContent())
                        .fit()
                        .centerCrop()
                        .into(viewHolder.imageView);
                break;
            case 2:
                TextPostViewHolder viewHolder1 = (TextPostViewHolder) holder;
                PostStuffForText item1 = mList.get(position);
                viewHolder1.setIsRecyclable(false);
                viewHolder1.Username.setText(item1.getUser_name());
                viewHolder1.Title.setText(item1.getTitle());
                viewHolder1.KeyHolder.setText(item1.getKey());
                viewHolder1.Content.setText(item1.getContent());
                viewHolder1.Date.setText(item1.getDate());
        }

       if (holder instanceof ImagePostViewHolder) {
            ImagePostViewHolder viewHolder = (ImagePostViewHolder) holder;
            PostStuffForText item = mList.get(position);
            viewHolder.setIsRecyclable(false);
            viewHolder.TitleImage.setText(item.getTitle());
            viewHolder.UsernameImage.setText(item.getUser_name());
            viewHolder.DateImage.setText(item.getDate());
            Picasso.get()
                    .load(item.getContent())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.imageView);
        } else {
            TextPostViewHolder viewHolder = (TextPostViewHolder) holder;
            PostStuffForText item = mList.get(position);
            viewHolder.setIsRecyclable(false);
            viewHolder.Username.setText(item.getUser_name());
            viewHolder.Title.setText(item.getTitle());
            viewHolder.KeyHolder.setText(item.getKey());
            viewHolder.Content.setText(item.getContent());
            viewHolder.Date.setText(item.getDate());
        }*/
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "GetItemCount");
        return mList.size();


    }

    private class TextPostViewHolder extends RecyclerView.ViewHolder {
        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;

        public TextPostViewHolder(View itemView, final GeneralAdapter.OnItemClickListener listener) {

            super(itemView);
            Log.e(TAG, "TextPostViewHolder: ");
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

        public void bind(PostStuffForText textPost) {
            Username.setText(textPost.getUser_name());
            Title.setText(textPost.getTitle());
            KeyHolder.setText(textPost.getKey());
            Content.setText(textPost.getContent());
            Date.setText(textPost.getDate());

        }
    }


    private class ImagePostViewHolder extends RecyclerView.ViewHolder {
        public TextView TitleImage, UsernameImage, DateImage, LikeCountImage, DislikeCountImage, CommentCountImage;
        public ImageView imageView;
        public ImageButton UpvoteImage, DownvoteImage;

        public ImagePostViewHolder(View itemview, final GeneralAdapter.OnItemClickListener listener) {
            super(itemview);
            Log.e(TAG, "ImagePostViewHolder: ");
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
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            UsernameImage.setOnClickListener(new View.OnClickListener() {
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

            UpvoteImage.setOnClickListener(new View.OnClickListener() {
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

            DownvoteImage.setOnClickListener(new View.OnClickListener() {
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

        public void bind(PostStuffForText imagePost) {
            TitleImage.setText(imagePost.getTitle());
            UsernameImage.setText(imagePost.getUser_name());
            DateImage.setText(imagePost.getDate());
            Picasso.get()
                    .load(imagePost.getContent())
                    .fit()
                    .centerCrop()
                    .into(imageView);

        }
    }
}
