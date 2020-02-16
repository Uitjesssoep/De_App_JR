package com.example.myfirstapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Chat;
import com.example.myfirstapp.Chatroom.Chat_With_Users_Activity;
import com.example.myfirstapp.Chatroom.PostStuffForChat;
import com.example.myfirstapp.Chatroom.PostStuffForChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class AllChatsTab extends Fragment {

    private List<PostStuffForChat> postStuffForChatList;
    private RecyclerView RoomList;
    private PostStuffForChatAdapter postStuffForChatAdapter;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    private LinearLayoutManager linearLayoutManager; //voor sorteren
    private SharedPreferences sharedPreferences; //saven sorteer setting
    private TextView SortCommentsBy;
    private ImageView SortByCommentsIV;

    public AllChatsTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPrefNightMode sharedPrefNightMode = new SharedPrefNightMode(getActivity());

        if(sharedPrefNightMode.loadNightModeState()==true){
            getContext().setTheme(R.style.AppTheme_Night);
        }
        else getContext().setTheme(R.style.AppTheme);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_chats_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CheckInternet();
        StartOrReloadChatRooms();

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipe_container_AllChats_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StartOrReloadChatRooms();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private void StartOrReloadChatRooms() {

        SortCommentsBy = getView().findViewById(R.id.tvSortByTextAllChats);
        SortByCommentsIV = getView().findViewById(R.id.ivSortByAllChats);

        sharedPreferences = getContext().getSharedPreferences("SortSettings5", Context.MODE_PRIVATE);
        String Sorting = sharedPreferences.getString("Sort", "Newest");

        if(Sorting.equals("Newest")){

            SortCommentsBy.setText("Sort by: new");

            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            Normal();
        }
        if(Sorting.equals("Oldest")){

            SortCommentsBy.setText("Sort by: old");

            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);

            Normal();
        }
        if(Sorting.equals("Top")){

            SortCommentsBy.setText("Sort by: top");

            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            Top();

        }

        SortCommentsBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] sortOptions = {"Newest", "Oldest", "Top"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort by").setIcon(R.drawable.ic_sort_green_24dp).setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Newest");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                        else if(i==1){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Oldest");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                        else if(i==2){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Top");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                    }
                });
                builder.show();
            }
        });

        SortByCommentsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] sortOptions = {"Newest", "Oldest", "Top"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort by").setIcon(R.drawable.ic_sort_green_24dp).setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Newest");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                        else if(i==1){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Oldest");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                        else if(i==2){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Top");
                            editor.apply();
                            StartOrReloadChatRooms();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void Top() {

        RoomList = getView().findViewById(R.id.rvAllChatsFragment);
        RoomList.setItemViewCacheSize(20);
        RoomList.setHasFixedSize(true);
        RoomList.setDrawingCacheEnabled(true);
        RoomList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RoomList.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingAllChats_fragment);
        postStuffForChatList = new ArrayList<>();
        postStuffForChatAdapter = null;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Chatrooms");
        registerForContextMenu(RoomList);

        rooms.orderByChild("LikeCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PostStuffForChat postStuffForChat = postSnapshot.getValue(PostStuffForChat.class);
                    postStuffForChatList.add(postStuffForChat);
                }


                postStuffForChatAdapter = new PostStuffForChatAdapter(getActivity(), postStuffForChatList);
                RoomList.setAdapter(postStuffForChatAdapter);

                progressBar.setVisibility(View.GONE);

                postStuffForChatAdapter.setOnItemClickListener(new PostStuffForChatAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final int position) {
                        final String key = postStuffForChatList.get(position).getKey();

                        DatabaseReference CheckIfExists = FirebaseDatabase.getInstance().getReference("Chatrooms");
                        CheckIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(key)){
                                    Intent Test2 = new Intent(getActivity().getApplicationContext(), Chat_With_Users_Activity.class);
                                    Test2.putExtra("Key", key);
                                    startActivity(Test2);
                                }
                                else{
                                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
                                    dialog.setTitle("This chatroom has been deleted");
                                    dialog.setMessage("This chatroom has been deleted, you can no longer view it.");

                                    dialog.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    android.app.AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onUserNameClick(final int position) {
                        final String PostKey = postStuffForChatList.get(position).getKey();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("uid");
                        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String MyUIDCheck2 = FirebaseAuth.getInstance().getUid();
                                final String PostUID2 = dataSnapshot.getValue().toString();

                                DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(PostUID2)){

                                            if(MyUIDCheck2.equals(PostUID2)){

                                                Intent GoToMyProfile = new Intent(getActivity(), Layout_Manager_BottomNav_Activity.class);
                                                GoToMyProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                GoToMyProfile.putExtra("Type", "Account");
                                                startActivity(GoToMyProfile);

                                            }
                                            else{
                                                FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue().equals("[anonymous]")){
                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                            dialog.setTitle("This user has posted anonymously");
                                                            dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                                            AlertDialog alertDialog = dialog.create();
                                                            alertDialog.show();
                                                        }else {
                                                            Intent GoToOtherProfile = new Intent(getActivity(), Account_Info_OtherUser_Chat.class);
                                                            GoToOtherProfile.putExtra("UID", postStuffForChatList.get(position).getUID());
                                                            Log.e("UIDFROMCHAT", PostUID2 );
                                                            startActivity(GoToOtherProfile);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });




                                            }

                                        }

                                        else{

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                            dialog.setTitle("This user has been deleted");
                                            dialog.setMessage("You can no longer view this user");
                                            AlertDialog alertDialog = dialog.create();
                                            alertDialog.show();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onUpvoteClick(int position) {
                        String key = postStuffForChatList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){

                                    DatabaseDislike.child(MyUID).removeValue();
                                    DatabaseLike.child(MyUID).setValue("RandomLike");

                                }


                                else{

                                    DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){
                                                DatabaseLike.child(MyUID).removeValue();
                                            }

                                            else{
                                                DatabaseLike.child(MyUID).setValue("RandomLike");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onDownvoteClick(int position) {
                        String key = postStuffForChatList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){
                                    DatabaseLike.child(MyUID).removeValue();
                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                }

                                else{

                                    DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){

                                                DatabaseDislike.child(MyUID).removeValue();

                                            }

                                            else{

                                                DatabaseDislike.child(MyUID).setValue("RandomDislike");

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                });

            }

            private void clear() {

                int size = postStuffForChatList.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        postStuffForChatList.remove(0);
                    }
                    postStuffForChatAdapter.notifyItemRangeRemoved(0, size);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void Normal() {

        RoomList = getView().findViewById(R.id.rvAllChatsFragment);
        RoomList.setItemViewCacheSize(20);
        RoomList.setHasFixedSize(true);
        RoomList.setDrawingCacheEnabled(true);
        RoomList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RoomList.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingAllChats_fragment);
        postStuffForChatList = new ArrayList<>();
        postStuffForChatAdapter = null;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Chatrooms");
        registerForContextMenu(RoomList);

        rooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PostStuffForChat postStuffForChat = postSnapshot.getValue(PostStuffForChat.class);
                    postStuffForChatList.add(postStuffForChat);
                }


                postStuffForChatAdapter = new PostStuffForChatAdapter(getActivity(), postStuffForChatList);
                RoomList.setAdapter(postStuffForChatAdapter);

                progressBar.setVisibility(View.GONE);

                postStuffForChatAdapter.setOnItemClickListener(new PostStuffForChatAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        final String key = postStuffForChatList.get(position).getKey();

                        DatabaseReference CheckIfExists = FirebaseDatabase.getInstance().getReference("Chatrooms");
                        CheckIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(key)){
                                    Intent Test2 = new Intent(getActivity().getApplicationContext(), Chat_With_Users_Activity.class);
                                    Test2.putExtra("Key", key);
                                    startActivity(Test2);
                                }
                                else{
                                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
                                    dialog.setTitle("This chatroom has been deleted");
                                    dialog.setMessage("This chatroom has been deleted, you can no longer view it.");

                                    dialog.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    android.app.AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onUserNameClick(final int position) {
                        final String PostKey = postStuffForChatList.get(position).getKey();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("uid");
                        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String MyUIDCheck2 = FirebaseAuth.getInstance().getUid();
                                final String PostUID2 = dataSnapshot.getValue().toString();

                                DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(PostUID2)){

                                            if(MyUIDCheck2.equals(PostUID2)){

                                                Intent GoToMyProfile = new Intent(getActivity(), Layout_Manager_BottomNav_Activity.class);
                                                GoToMyProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                GoToMyProfile.putExtra("Type", "Account");
                                                startActivity(GoToMyProfile);

                                            }
                                            else{
                                                FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("user_name").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue().equals("[anonymous]")){
                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                            dialog.setTitle("This user has posted anonymously");
                                                            dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                                            AlertDialog alertDialog = dialog.create();
                                                            alertDialog.show();
                                                        }else {
                                                            Intent GoToOtherProfile = new Intent(getActivity(), Account_Info_OtherUser_Chat.class);
                                                            GoToOtherProfile.putExtra("UID", postStuffForChatList.get(position).getUID());
                                                            Log.e("UIDFROMCHAT", PostUID2 );
                                                            startActivity(GoToOtherProfile);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                        }

                                        else{

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                            dialog.setTitle("This user has been deleted");
                                            dialog.setMessage("You can no longer view this user");
                                            AlertDialog alertDialog = dialog.create();
                                            alertDialog.show();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onUpvoteClick(int position) {
                        String key = postStuffForChatList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){

                                    DatabaseDislike.child(MyUID).removeValue();
                                    DatabaseLike.child(MyUID).setValue("RandomLike");

                                }


                                else{

                                    DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){
                                                DatabaseLike.child(MyUID).removeValue();
                                            }

                                            else{
                                                DatabaseLike.child(MyUID).setValue("RandomLike");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onDownvoteClick(int position) {
                        String key = postStuffForChatList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){
                                    DatabaseLike.child(MyUID).removeValue();
                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                }

                                else{

                                    DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){

                                                DatabaseDislike.child(MyUID).removeValue();

                                            }

                                            else{

                                                DatabaseDislike.child(MyUID).setValue("RandomDislike");

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                });

            }

            private void clear() {

                int size = postStuffForChatList.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        postStuffForChatList.remove(0);
                    }
                    postStuffForChatAdapter.notifyItemRangeRemoved(0, size);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void CheckInternet() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (connected) {

        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("No internet connection");
            dialog.setMessage("Please connect to the internet and try again");
            dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            });

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
    }

    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    RoomList.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }
    }


    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();

        mListState = RoomList.getLayoutManager().onSaveInstanceState();

        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

}
