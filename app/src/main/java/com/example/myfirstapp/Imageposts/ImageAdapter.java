package com.example.myfirstapp.Imageposts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Upload> mUploads;
    private Context mContext;
    public int CommentCountAdapter, LikeCountAdapter, DislikeCountAdapter;

    public ImageAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.TitleImage.setText(uploadCurrent.getTitle());
        holder.UsernameImage.setText(uploadCurrent.getUser_name());
        holder.DateImage.setText(uploadCurrent.getDate());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.app_logo_200)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        String KeyYeah = uploadCurrent.getKey().toString();

       /* final DatabaseReference CommentCountInAdapter = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(KeyYeah).child("Comments");
        CommentCountInAdapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CommentCountAdapter = (int) dataSnapshot.getChildrenCount();
                holder.CommentCount.setText("Number of comments: " + CommentCountAdapter);
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
        });*/

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView TitleImage, UsernameImage, DateImage, LikeCountImage, DislikeCountImage, CommentCountImage ;
        public ImageView imageView;
        public ImageButton UpvoteImage, DownvoteImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            TitleImage = itemView.findViewById(R.id.tvTitleImageItem);
            UsernameImage = itemView.findViewById(R.id.tvUsernameImageItem);
            DateImage = itemView.findViewById(R.id.tvDateImageItem);
            imageView = itemView.findViewById(R.id.ivUpload);
            LikeCountImage = itemView.findViewById(R.id.tvLikeCounterImageItem2);
            DislikeCountImage = itemView.findViewById(R.id.tvDislikeCounterImageItem);
            CommentCountImage = itemView.findViewById(R.id.tvCommentCountImageItem);
            UpvoteImage = itemView.findViewById(R.id.ibLikeUpImageItem);
            DownvoteImage = itemView.findViewById(R.id.ibLikeDownImageItem);

        }
    }
}
