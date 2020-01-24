package com.example.myfirstapp.Textposts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.App_Settings_Activity;
import com.example.myfirstapp.Chatroom.Chat_Room_MakeOrSearch_Activity;
import com.example.myfirstapp.Chatroom.Chatrooms_Post_Activity;
import com.example.myfirstapp.GeneralAdapter;
import com.example.myfirstapp.Imageposts.Image_Post_Viewing_Activity;
import com.example.myfirstapp.Imageposts.ImagesFeed;
import com.example.myfirstapp.Imageposts.Upload_Images_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Users.UserListToFollow;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class General_Feed_Activity extends AppCompatActivity
        //implements PopupMenu.OnMenuItemClickListener
{


    private RecyclerView GeneralFeed;
    private List<StuffForPost> StuffForPostList;
    private StuffForPostAdapter stuffForPostAdapter;
    private GeneralAdapter generalAdapter;

    private FloatingActionButton ImageFAB, TextFAB, ChatFAB;
    private Animation FABOpen, FABClose;
    private boolean FABisOpen = false;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference Textposts = FirebaseDatabase.getInstance().getReference("General_Posts");
    private DatabaseReference Imageposts = FirebaseDatabase.getInstance().getReference("General_Posts");
    private DatabaseReference CheckIfMyUID;
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;

    private String key, MyUID, TAG = "Check";
    private Boolean Liked = false, Disliked = false, LikedCheck = false, DislikedCheck = false;
    private int LikeCount, DislikeCount, CommentCount;

    private ProgressBar progressBar;

    private BottomNavigationView bottomNavigationView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Boolean test = false;

    private void SetupUI() {

        GeneralFeed = findViewById(R.id.rvFeedScreen);
        GeneralFeed.setItemViewCacheSize(20);
        GeneralFeed.setHasFixedSize(true);
        GeneralFeed.setDrawingCacheEnabled(true);
        GeneralFeed.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GeneralFeed.setLayoutManager(new LinearLayoutManager(this));
        ImageFAB = findViewById(R.id.fabImageMake);
        TextFAB = findViewById(R.id.fabTextMake);
        ChatFAB = findViewById(R.id.fabChatMake);

        ImageFAB.setClickable(false);
        TextFAB.setClickable(false);
        ChatFAB.setClickable(false);

        FABOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FABClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        StuffForPostList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.pbLoadingGeneralFeed);

        bottomNavigationView = findViewById(R.id.bottom_nav_second);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        swipeRefreshLayout = findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                StartOrReloadTextPosts();
                //  clear();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        registerForContextMenu(GeneralFeed);

        ImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(General_Feed_Activity.this, Upload_Images_Activity.class);
                startActivity(intent);
                ImageFAB.startAnimation(FABClose);
                TextFAB.startAnimation(FABClose);
                ChatFAB.startAnimation(FABClose);
                ImageFAB.setClickable(false);
                TextFAB.setClickable(false);
                ChatFAB.setClickable(false);
                FABisOpen = false;
            }
        });

        TextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(General_Feed_Activity.this, Upload_TextPost_Activity.class);
                startActivity(intent2);
                ImageFAB.startAnimation(FABClose);
                TextFAB.startAnimation(FABClose);
                ChatFAB.startAnimation(FABClose);
                ImageFAB.setClickable(false);
                TextFAB.setClickable(false);
                ChatFAB.setClickable(false);
                FABisOpen = false;
            }
        });

        ChatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(General_Feed_Activity.this, Chatrooms_Post_Activity.class);
                startActivity(intent3);
                ImageFAB.startAnimation(FABClose);
                TextFAB.startAnimation(FABClose);
                ChatFAB.startAnimation(FABClose);
                ImageFAB.setClickable(false);
                TextFAB.setClickable(false);
                ChatFAB.setClickable(false);
                FABisOpen = false;
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general__feed);

        CheckInternet();

        SetupUI();

        SetupDesign();

        //clear();
        //   LoadAdapter();

        StartOrReloadTextPosts();


    }

    private void StartOrReloadImagePosts() {

        Imageposts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StuffForPost upload = postSnapshot.getValue(StuffForPost.class);
                    StuffForPostList.add(upload);
                    stuffForPostAdapter = new StuffForPostAdapter(General_Feed_Activity.this, StuffForPostList);
                    GeneralFeed.setAdapter(stuffForPostAdapter);
                    // Log.e(TAG, "LoadAdapter");

                    progressBar.setVisibility(View.GONE);
                    //  Log.e(TAG,"images toegevoegd");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void StartOrReloadTextPosts() {

        Textposts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StuffForPost StuffForPost = postSnapshot.getValue(StuffForPost.class);
                    StuffForPostList.add(StuffForPost);

                    //  Log.e(TAG,"images toegevoegd");
                    Log.e(TAG, "textpostsreload");
                    // Log.e("tekstshit", StuffForPostList.toString());
                }

                stuffForPostAdapter = new StuffForPostAdapter(General_Feed_Activity.this, StuffForPostList);
                GeneralFeed.setAdapter(stuffForPostAdapter);

                progressBar.setVisibility(View.GONE);

                stuffForPostAdapter.setOnItemClickListener(new StuffForPostAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        key = StuffForPostList.get(position).getKey().toString();

                        if (StuffForPostList.get(position).getType().equals("Image")) {
                            Intent Test2 = new Intent(getApplicationContext(), Image_Post_Viewing_Activity.class);
                            Test2.putExtra("Key", key);
                            startActivity(Test2);
                        } else {
                            Intent Test2 = new Intent(getApplicationContext(), Text_Post_Viewing_Activity.class);
                            Test2.putExtra("Key", key);
                            startActivity(Test2);
                        }


                    }

                    @Override
                    public void onUserNameClick(int position) {
                        final String PostKey = StuffForPostList.get(position).getKey().toString();
                        CheckIfMyUID = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey);
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

                                                Intent GoToMyProfile = new Intent(General_Feed_Activity.this, Account_Info_Activity.class);
                                                startActivity(GoToMyProfile);

                                            } else {

                                                if (AnonCheck.equals(AnonToCheck)) {

                                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(General_Feed_Activity.this);
                                                    dialog.setTitle("This user has posted anonymously");
                                                    dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                                    AlertDialog alertDialog = dialog.create();
                                                    alertDialog.show();

                                                } else {

                                                    Intent GoToProfile = new Intent(General_Feed_Activity.this, Account_Info_OtherUser_Activity.class);
                                                    GoToProfile.putExtra("Key", PostKey);
                                                    startActivity(GoToProfile);

                                                }
                                            }

                                        } else {

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(General_Feed_Activity.this);
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
                        key = StuffForPostList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");


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
                        key = StuffForPostList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckInternet() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (connected) {

        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(General_Feed_Activity.this);
            dialog.setTitle("No internet connection");
            dialog.setMessage("Please connect to the internet and try again");
            dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(getIntent());
                }
            });

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = General_Feed_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(General_Feed_Activity.this, R.color.slighly_darker_mainGreen));


        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //bottom navigation view dingen

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;
                    bottomNavigationView = findViewById(R.id.bottom_nav_second);

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:

                            Intent home = new Intent(General_Feed_Activity.this, General_Feed_Activity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                            startActivity(home);

                            break;

                        case R.id.navigation_chat:

                            Intent chat = new Intent(General_Feed_Activity.this, Chat_Room_MakeOrSearch_Activity.class);
                            chat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(chat);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

                            break;

                        case R.id.navigation_make:

                            /*Intent make = new Intent(General_Feed_Activity.this, Choose_PostType_Activity.class);
                            make.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(make);*/

                            if (FABisOpen) {
                                ImageFAB.startAnimation(FABClose);
                                TextFAB.startAnimation(FABClose);
                                ChatFAB.startAnimation(FABClose);
                                ImageFAB.setClickable(false);
                                TextFAB.setClickable(false);
                                ChatFAB.setClickable(false);
                                FABisOpen = false;
                            } else {
                                ImageFAB.startAnimation(FABOpen);
                                TextFAB.startAnimation(FABOpen);
                                ChatFAB.startAnimation(FABOpen);
                                ImageFAB.setClickable(true);
                                TextFAB.setClickable(true);
                                ChatFAB.setClickable(true);
                                FABisOpen = true;
                            }

                            break;

                        case R.id.navigation_search:

                            Intent search = new Intent(General_Feed_Activity.this, UserListToFollow.class);
                            search.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(search);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_search);

                            break;

                        case R.id.navigation_account:

                            Intent account = new Intent(General_Feed_Activity.this, Account_Info_Activity.class);
                            account.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(account);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_account);

                            break;

                    }

                    return true;
                }
            };

    //voor menu in de action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                Intent intent = new Intent(General_Feed_Activity.this, App_Settings_Activity.class);
                startActivity(intent);

                break;

            case R.id.action_refresh_feed:
                // clear();
                StartOrReloadTextPosts();

                break;

            case R.id.action_image_feed:

                Intent intent2 = new Intent(General_Feed_Activity.this, ImagesFeed.class);
                startActivity(intent2);

                break;

            case R.id.action_follwers_feed:
                Intent startFeed = new Intent(General_Feed_Activity.this, Followers_Feed_Activity.class);
                startActivity(startFeed);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void clear() {

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

}
