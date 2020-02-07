package com.example.myfirstapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_HisAccount extends FragmentPagerAdapter {

    private int numoftabs;
    private String uid;

    public PageAdapter_HisAccount(@NonNull FragmentManager fm, int numOfTabs, String TheUID) {
        super(fm);
        this.numoftabs = numOfTabs;
        this.uid = TheUID;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new HisPostsTab(uid);

            case 1:
                return new HisCommentsTab(uid);

            case 2:
                return new HisAboutTab(uid);

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
