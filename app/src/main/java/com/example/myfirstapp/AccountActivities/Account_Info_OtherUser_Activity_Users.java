package com.example.myfirstapp.AccountActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myfirstapp.Chatroom.ChatPrivateWithUsers;
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
    private Button ChatWithUser;

    private String uid;

    public PageAdapter_HisAccount pagerAdapter;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private static final String TAG = "Account_Info_Other_User";


    private void SetupUI() {

        //voor het geven van kleur aan de status bar:
        Window window = Account_Info_OtherUser_Activity_Users.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Account_Info_OtherUser_Activity_Users.this, R.color.slighly_darker_mainGreen));

        ChatWithUser = findViewById(R.id.btChatWithUserAccountInfo);
        RealName = findViewById(R.id.tvDisplayNameOtherUserAccountViewing);
        UserName = findViewById(R.id.tvUsernameOtherUserAccountViewing);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoViewingOtherUser);

        uid = getIntent().getExtras().get("UID").toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        ChatWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account_Info_OtherUser_Activity_Users.this, ChatPrivateWithUsers.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });

        //user visit count
        final DatabaseReference UserVisitCount = FirebaseDatabase.getInstance().getReference("users").child(uid);
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
