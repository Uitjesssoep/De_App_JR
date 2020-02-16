package com.example.myfirstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    public PageAdapter pagerAdapter;
    SharedPrefNightMode sharedPrefNightMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(getActivity());

        if(sharedPrefNightMode.loadNightModeState()==true){
            getContext().setTheme(R.style.AppTheme_Night);
        }
        else getContext().setTheme(R.style.AppTheme);

        return inflater.inflate(R.layout.fragment_homelayout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TabLayout tabLayout = getView().findViewById(R.id.tab_layout_general_feed_home);
        TabItem AllFeed = getView().findViewById(R.id.tab_layout_allFeed_home);
        TabItem FollowingFeed = getView().findViewById(R.id.tab_layout_followingFeed_home);
        final ViewPager viewPager = getView().findViewById(R.id.viewpager_tablayout_home);

        pagerAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
