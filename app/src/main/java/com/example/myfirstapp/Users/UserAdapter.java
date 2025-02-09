package com.example.myfirstapp.Users;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    private Context mContext;
    private List<UserProfileToDatabase> list;
    private List<UserProfileToDatabase> listFull;
    public String UIDString;
    private boolean Checkje = true;
    private String UsernameToFollow, UIDToFollow, userNameFollower, UIDFollower, UsernameToFollow2, UIDToFollow2;
    private String TAGTEST = "Check";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ;
    private String MyUID = firebaseAuth.getUid();
    private DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");
    private DatabaseReference datarefFollower = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("userName");
    private DatabaseReference datarefFollowing = FirebaseDatabase.getInstance().getReference().child("users");

    private UserAdapter.OnItemClickListener mListener;


    private Filter listFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserProfileToDatabase> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UserProfileToDatabase user : listFull) {
                    if (user.getUserName().toLowerCase().contains(filterPattern) || user.getUserFullName().toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();


        }
    };

    @Override
    public Filter getFilter() {
        return listFilter;
    }



    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUserNameClick(int position);
        void onProfilePictureClick(int position);
        void onFollowClick(int position);
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener){
        mListener = listener;
    }
    public UserAdapter(Context context, List list) {
        this.list = list;
        listFull = new ArrayList<>(list);
        mContext = context;

    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.users_list, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view, mListener);
        return userViewHolder;
    }
//l
    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        UserProfileToDatabase users = list.get(position);
     /*   if (users.getTheUID().equals(MyUID)){
            delete(holder.getAdapterPosition());
        }
        else {*/
        holder.Username.setText(users.getUserName());
        holder.DisplayName.setText(users.getUserFullName());
        final String UIDToFollow2 = users.getTheUID();
        //UIDString = users.getTheUID();
        Log.e(TAGTEST, users.getUserName());
        Log.e(TAGTEST, users.getProfilePicture());
        Picasso.get()
                .load(users.getProfilePicture())
                .placeholder(R.drawable.neutral_profile_picture_nobackground)
                .fit()
                .centerCrop()
                .into(holder.ProfilePicture);


        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsernameToFollow2 = holder.Username.getText().toString();

                if (dataSnapshot.hasChild(UIDToFollow2)) {
                    holder.FollowButton.setBackgroundResource(R.drawable.button_roundedcorners_following);
                    holder.FollowButton.setText("Following");
                    holder.FollowButton.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                }
                else {
                    holder.FollowButton.setText("Follow");
                    holder.FollowButton.setBackgroundResource(R.drawable.button_roundedcorners_follow);
                    holder.FollowButton.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


     /*   holder.Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIDToFollow = holder.UIDhidden.getText().toString();
                UsernameToFollow = holder.Username.getText().toString();

                datarefFollower.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(UsernameToFollow)) {
                            Log.e(TAGTEST, "TRUEE" );
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle("Do you want to unfollow this user?");
                           // dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        //   holder.Follow.setEnabled(false);
                        } else {
                            userNameFollower = dataSnapshot.getValue().toString();
                            FollowersList followerslist = new FollowersList(userNameFollower, MyUID);

                            Log.e(TAGTEST, UIDToFollow);
                            Log.e(TAGTEST, userNameFollower);
                            FollowersList followingList = new FollowersList(UsernameToFollow, UIDToFollow);
                            datarefFollowing.child(MyUID).child("following").child(UsernameToFollow).setValue(followingList);
                            datarefFollowing
                                    .child(UIDToFollow)
                                    .child("followers")
                                    .child(userNameFollower)
                                    .setValue(followerslist);
                            Log.e(TAGTEST, userNameFollower);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });*/
        // }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView Username, DisplayName;
        public ImageView ProfilePicture;
        public Button FollowButton;


        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            Username = itemView.findViewById(R.id.tvUser_name);
            ProfilePicture = itemView.findViewById(R.id.ivProfilePictureUserList);
            FollowButton = itemView.findViewById(R.id.btnFollowUserListToFollow);
            DisplayName = itemView.findViewById(R.id.tvDisplay_Name);

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
            ProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onProfilePictureClick(position);
                        }
                    }
                }
            });
            FollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onFollowClick(position);
                        }
                    }
                }
            });
        }
    }



}