package com.example.myfirstapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_HisAccount extends FragmentPagerAdapter {

    private int numoftabs;
    private String key;

    public PageAdapter_HisAccount(@NonNull FragmentManager fm, int numOfTabs, String TheKey) {
        super(fm);
        this.numoftabs = numOfTabs;
        this.key = TheKey;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new HisPostsTab(key);

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
