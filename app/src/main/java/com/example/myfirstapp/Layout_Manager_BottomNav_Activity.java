package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.myfirstapp.Chatroom.Chat_With_Users_Activity;
import com.example.myfirstapp.Textposts.Followers_Feed_Activity;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Layout_Manager_BottomNav_Activity extends AppCompatActivity {

    private String CurrentFrag = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout__manager__bottom_nav_);

        SetupDesign();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_manager);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(getIntent().hasExtra("Type")) {
            String Type = getIntent().getExtras().get("Type").toString();
            if (Type.equals("Account")) {
                bottomNav.setSelectedItemId(R.id.navigation_account);
            }
            if (Type.equals("ChatMake")) {
                bottomNav.setSelectedItemId(R.id.navigation_chat);
                String ChatRoomKey = getIntent().getExtras().get("Key").toString();
                Intent intent = new Intent(Layout_Manager_BottomNav_Activity.this, Chat_With_Users_Activity.class);
                intent.putExtra("Key", ChatRoomKey);
                startActivity(intent);
            }
            else {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                        new HomeFragment()).commit();
            }
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                    new HomeFragment()).commit();
        }
    }

    private void SetupDesign() {

        Window window = Layout_Manager_BottomNav_Activity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Layout_Manager_BottomNav_Activity.this, R.color.slighly_darker_mainGreen));

        Toolbar toolbar = findViewById(R.id.action_bar_feed_manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:

                            selectedFragment = new HomeFragment();
                            CurrentFrag = "Home";

                            break;

                        case R.id.navigation_chat:

                            selectedFragment = new ChatFragment();
                            CurrentFrag = "Chat";

                            break;

                        case R.id.navigation_make:

                            Intent intent = new Intent(Layout_Manager_BottomNav_Activity.this, Choose_PostType_Activity.class);
                            startActivity(intent);

                            break;

                        case R.id.navigation_search:

                            selectedFragment = new SearchFragment();
                            CurrentFrag = "Search";

                            break;

                        case R.id.navigation_account:

                            selectedFragment = new AccountFragment();
                            CurrentFrag = "Account";

                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                            selectedFragment).commit();

                    return true;

                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_feedfragment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(Layout_Manager_BottomNav_Activity.this, App_Settings_Activity.class);
                startActivity(intent);
                break;

            case R.id.action_ToSavedPosts:
                Intent intent2 = new Intent(Layout_Manager_BottomNav_Activity.this, MySaved_Activity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
