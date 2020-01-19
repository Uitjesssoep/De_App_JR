package com.example.myfirstapp.Chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Chat;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Choose_PostType_Activity;
import com.example.myfirstapp.Imageposts.ImagesFeed;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.example.myfirstapp.Textposts.PostStuffForText;
import com.example.myfirstapp.Textposts.PostStuffForTextAdapter;
import com.example.myfirstapp.Textposts.Text_Post_Viewing_Activity;
import com.example.myfirstapp.Users.UserListToFollow;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chat_Room_MakeOrSearch_Activity extends AppCompatActivity {

    private RecyclerView RoomList;
    private List<PostStuffForChat> postStuffForChatList;
    private PostStuffForChatAdapter postStuffForChatAdapter;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Chatrooms");
    private DatabaseReference DatabaseDislike, DatabaseLike;

    private String MyUID, key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room__make_or_search_);

        SetupUI();

        SetupDesign();

        rooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PostStuffForChat postStuffForChat = postSnapshot.getValue(PostStuffForChat.class);
                    postStuffForChatList.add(postStuffForChat);
                }

                postStuffForChatAdapter = new PostStuffForChatAdapter(Chat_Room_MakeOrSearch_Activity.this, postStuffForChatList);
                RoomList.setAdapter(postStuffForChatAdapter);

                postStuffForChatAdapter.setOnItemClickListener(new PostStuffForChatAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        key = postStuffForChatList.get(position).getKey().toString();
                        Intent Test2 = new Intent(getApplicationContext(), Chat_With_Users_Activity.class);
                        Test2.putExtra("Key", key);
                        startActivity(Test2);
                        finish();
                    }

                    @Override
                    public void onDeleteIconClick(int position) {
                        final String TAGCheck = "DeleteTextPost";
                        Log.e(TAGCheck, "Deleting Text Post After Click");

                        final String ThePostKey = postStuffForChatList.get(position).getKey().toString();

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Chat_Room_MakeOrSearch_Activity.this);
                        dialog.setTitle("Delete your chatroom?");
                        dialog.setMessage("Deleting this chatroom will delete the chatroom and all of its content, it cannot be undone! Are you sure you want to delete it?");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("Chatrooms").child(ThePostKey);
                                DeleteThePost.removeValue();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
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

                    }

                    @Override
                    public void onUserNameClick(int position) {
                        final String PostKey = postStuffForChatList.get(position).getKey().toString();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("uid");
                        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String MyUIDCheck2 = FirebaseAuth.getInstance().getUid().toString();
                                final String PostUID2 = dataSnapshot.getValue().toString();

                                DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(PostUID2)){

                                            if(MyUIDCheck2.equals(PostUID2)){

                                                Intent GoToMyProfile = new Intent(Chat_Room_MakeOrSearch_Activity.this, Account_Info_Activity.class);
                                                startActivity(GoToMyProfile);

                                            }
                                            else{

                                                Intent GoToOtherProfile = new Intent(Chat_Room_MakeOrSearch_Activity.this, Account_Info_OtherUser_Chat.class);
                                                GoToOtherProfile.putExtra("Key", PostKey);
                                                startActivity(GoToOtherProfile);

                                            }

                                        }

                                        else{

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(Chat_Room_MakeOrSearch_Activity.this);
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
                        key = postStuffForChatList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
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
                        key = postStuffForChatList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = Chat_Room_MakeOrSearch_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Chat_Room_MakeOrSearch_Activity.this, R.color.slighly_darker_mainGreen));


        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //bottom navigation view dingen

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:

                            Intent home = new Intent(Chat_Room_MakeOrSearch_Activity.this, General_Feed_Activity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(home);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                            break;

                        case R.id.navigation_chat:

                            //Intent chat = new Intent(Chat_Room_MakeOrSearch_Activity.this, Chat_Room_MakeOrSearch_Activity.class);
                            //chat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //startActivity(chat);

                            break;

                        case R.id.navigation_make:

                            Intent make = new Intent(Chat_Room_MakeOrSearch_Activity.this, Choose_PostType_Activity.class);
                            make.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(make);

                            break;

                        case R.id.navigation_search:

                            Intent search = new Intent(Chat_Room_MakeOrSearch_Activity.this, UserListToFollow.class);
                            search.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(search);
                            //bottomNavigationView.setSelectedItemId(R.id.navigation_search);

                            break;

                        case R.id.navigation_account:

                            Intent account = new Intent(Chat_Room_MakeOrSearch_Activity.this, Account_Info_Activity.class);
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

        switch (item.getItemId()){
            case R.id.action_settings:

                Intent intent = new Intent(Chat_Room_MakeOrSearch_Activity.this, ImagesFeed.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void SetupUI() {

        RoomList = findViewById(R.id.rvChatroomFeed);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RoomList.setLayoutManager(new LinearLayoutManager(this));

        postStuffForChatList = new ArrayList<>();

    }

    @Override
    protected void onResume(){
        super.onResume();

        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);
        //bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Log.e("Testjeyeah", "onResume bereikt");
        //bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

    }

}
