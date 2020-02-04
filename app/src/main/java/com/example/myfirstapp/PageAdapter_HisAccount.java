package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_HisAccount extends FragmentPagerAdapter {

    private String Key;
    private int numoftabs;


    public PageAdapter_HisAccount(@NonNull FragmentManager fm, int numOfTabs, String TheKey) {
        super(fm);
        this.numoftabs = numOfTabs;
        this.Key = TheKey;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new HisPostsTab();

            case 1:
                return new HisCommentsTab();

            case 2:
                return new HisAboutTab();

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
