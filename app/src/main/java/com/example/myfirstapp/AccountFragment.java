package com.example.myfirstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    public PageAdapter_MyAccount pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accountlayout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        
        AccountInfoLoading();
        
        TabLayout tabLayout = getView().findViewById(R.id.tab_layout_personal_account);
        TabItem MyPosts = getView().findViewById(R.id.tab_layout_MyPosts);
        TabItem SavedPosts = getView().findViewById(R.id.tab_layout_SavedPosts);
        final ViewPager viewPager = getView().findViewById(R.id.viewpager_tablayout_MyAccount);

        pagerAdapter = new PageAdapter_MyAccount(getFragmentManager(), tabLayout.getTabCount());
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

    private void AccountInfoLoading() {

        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference GetAccountInfo = FirebaseDatabase.getInstance().getReference("users").child(MyUID);

        GetAccountInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ImageView ProfilePicture = getView().findViewById(R.id.ivProfilePictureAccountInfoViewing);
                TextView DisplayName = getView().findViewById(R.id.tvDisplayNameMyAccountViewing);
                TextView UserName = getView().findViewById(R.id.tvUsernameMyAccountViewing);

                String DN = dataSnapshot.child("userFullName").getValue().toString();
                DisplayName.setText(DN);

                String UN = dataSnapshot.child("userName").getValue().toString();
                UserName.setText(UN);

                String PF = dataSnapshot.child("profilePicture").getValue().toString();
                Picasso.get().load(PF).fit().centerCrop().into(ProfilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
