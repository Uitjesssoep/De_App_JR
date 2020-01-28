package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_ChatRooms extends FragmentPagerAdapter {


    private int numoftabs;


    public PageAdapter_ChatRooms(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numoftabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new AllChatsTab();

            case 1:
                return new PrivateChatsTab();

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
