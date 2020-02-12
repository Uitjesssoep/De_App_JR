package com.example.myfirstapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity_Users;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.App_Settings_Activity;
import com.example.myfirstapp.Chatroom.Chat_Room_MakeOrSearch_Activity;
import com.example.myfirstapp.Chatroom.Chatrooms_Post_Activity;
import com.example.myfirstapp.Imageposts.Upload_Images_Activity;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Textposts.Upload_TextPost_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserListToFollow extends AppCompatActivity {

    private TextView Username;
    private ImageView ProfilePicture;
    private DatabaseReference databaseReferenceUIDlist, databaseReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabaseUIDlist, firebaseDatabase;
    private ProgressBar ProgressCircle;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private RecyclerView.LayoutManager layoutManager;
    private List<UserProfileToDatabase> list;
    private List<String> UIDlist;
    private UserAdapter adapter;
    private String MyUID, UIDOtherUser, UsernameOtherUser, userNameFollower;
    private DatabaseReference datarefFollower, datarefFollowing, datarefUID, datarefOtherUID;

    private FloatingActionButton ImageFAB, TextFAB, ChatFAB;
    private Animation FABOpen, FABClose;
    private boolean FABisOpen = false;

    private void SetupUI() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        ProgressCircle = findViewById(R.id.pbLoadingUserListToFollow);
        recyclerView = findViewById(R.id.rvUserList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReferenceUIDlist = firebaseDatabase.getReference("users");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        UIDlist = new ArrayList<>();
        MyUID = firebaseAuth.getUid();
        datarefFollower = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("userName");
        datarefUID = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");
        datarefFollowing = FirebaseDatabase.getInstance().getReference().child("users");

        ImageFAB = findViewById(R.id.fabImageMakeSearch);
        TextFAB = findViewById(R.id.fabTextMakeSearch);
        ChatFAB = findViewById(R.id.fabChatMakeSearch);

        ImageFAB.setClickable(false);
        TextFAB.setClickable(false);
        ChatFAB.setClickable(false);

        FABOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FABClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        ImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserListToFollow.this, Upload_Images_Activity.class);
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
                Intent intent2 = new Intent(UserListToFollow.this, Upload_TextPost_Activity.class);
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
                Intent intent3 = new Intent(UserListToFollow.this, Chatrooms_Post_Activity.class);
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
        setContentView(R.layout.activity_user_list_to_follow);
        SetupUI();

        Log.e("Userlist", "UserListToFollow" );

        SetupDesign();

        Refresh();

    }

    private void Refresh() {

        databaseReference = firebaseDatabase.getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserProfileToDatabase users = postSnapshot.getValue(UserProfileToDatabase.class);
                    list.add(users);
                    int position;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getTheUID().equals(MyUID)) {
                            position = i;
                            list.remove(position);
                            Log.d("list", list.toString());
                        }
                    }

                }

                adapter = new UserAdapter(UserListToFollow.this, list);
                recyclerView.setAdapter(adapter);
                ProgressCircle.setVisibility(View.INVISIBLE);

                datarefFollower.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userNameFollower = dataSnapshot.getValue().toString();

                        //    Log.e(TAGTEST, userNameFollower);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        UIDOtherUser = list.get(position).getTheUID();
                        Log.e("Checkj", "test");
                        Intent intent = new Intent(getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);

                        startActivity(intent);
                    }

                    @Override
                    public void onUserNameClick(int position) {
                        UIDOtherUser = list.get(position).getTheUID();
                        Intent intent = new Intent(getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onProfilePictureClick(int position) {
                        UIDOtherUser = list.get(position).getTheUID();
                        Log.e("Check", UIDOtherUser);
                        Intent intent = new Intent(getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onFollowClick(int position) {
                        UIDOtherUser = list.get(position).getTheUID();
                        UsernameOtherUser = list.get(position).getUserName();
                        datarefOtherUID = FirebaseDatabase.getInstance().getReference().child("users").child(UIDOtherUser).child("followers");
                        datarefUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(UIDOtherUser)) {
                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(UserListToFollow.this);
                                    dialog.setTitle("Unfollow");
                                    dialog.setMessage("Are you sure you want to unfollow this user?");
                                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            datarefUID.child(UIDOtherUser).removeValue();
                                            datarefOtherUID.child(MyUID).removeValue();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                } else {
                                    datarefFollowing.child(MyUID).child("following").child(UIDOtherUser).setValue(UsernameOtherUser);
                                    datarefFollowing.child(UIDOtherUser).child("followers").child(MyUID).setValue(userNameFollower);
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
                Toast.makeText(UserListToFollow.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                ProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void SetupDesign() {

        setTheme(R.style.AppTheme);

        //voor het geven van kleur aan de status bar:

        Window window = UserListToFollow.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(UserListToFollow.this, R.color.slighly_darker_mainGreen));


        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //bottom navigation view dingen

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;
                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:

                            Intent home = new Intent(UserListToFollow.this, Layout_Manager_BottomNav_Activity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(home);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                            break;

                        case R.id.navigation_chat:

                            Intent chat = new Intent(UserListToFollow.this, Chat_Room_MakeOrSearch_Activity.class);
                            chat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(chat);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

                            break;

                        case R.id.navigation_make:

                            if(FABisOpen){
                                ImageFAB.startAnimation(FABClose);
                                TextFAB.startAnimation(FABClose);
                                ChatFAB.startAnimation(FABClose);
                                ImageFAB.setClickable(false);
                                TextFAB.setClickable(false);
                                ChatFAB.setClickable(false);
                                FABisOpen = false;
                            }

                            else {
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

                            //Intent search = new Intent(UserListToFollow.this, UserListToFollow.class);
                            //search.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //startActivity(search);

                            break;

                        case R.id.navigation_account:

                            Intent account = new Intent(UserListToFollow.this, Account_Info_Activity.class);
                            account.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(account);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_account);

                            break;

                    }

                    return true;
                }
            };

    //voor menu in de action bar

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search);
      // SearchView searchView = ((s) menu.findItem(R.id.action_search).getActionView());
        //SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:

                Intent intent = new Intent(UserListToFollow.this, App_Settings_Activity.class);
                startActivity(intent);

                break;

            case R.id.action_refresh_feed:
                // clear();
                Refresh();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clear() {

        int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);

                String TAGTest = "ListEmpty";
                Log.e(TAGTest, "tot 'for' gekomen");
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Log.e("Testjeyeah", "onResume bereikt");
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

    }
}