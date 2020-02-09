package com.example.myfirstapp.AccountActivities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

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

public class Account_Info_OtherUser_Activity_Users extends AppCompatActivity {


    private TextView RealName, UserName;
    private ImageView ProfilePicture;
    private Button Follow, Chat;

    private String uid, MyUID, key;

    public PageAdapter_HisAccount pagerAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private static final String TAG = "Account_Info_Other_User";


    private void SetupUI() {

        setTheme(R.style.AppTheme);

        //voor het geven van kleur aan de status bar:
        Window window = Account_Info_OtherUser_Activity_Users.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Account_Info_OtherUser_Activity_Users.this, R.color.slighly_darker_mainGreen));

        Follow = findViewById(R.id.btChatWithUserAccountInfo);
        RealName = findViewById(R.id.tvDisplayNameOtherUserAccountViewing);
        UserName = findViewById(R.id.tvUsernameOtherUserAccountViewing);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoViewingOtherUser);

        uid = getIntent().getExtras().get("UID").toString();

        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference("Private Chatrooms");


        //check if following


        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(uid)) {
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

                                final String UsernameOtherUser = dataSnapshot.child(uid).child("userName").getValue().toString();

                                final DatabaseReference datarefOtherUID = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("followers");
                                datarefUID.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(uid)) {
                                            Log.e("Check", "TRUEE");
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(Account_Info_OtherUser_Activity_Users.this);
                                            dialog.setTitle("Unfollow");
                                            dialog.setMessage("Are you sure you want to unfollow this user?");
                                            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    datarefUID.child(uid).removeValue();
                                                    datarefOtherUID.child(MyUID).removeValue();
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
                                            datarefFollowing.child(MyUID).child("following").child(uid).setValue(UsernameOtherUser);
                                            datarefFollowing.child(uid).child("followers").child(MyUID).setValue(userNameFollower);
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
        final DatabaseReference UserVisitCount = FirebaseDatabase.getInstance().getReference("users").child(uid);
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
    }

    private void RetrieveData() {

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("ProfilePictures").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePicture);
            }
        });

        //voor de rest
        DatabaseReference OtherUserData = firebaseDatabase.getReference("users").child(uid);
        OtherUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                RealName.setText(userProfile.getUserFullName());
                UserName.setText(userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Account_Info_OtherUser_Activity_Users.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__info__other_user_);
        SetupUI();
        RetrieveData();
        SetupTabView();

    }

    private void SetupTabView() {

        TabLayout tabLayout = findViewById(R.id.tab_layout_other_user_account);
        final ViewPager viewPager = findViewById(R.id.viewpager_tablayout_OtherUserAccount);
        pagerAdapter = new PageAdapter_HisAccount(getSupportFragmentManager(), tabLayout.getTabCount(), uid);
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


}
