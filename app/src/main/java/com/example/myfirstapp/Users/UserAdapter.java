package com.example.myfirstapp.Users;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private List<Users> list;
    public String UIDString;

    public UserAdapter(Context context, List list) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.users_list, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users users = list.get(position);
        holder.Username.setText(users.getUsername());
       // holder.UIDhidden.setText(users.getTheUID());
      //  UIDString = users.getTheUID();
        Picasso.get()
                .load(users.getProfilePicture())
                .fit()
                .centerCrop()
                .into(holder.ProfilePicture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView Username, UIDhidden;
        public ImageView ProfilePicture;
        public Button Follow;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.tvUser_name);
            UIDhidden = itemView.findViewById(R.id.tvUID);
            ProfilePicture = itemView.findViewById(R.id.ivProfilePictureUserList);
            Follow = itemView.findViewById(R.id.btFollow);

        }
    }

}