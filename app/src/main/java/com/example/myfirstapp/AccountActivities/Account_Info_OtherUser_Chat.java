package com.example.myfirstapp.AccountActivities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myfirstapp.PageAdapter_HisAccount;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;
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

public class Account_Info_OtherUser_Chat extends AppCompatActivity {


    private TextView RealName, UserName;
    private ImageView ProfilePicture;

    private String UID, OtherUserUID;

    public PageAdapter_HisAccount pagerAdapter;

    private Button Follow;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    SharedPrefNightMode sharedPrefNightMode;

    private static final String TAG = "Account_Info_chatList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if (sharedPrefNightMode.loadNightModeState() == true) {
            setTheme(R.style.AppTheme_Night);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__info__other_user_);

        SetupUI();
        SetupTabView();

    }


    private void SetupTabView() {

        final TabLayout tabLayout = findViewById(R.id.tab_layout_other_user_account);
        final ViewPager viewPager = findViewById(R.id.viewpager_tablayout_OtherUserAccount);

        String HisUID = getIntent().getExtras().get("UID").toString();
        pagerAdapter = new PageAdapter_HisAccount(getSupportFragmentManager(), tabLayout.getTabCount(), HisUID);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
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


    private void SetupUI() {

        RealName = findViewById(R.id.tvDisplayNameOtherUserAccountViewing);
        UserName = findViewById(R.id.tvUsernameOtherUserAccountViewing);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoViewingOtherUser);

        Follow = findViewById(R.id.btChatWithUserAccountInfo);

        UID = getIntent().getExtras().get("UID").toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(UID)) {
                    Follow.setBackgroundResource(R.drawable.button_roundedcorners_following);
                    Follow.setText("Following");
                    Follow.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    Follow.setText("Follow");
                    Follow.setBackgroundResource(R.drawable.button_roundedcorners_follow);
                    Follow.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference datarefFollower = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("userName");
                datarefFollower.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String userNameFollower = dataSnapshot.getValue().toString();

                        final DatabaseReference datarefUID = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");
                        final DatabaseReference datarefFollowing = FirebaseDatabase.getInstance().getReference().child("users");

                        datarefFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String UsernameOtherUser = dataSnapshot.child(UID).child("userName").getValue().toString();

                                final DatabaseReference datarefOtherUID = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("followers");
                                datarefUID.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(UID)) {
                                            Log.e("Check", "TRUEE");
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(Account_Info_OtherUser_Chat.this);
                                            dialog.setTitle("Unfollow");
                                            dialog.setMessage("Are you sure you want to unfollow this user?");
                                            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    datarefUID.child(UID).removeValue();
                                                    datarefOtherUID.child(MyUID).removeValue();
                                                    ;
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            AlertDialog alertDialog = dialog.create();
                                            alertDialog.show();
                                        } else {
                                            datarefFollowing.child(MyUID).child("following").child(UID).setValue(UsernameOtherUser);
                                            datarefFollowing.child(UID).child("followers").child(MyUID).setValue(userNameFollower);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        //user visit count
        final DatabaseReference UserVisitCount = FirebaseDatabase.getInstance().getReference("users").child(UID);
        UserVisitCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("AccountVisits")) {
                    String VisitCountString = dataSnapshot.child("Counters").child("AccountVisits").getValue().toString();
                    int VisitCountInt = Integer.parseInt(VisitCountString);
                    VisitCountInt = Integer.valueOf(VisitCountInt + 1);
                    String NewVisitCountString = Integer.toString(VisitCountInt);
                    UserVisitCount.child("Counters").child("AccountVisits").setValue(NewVisitCountString);
                } else {
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

    private void RetrieveData() {

        Log.e(TAG, UID);

        //voor profielfoto
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("ProfilePictures").child(UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePicture);
            }
        });

        //voor de rest
        DatabaseReference OtherUserData = firebaseDatabase.getReference("users").child(UID);
        OtherUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                RealName.setText(userProfile.getUserFullName());
                UserName.setText(userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Account_Info_OtherUser_Chat.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
