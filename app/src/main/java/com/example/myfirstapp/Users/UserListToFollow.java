package com.example.myfirstapp.Users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity_Users;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Chatroom.Chat_Room_MakeOrSearch_Activity;
import com.example.myfirstapp.Choose_PostType_Activity;
import com.example.myfirstapp.Imageposts.ImagesFeed;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private Button Follow;
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
    private String UIDlistString, test, UIDlistString2, MyUID, UIDOtherUser;

    private void SetupUI() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        ProgressCircle = findViewById(R.id.progress_circle3);
        recyclerView = findViewById(R.id.recycler_viewUserList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReferenceUIDlist = firebaseDatabase.getReference("users");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        UIDlist = new ArrayList<>();
        MyUID=firebaseAuth.getUid();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_to_follow);
        SetupUI();

        SetupDesign();

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
                        Log.d("list", list.toString());}
                        }

                    }

                    adapter = new UserAdapter(UserListToFollow.this, list);
                    recyclerView.setAdapter(adapter);
                    ProgressCircle.setVisibility(View.INVISIBLE);

                    adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            UIDOtherUser=list.get(position).getTheUID();
                            Intent intent = new Intent(getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                            intent.putExtra("UID", UIDOtherUser);
                            startActivity(intent);
                        }

                        @Override
                        public void onUserNameClick(int position) {

                        }

                        @Override
                        public void onProfilePictureClick(int position) {

                        }

                        @Override
                        public void onFollowClick(int position) {

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

                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:

                            Intent home = new Intent(UserListToFollow.this, General_Feed_Activity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(home);

                            break;

                        case R.id.navigation_chat:

                            Intent chat = new Intent(UserListToFollow.this, Chat_Room_MakeOrSearch_Activity.class);
                            chat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(chat);

                            break;

                        case R.id.navigation_make:

                            Intent make = new Intent(UserListToFollow.this, Choose_PostType_Activity.class);
                            make.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(make);

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

                Intent intent = new Intent(UserListToFollow.this, ImagesFeed.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clear() {

        int size = list.size();
        if(size > 0){
            for (int i = 0; i < size; i++) {
                list.remove(0);

                String TAGTest = "ListEmpty";
                Log.e(TAGTest, "tot 'for' gekomen");
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_second);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Log.e("Testjeyeah", "onResume bereikt");
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

    }
}