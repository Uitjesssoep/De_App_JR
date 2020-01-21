package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Textposts.PostStuffForText;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<PostStuffForText> mList;
    public Context mContext;
    private String KeyPost, key, TAG = "Check";
    private Query databaseReference;

    private GeneralAdapter.OnItemClickListener mListener;
    private String ContentPost;

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
       // Log.e(TAG, "GeneralAdapter");
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getItemViewType(final int position) {
       // Log.e("Check", "Tot getitemviewtype gekomen");
        PostStuffForText post = mList.get(position);
        KeyPost = post.getKey();
        ContentPost = post.getContent();
        //Log.e("CheckNeutral", ContentPost );
        if (ContentPost.contains("firebasestorage.googleapis.com")) {
            return 1;
        }
        return 2;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (viewType == 1) {
            //Log.e(TAG, "onCreateViewHolder images");
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
            return new ImagePostViewHolder(view, mListener);
        }else {//Log.e(TAG, "onCreateViewHolder text");
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_post_item_layout, parent, false);
        return new TextPostViewHolder(v, mListener);}

    }

   @Override
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
    }

    @Override
    public int getItemCount() {
     //  Log.e(TAG, "GetItemCount");
        return mList.size();


    }

    private class TextPostViewHolder extends RecyclerView.ViewHolder {
        public TextView Username, LikeCount, DislikeCount, CommentCount, Title, Content, KeyHolder, Date;
        public ImageButton Upvote, Downvote, DeleteTextPost;

        public TextPostViewHolder(View itemView, final GeneralAdapter.OnItemClickListener listener) {

            super(itemView);
         //   Log.e(TAG, "TextPostViewHolder: ");
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
    }


    private class ImagePostViewHolder extends RecyclerView.ViewHolder {
        public TextView TitleImage, UsernameImage, DateImage, LikeCountImage, DislikeCountImage, CommentCountImage;
        public ImageView imageView;
        public ImageButton UpvoteImage, DownvoteImage;

        public ImagePostViewHolder(View itemview, final GeneralAdapter.OnItemClickListener listener) {
            super(itemview);
           // Log.e(TAG, "ImagePostViewHolder: ");
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

    }
}
