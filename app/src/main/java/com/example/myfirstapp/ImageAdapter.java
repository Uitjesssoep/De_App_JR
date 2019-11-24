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
      //  Picasso picasso = Picasso.get();
       // Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
      //  Picasso picasso = picassoBuilder.build();
        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/de-app-b0fb9.appspot.com/o/7yTA9yX4fiTGw5oHInp7rcPIDHF2%2FGeneral_Image_Posts%2F1574592477935.jpg?alt=media&token=4b3950d3-f9eb-4090-8589-c7e3c80b6a62")
                //.load(uploadCurrent.getImageUrl())
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

            textViewName = itemView.findViewById(R.id.tvName);
            imageView = itemView.findViewById(R.id.ivUpload);
        }
    }
}
