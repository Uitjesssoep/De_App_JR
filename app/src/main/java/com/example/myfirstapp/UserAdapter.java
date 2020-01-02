package com.example.myfirstapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private List<Users> list;

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
        holder.Username.setText(users.getUsernameFollow());
        Picasso.get()
                //.load("https://firebasestorage.googleapis.com/v0/b/de-app-b0fb9.appspot.com/o/7yTA9yX4fiTGw5oHInp7rcPIDHF2%2FImages%2FProfilePicture?alt=media&token=e90ad8a4-4815-4543-b7f2-dc258dcdf4c9")
                .load(users.getProfilePicture())
                .placeholder(R.drawable.app_logo_200)
                .fit()
                .centerCrop()
                .into(holder.ProfilePicture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView Username;
        public ImageView ProfilePicture;
        public Button Follow;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.tvUser_name);
            ProfilePicture = itemView.findViewById(R.id.ivProfilePictureUserList);
            Follow = itemView.findViewById(R.id.btFollow);

        }
    }

}