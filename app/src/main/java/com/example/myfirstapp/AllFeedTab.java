package com.example.myfirstapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.Imageposts.Image_Post_Viewing_Activity;
import com.example.myfirstapp.Textposts.StuffForPost;
import com.example.myfirstapp.Textposts.StuffForPostAdapter;
import com.example.myfirstapp.Textposts.Post_Viewing_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllFeedTab extends Fragment {

    private List<StuffForPost> StuffForPostList;
    private RecyclerView GeneralFeed;
    private StuffForPostAdapter stuffForPostAdapter;

    private TextView SortCommentsBy;
    private ImageView SortByCommentsIV;

    private LinearLayoutManager linearLayoutManager; //voor sorteren
    private SharedPreferences sharedPreferences; //saven sorteer setting

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    public AllFeedTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_feed_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipe_container_AllFeed_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StartOrReloadTextPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CheckInternet();
        StartOrReloadTextPosts();
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

    private void StartOrReloadTextPosts() {


        SortCommentsBy = getView().findViewById(R.id.tvSortByTextAllFeed);
        SortByCommentsIV = getView().findViewById(R.id.ivSortByAllFeed);

        sharedPreferences = getContext().getSharedPreferences("SortSettings", Context.MODE_PRIVATE);
        String Sorting = sharedPreferences.getString("Sort", "Newest");

        if(Sorting.equals("Newest")){

            SortCommentsBy.setText("Sort by: new");

            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        }
        if(Sorting.equals("Oldest")){

            SortCommentsBy.setText("Sort by: old");

            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }
        if(Sorting.equals("Top")){

            SortCommentsBy.setText("Sort by: top");

            linearLayoutManager = new LinearLayoutManager(getContext());

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
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(i==1){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Oldest");
                            editor.apply();
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(i==2){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Top");
                            editor.apply();
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
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
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(i==1){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Oldest");
                            editor.apply();
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else if(i==2){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "Top");
                            editor.apply();
                            Intent intent = new Intent(getActivity().getIntent());
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
                builder.show();
            }
        });


        GeneralFeed = getView().findViewById(R.id.rvAllFeedFragment);
        GeneralFeed.setItemViewCacheSize(20);
        GeneralFeed.setHasFixedSize(true);
        GeneralFeed.setDrawingCacheEnabled(true);
        GeneralFeed.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GeneralFeed.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingAllFeed_fragment);
        StuffForPostList = new ArrayList<>();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference Textposts = FirebaseDatabase.getInstance().getReference("General_Posts");
        final DatabaseReference Imageposts = FirebaseDatabase.getInstance().getReference("General_Posts");
        registerForContextMenu(GeneralFeed);

        Textposts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StuffForPost StuffForPost = postSnapshot.getValue(StuffForPost.class);
                    StuffForPostList.add(StuffForPost);
                }

                StuffForPostAdapter stuffForPostAdapter;

                stuffForPostAdapter = new StuffForPostAdapter(getActivity(), StuffForPostList);
                GeneralFeed.setAdapter(stuffForPostAdapter);

                progressBar.setVisibility(View.GONE);

                stuffForPostAdapter.setOnItemClickListener(new StuffForPostAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        final String key = StuffForPostList.get(position).getKey().toString();

                        DatabaseReference CheckIfExists = FirebaseDatabase.getInstance().getReference("General_Posts");
                        CheckIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(key)){
                                    Intent Test2 = new Intent(getActivity().getApplicationContext(), Post_Viewing_Activity.class);
                                    Test2.putExtra("Key", key);
                                    startActivity(Test2);
                                }
                                else{
                                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
                                    dialog.setTitle("This post has been deleted");
                                    dialog.setMessage("This post has been deleted, you can no longer view it.");

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
                    public void onUserNameClick(int position) {
                        final String PostKey = StuffForPostList.get(position).getKey();
                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey);
                        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String MyUIDCheck2 = FirebaseAuth.getInstance().getUid().toString();
                                final String PostUID2 = dataSnapshot.child("uid").getValue().toString();
                                final String AnonToCheck = dataSnapshot.child("user_name").getValue().toString();
                                final String AnonCheck = "[anonymous]";

                                DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(PostUID2)) {

                                            if (MyUIDCheck2.equals(PostUID2)) {

                                                Intent GoToMyProfile = new Intent(getActivity(), Layout_Manager_BottomNav_Activity.class);
                                                GoToMyProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                GoToMyProfile.putExtra("Type", "Account");
                                                startActivity(GoToMyProfile);

                                            } else {

                                                if (AnonCheck.equals(AnonToCheck)) {

                                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                    dialog.setTitle("This user has posted anonymously");
                                                    dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                                    AlertDialog alertDialog = dialog.create();
                                                    alertDialog.show();

                                                } else {

                                                    Intent GoToProfile = new Intent(getActivity(), Account_Info_OtherUser_Activity.class);
                                                    GoToProfile.putExtra("Key", PostKey);
                                                    startActivity(GoToProfile);

                                                }
                                            }

                                        } else {

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
                        String key = StuffForPostList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");


                        final String TAGDownvote = "VoteCheck";


                        DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(MyUID)) {

                                    DatabaseDislike.child(MyUID).removeValue();
                                    DatabaseLike.child(MyUID).setValue("RandomLike");

                                } else {

                                    DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild(MyUID)) {
                                                DatabaseLike.child(MyUID).removeValue();
                                            } else {
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
                        String key = StuffForPostList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");

                        final String TAGDownvote = "VoteCheck";


                        DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(MyUID)) {
                                    DatabaseLike.child(MyUID).removeValue();
                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                } else {

                                    DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild(MyUID)) {

                                                DatabaseDislike.child(MyUID).removeValue();

                                            } else {

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

                int size = StuffForPostList.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        StuffForPostList.remove(0);

                        String TAGTest = "ListEmpty";
                        // Log.e(TAGTest, "tot 'for' gekomen");
                    }

                    stuffForPostAdapter.notifyItemRangeRemoved(0, size);
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
                    GeneralFeed.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }
    }


    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();

        mListState = GeneralFeed.getLayoutManager().onSaveInstanceState();

        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }


}
