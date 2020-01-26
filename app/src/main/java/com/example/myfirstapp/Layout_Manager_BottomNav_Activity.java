package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Layout_Manager_BottomNav_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout__manager__bottom_nav_);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_manager);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:

                            selectedFragment = new HomeFragment();

                            break;

                        case R.id.navigation_chat:

                            break;

                        case R.id.navigation_make:

                            break;

                        case R.id.navigation_search:

                            break;

                        case R.id.navigation_account:

                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_manager,
                            selectedFragment).commit();

                    return true;

                }
            };
}
