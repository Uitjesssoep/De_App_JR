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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myfirstapp.AccountActivities.Account_Info_OtherUserComments_Activity;
import com.example.myfirstapp.Textposts.CommentStuffForTextPostMyProf;
import com.example.myfirstapp.Textposts.CommentStuffForTextPostMyProfAdapter;
import com.example.myfirstapp.Textposts.Post_Viewing_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyCommentsTab extends Fragment {

    private RecyclerView CommentView;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    public MyCommentsTab() {
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
        return inflater.inflate(R.layout.fragment_my_comments_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        CheckIfMineGetsDeleted();
        CheckInternet();
        StartOrReload();

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipe_container_MyComments_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StartOrReload();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private void CheckIfMineGetsDeleted() {
        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference Check = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("MyComments");
        Check.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                StartOrReload();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

    private void StartOrReload() {

        CommentView = getView().findViewById(R.id.rvMyCommentsFragment);
        CommentView.setItemViewCacheSize(20);
        CommentView.setHasFixedSize(true);
        CommentView.setDrawingCacheEnabled(true);
        CommentView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentView.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingMyComments_fragment);
        final List<CommentStuffForTextPostMyProf> commentStuffForTextPostMyProfList = new ArrayList<>();
        final CommentStuffForTextPostMyProfAdapter commentStuffForTextPostMyProfAdapter = null;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference comments = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("MyComments");
        registerForContextMenu(CommentView);

        comments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentStuffForTextPostMyProf commentStuffForTextPostMyProf = dataSnapshot1.getValue(CommentStuffForTextPostMyProf.class);
                    commentStuffForTextPostMyProfList.add(commentStuffForTextPostMyProf);
                }

                CommentStuffForTextPostMyProfAdapter commentStuffForTextPostMyProfAdapter1;

                commentStuffForTextPostMyProfAdapter1 = new CommentStuffForTextPostMyProfAdapter(getActivity(), commentStuffForTextPostMyProfList);
                CommentView.setAdapter(commentStuffForTextPostMyProfAdapter1);
                progressBar.setVisibility(View.GONE);

                commentStuffForTextPostMyProfAdapter1.setOnItemClickListener(new CommentStuffForTextPostMyProfAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        final String PostKey = commentStuffForTextPostMyProfList.get(position).getOldKey();

                        DatabaseReference CheckIfExists = FirebaseDatabase.getInstance().getReference("General_Posts");
                        CheckIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(PostKey)){
                                    Intent Test2 = new Intent(getActivity().getApplicationContext(), Post_Viewing_Activity.class);
                                    Test2.putExtra("Key", PostKey);
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
                        final String CommentKey = commentStuffForTextPostMyProfList.get(position).getKey();
                        final String PostKey = commentStuffForTextPostMyProfList.get(position).getOldKey();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey).child("Comments").child(CommentKey).child("uid");
                        CheckIfMyUID.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String MyUIDCheck = FirebaseAuth.getInstance().getUid();
                                final String CommentUID = dataSnapshot.getValue().toString();

                                DatabaseReference UserIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                UserIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(CommentUID)){


                                            if(MyUIDCheck.equals(CommentUID)){

                                                Intent GoToMyProfile = new Intent(getActivity(), Layout_Manager_BottomNav_Activity.class);
                                                GoToMyProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                GoToMyProfile.putExtra("Type", "Account");
                                                startActivity(GoToMyProfile);

                                            }

                                            else{

                                                DatabaseReference GetCommentUsername = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey).child("Comments").child(CommentKey).child("user_name");
                                                GetCommentUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        String CommentUsername = dataSnapshot.getValue().toString();
                                                        if(CommentUsername.equals("[deleted_comment_user")){
                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                            dialog.setTitle("This comment has been deleted");
                                                            dialog.setMessage("You can no longer see who made this comment");
                                                            AlertDialog alertDialog = dialog.create();
                                                            alertDialog.show();
                                                        }

                                                        else {
                                                            Intent GoToProfile = new Intent(getActivity(), Account_Info_OtherUserComments_Activity.class);
                                                            GoToProfile.putExtra("CommentKey", CommentKey);
                                                            GoToProfile.putExtra("PostKey", PostKey);
                                                            startActivity(GoToProfile);
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
                        String key = commentStuffForTextPostMyProfList.get(position).getKey();
                        String Postkey = commentStuffForTextPostMyProfList.get(position).getOldKey();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Dislikes");

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
                        String key = commentStuffForTextPostMyProfList.get(position).getKey();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid();
                        String Postkey = commentStuffForTextPostMyProfList.get(position).getOldKey();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Dislikes");

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
                int size = commentStuffForTextPostMyProfList.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        commentStuffForTextPostMyProfList.remove(0);

                        String TAGTest = "ListEmpty";
                        // Log.e(TAGTest, "tot 'for' gekomen");
                    }

                    commentStuffForTextPostMyProfAdapter.notifyItemRangeRemoved(0, size);
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
                    CommentView.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }
    }


    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();

        mListState = CommentView.getLayoutManager().onSaveInstanceState();

        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

}
