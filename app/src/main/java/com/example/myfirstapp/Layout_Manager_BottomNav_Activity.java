package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myfirstapp.AccountActivities.MainActivity;
import com.example.myfirstapp.Chatroom.Chat_With_Users_Activity;
import com.example.myfirstapp.Textposts.Post_Viewing_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Layout_Manager_BottomNav_Activity extends AppCompatActivity {

    private String CurrentFrag = "Home";
    private Boolean MakingSelected = false;
    SharedPrefNightMode sharedPrefNightMode;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if (sharedPrefNightMode.loadNightModeState() == true) {
            setTheme(R.style.AppTheme_Night);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout__manager__bottom_nav_);

        firebaseAuth = FirebaseAuth.getInstance();
        checkEmailVerification();
    }

    private void Checked() {

        FrameLayout frameLayout = findViewById(R.id.framelayout_manager_makepost);
        frameLayout.setVisibility(View.GONE);

        SetupDesign();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_manager);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (getIntent().hasExtra("Type")) {
            String Type = getIntent().getExtras().get("Type").toString();
            if (Type.equals("Account")) {
                Log.e("Intent", "Account");
                bottomNav.setSelectedItemId(R.id.navigation_account);
                MakingSelected = false;
            } else {
                if (Type.equals("ChatMake")) {
                    bottomNav.setSelectedItemId(R.id.navigation_chat);
                    String ChatRoomKey = getIntent().getExtras().get("Key").toString();
                    Intent intent = new Intent(Layout_Manager_BottomNav_Activity.this, Chat_With_Users_Activity.class);
                    intent.putExtra("Key", ChatRoomKey);
                    startActivity(intent);
                } else {
                    if (Type.equals("TextMake")) {
                        bottomNav.setSelectedItemId(R.id.navigation_home);
                        String PostKey = getIntent().getExtras().get("Key").toString();
                        Intent intent = new Intent(Layout_Manager_BottomNav_Activity.this, Post_Viewing_Activity.class);
                        intent.putExtra("Key", PostKey);
                        startActivity(intent);
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                                new HomeFragment()).commit();
                        MakingSelected = false;
                    }
                }
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                    new HomeFragment()).commit();
            MakingSelected = false;
        }

    }

    private void SetupDesign() {

        Toolbar toolbar = findViewById(R.id.action_bar_feed_manager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:

                            selectedFragment = new HomeFragment();
                            CurrentFrag = "Home";

                            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                                    selectedFragment).commit();

                            if (MakingSelected) {
                                FrameLayout frameLayout = findViewById(R.id.framelayout_manager_makepost);
                                Animation animation = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
                                frameLayout.startAnimation(animation);
                                frameLayout.setVisibility(View.GONE);
                            }

                            MakingSelected = false;

                            break;

                        case R.id.navigation_chat:

                            selectedFragment = new ChatFragment();
                            CurrentFrag = "Chat";

                            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                                    selectedFragment).commit();

                            if (MakingSelected) {
                                FrameLayout frameLayout2 = findViewById(R.id.framelayout_manager_makepost);
                                Animation animation2 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
                                frameLayout2.startAnimation(animation2);
                                frameLayout2.setVisibility(View.GONE);
                            }

                            MakingSelected = false;

                            break;

                        case R.id.navigation_make:

                            if (MakingSelected) {

                                FrameLayout frameLayout3 = findViewById(R.id.framelayout_manager_makepost);
                                Animation animation3 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
                                frameLayout3.startAnimation(animation3);
                                frameLayout3.setVisibility(View.GONE);

                                MakingSelected = false;

                            } else {

                                FrameLayout frameLayout4 = findViewById(R.id.framelayout_manager_makepost);
                                frameLayout4.setVisibility(View.VISIBLE);

                                selectedFragment = new MakeFragment();
                                CurrentFrag = "Make";

                                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager_makepost,
                                        selectedFragment).commit();

                                Animation animation4 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_open);
                                frameLayout4.startAnimation(animation4);

                                MakingSelected = true;
                            }

                            break;

                        case R.id.navigation_search:

                            selectedFragment = new SearchFragment();
                            CurrentFrag = "Search";

                            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                                    selectedFragment).commit();

                            if (MakingSelected) {

                                FrameLayout frameLayout5 = findViewById(R.id.framelayout_manager_makepost);
                                Animation animation5 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
                                frameLayout5.startAnimation(animation5);
                                frameLayout5.setVisibility(View.GONE);
                            }

                            MakingSelected = false;

                            break;

                        case R.id.navigation_account:

                            selectedFragment = new AccountFragment();
                            CurrentFrag = "Account";

                            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                                    selectedFragment).commit();

                            if (MakingSelected) {
                                FrameLayout frameLayout6 = findViewById(R.id.framelayout_manager_makepost);
                                Animation animation6 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
                                frameLayout6.startAnimation(animation6);
                                frameLayout6.setVisibility(View.GONE);
                            }

                            MakingSelected = false;

                            break;
                    }

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

    public void onBackPressed() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_manager);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (MakingSelected) {
            FrameLayout frameLayout7 = findViewById(R.id.framelayout_manager_makepost);
            Animation animation7 = AnimationUtils.loadAnimation(Layout_Manager_BottomNav_Activity.this, R.anim.fab_close);
            frameLayout7.startAnimation(animation7);
            MakingSelected = false;
            frameLayout7.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verificationemail = firebaseUser.isEmailVerified();

        if(verificationemail){
            Checked();
        }

        else{
            Toast.makeText(Layout_Manager_BottomNav_Activity.this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
            startActivity(new Intent(Layout_Manager_BottomNav_Activity.this, MainActivity.class));
            finish();
        }
    }
}
