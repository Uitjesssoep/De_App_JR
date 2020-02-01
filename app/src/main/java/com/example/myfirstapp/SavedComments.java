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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUserComments_Activity;
import com.example.myfirstapp.Chatroom.PostStuffForChat;
import com.example.myfirstapp.Chatroom.PostStuffForChatAdapter;
import com.example.myfirstapp.Imageposts.Image_Post_Viewing_Activity;
import com.example.myfirstapp.Textposts.CommentStuffForTextPost;
import com.example.myfirstapp.Textposts.CommentStuffForTextPostAdapter;
import com.example.myfirstapp.Textposts.Text_Post_Viewing_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedComments extends Fragment {


    public SavedComments() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        CheckInternet();
        StartOrReload();

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipe_container_SavedComments_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

    private void StartOrReload() {

        final RecyclerView CommentView = getView().findViewById(R.id.rvSavedCommentsFragment);
        CommentView.setItemViewCacheSize(20);
        CommentView.setHasFixedSize(true);
        CommentView.setDrawingCacheEnabled(true);
        CommentView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        CommentView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ProgressBar progressBar = getView().findViewById(R.id.pbLoadingSavedComments_fragment);
        final List<CommentStuffForTextPost> commentStuffForTextPostList = new ArrayList<>();
        final CommentStuffForTextPostAdapter commentStuffForTextPostAdapter = null;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference comments = FirebaseDatabase.getInstance().getReference("users").child(MyUID).child("SavedComments");
        registerForContextMenu(CommentView);

        comments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentStuffForTextPost commentStuffForTextPost = dataSnapshot1.getValue(CommentStuffForTextPost.class);
                    commentStuffForTextPostList.add(commentStuffForTextPost);
                }

                CommentStuffForTextPostAdapter commentStuffForTextPostAdapter;

                commentStuffForTextPostAdapter = new CommentStuffForTextPostAdapter(getActivity(), commentStuffForTextPostList);
                CommentView.setAdapter(commentStuffForTextPostAdapter);
                progressBar.setVisibility(View.GONE);

                commentStuffForTextPostAdapter.setOnItemClickListener(new CommentStuffForTextPostAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {

                        final String PostKey = commentStuffForTextPostList.get(position).getOldKey();

                        DatabaseReference CheckType = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey);
                        CheckType.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String Type = dataSnapshot.child("type").getValue().toString();

                                if(Type.equals("Text")){

                                    Intent ToTextPost = new Intent(getActivity(), Text_Post_Viewing_Activity.class);
                                    ToTextPost.putExtra("Key", PostKey);
                                    startActivity(ToTextPost);

                                }
                                else if(Type.equals("Image")){

                                    Intent ToImagePost = new Intent(getActivity(), Image_Post_Viewing_Activity.class);
                                    ToImagePost.putExtra("Key", PostKey);
                                    startActivity(ToImagePost);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onUserNameClick(int position) {
                        final String CommentKey = commentStuffForTextPostList.get(position).getKey();
                        final String PostKey = commentStuffForTextPostList.get(position).getOldKey();

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

                                                Intent GoToMyProfile = new Intent(getActivity(), Account_Info_Activity.class);
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
                        String key = commentStuffForTextPostList.get(position).getKey().toString();
                        String Postkey = commentStuffForTextPostList.get(position).getOldKey();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Dislikes");
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
                        String key = commentStuffForTextPostList.get(position).getKey().toString();
                        final String MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        String Postkey = commentStuffForTextPostList.get(position).getOldKey();
                        final DatabaseReference DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Likes");
                        final DatabaseReference DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(Postkey).child("Comments").child(key).child("Dislikes");
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
                int size = commentStuffForTextPostList.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        commentStuffForTextPostList.remove(0);

                        String TAGTest = "ListEmpty";
                        // Log.e(TAGTest, "tot 'for' gekomen");
                    }

                    commentStuffForTextPostAdapter.notifyItemRangeRemoved(0, size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}