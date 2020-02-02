package com.example.myfirstapp.AccountActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.PageAdapter_HisAccount;
import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Account_Info_OtherUserComments_Activity extends AppCompatActivity {
    private TextView RealName, UserName;
    private ImageView ProfilePicture;

    public PageAdapter_HisAccount pagerAdapter;

    private String PostKey, CommentKey, OtherUserUIDComments;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private static final String TAG = "AccountInfo_Other_UserC";


    private void SetupUI() {

        RealName = findViewById(R.id.tvDisplayNameOtherUserAccountViewing);
        UserName = findViewById(R.id.tvUsernameOtherUserAccountViewing);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoViewingOtherUser);

        PostKey = getIntent().getExtras().get("PostKey").toString();
        CommentKey = getIntent().getExtras().get("CommentKey").toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("General_Posts").child(PostKey).child("Comments").child(CommentKey).child("uid");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "Uit database: " + dataSnapshot.getValue().toString());
                OtherUserUIDComments = dataSnapshot.getValue().toString();
                Log.e(TAG, "In String: " + OtherUserUIDComments);

                //user visit count
                final DatabaseReference UserVisitCount = FirebaseDatabase.getInstance().getReference("users").child(OtherUserUIDComments);
                UserVisitCount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("AccountVisits")) {
                            String VisitCountString = dataSnapshot.child("Counters").child("AccountVisits").getValue().toString();
                            int VisitCountInt = Integer.parseInt(VisitCountString);
                            VisitCountInt = Integer.valueOf(VisitCountInt + 1);
                            String NewVisitCountString = Integer.toString(VisitCountInt);
                            UserVisitCount.child("Counters").child("AccountVisits").setValue(NewVisitCountString);
                        }
                        else{
                            UserVisitCount.child("Counters").child("AccountVisits").setValue("1");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //hopelijk werkt t

                RetrieveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RetrieveData() {

        Log.e(TAG, OtherUserUIDComments);

        //voor profielfoto
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(OtherUserUIDComments).child("Images").child("ProfilePicture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePicture);
            }
        });

        //voor de rest
        DatabaseReference OtherUserData = firebaseDatabase.getReference("users").child(OtherUserUIDComments);
        OtherUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                RealName.setText(userProfile.getUserFullName());
                UserName.setText(userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Account_Info_OtherUserComments_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__info__other_user_);

        SetupUI();

        SetupTabView();

    }

    private void SetupTabView() {

        TabLayout tabLayout = findViewById(R.id.tab_layout_other_user_account);
        final ViewPager viewPager = findViewById(R.id.viewpager_tablayout_OtherUserAccount);
        pagerAdapter = new PageAdapter_HisAccount(getSupportFragmentManager(), tabLayout.getTabCount());
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
                else if(tab.getPosition() == 2){
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
