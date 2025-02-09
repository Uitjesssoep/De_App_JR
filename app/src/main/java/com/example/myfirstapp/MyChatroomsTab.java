package com.example.myfirstapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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


public class MyChatroomsTab extends Fragment {

    private RecyclerView RoomList;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    public MyChatroomsTab() {
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
        return inflater.inflate(R.layout.fragment_my_chatrooms_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CheckInternet();
        StartOrReloadChatRooms();

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipe_container_MyChats_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StartOrReloadChatRooms();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
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

    private void StartOrReloadChatRooms() {

        RoomList = getView().findViewById(R.id.rvMyChatsFragment);
        RoomList.setItemViewCacheSize(20);
        RoomList.setHasFixedSize(true);
        RoomList.setDrawingCacheEnabled(true);
        RoomList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RoomList.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingMyChats_fragment);
        final List<PostStuffForChat> postStuffForChatList = new ArrayList<>();
        final PostStuffForChatAdapter postStuffForChatAdapter = null;
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
                    final String MyUID = firebaseAuth.getCurrentUser().getUid();
                    for (int i = 0; i < postStuffForChatList.size(); i++) {
                        int position;

                        if (!MyUID.equals(postStuffForChatList.get(i).getUID())){
                            position = i;
                            postStuffForChatList.remove(position);
                            Log.e("list", postStuffForChatList.toString());
                        }}

                    PostStuffForChatAdapter stuffForChatAdapter;
                    stuffForChatAdapter = new PostStuffForChatAdapter(getActivity(), postStuffForChatList);
                    RoomList.setAdapter(stuffForChatAdapter);

                    progressBar.setVisibility(View.GONE);

                    stuffForChatAdapter.setOnItemClickListener(new PostStuffForChatAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(int position) {
                            String key = postStuffForChatList.get(position).getKey();
                            Intent Test2 = new Intent(getActivity().getApplicationContext(), Chat_With_Users_Activity.class);
                            Test2.putExtra("Key", key);
                            startActivity(Test2);
                        }

                        @Override
                        public void onUserNameClick(int position) {
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

                                                    Intent GoToOtherProfile = new Intent(getActivity(), Account_Info_OtherUser_Chat.class);
                                                    GoToOtherProfile.putExtra("Key", PostKey);
                                                    startActivity(GoToOtherProfile);

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
