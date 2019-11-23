package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Upload> mUploads;
    private Context mContext;

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
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.app_logo_200)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.tvname);
            imageView = itemView.findViewById(R.id.ivUpload);
        }
    }
}
