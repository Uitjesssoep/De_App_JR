package com.example.myfirstapp.AccountActivities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
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


    private TextView RealName, UserName, BirthDate, Email, tvOtherUserUID;
    private ImageView ProfilePicture;

    private String key, OtherUserUID;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private static final String TAG = "Account_Info_Other_User";


    private void SetupUI() {

        RealName = findViewById(R.id.tvRealNameAccountInfoOtherUser);
        UserName = findViewById(R.id.tvUsernameAccountInfoOtherUser);
        BirthDate = findViewById(R.id.tvBirthdayAccountInfoOtherUser);
        Email = findViewById(R.id.tvEmailAccountInfoOtherUser);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoOtherUser);

        tvOtherUserUID = findViewById(R.id.tvHiddenOtherUserUIDPlaceholder);

        key = getIntent().getExtras().get("Key").toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

      /*  DatabaseReference databaseReference = firebaseDatabase.getReference("General_Text_Posts").child(key).child("uid");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvOtherUserUID.setText(dataSnapshot.getValue(String.class));
                OtherUserUID = tvOtherUserUID.getText().toString();
                Log.e(TAG, OtherUserUID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void RetrieveData() {

//        Log.e(TAG, OtherUserUID);


        //voor profielfoto
        tvOtherUserUID.setText(key);
        OtherUserUID = tvOtherUserUID.getText().toString();


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("ProfilePictures").child(OtherUserUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePicture);
            }
        });

        //voor de rest
        DatabaseReference OtherUserData = firebaseDatabase.getReference("users").child(OtherUserUID);
        OtherUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                RealName.setText(userProfile.getUserFullName());
                UserName.setText(userProfile.getUserName());
                BirthDate.setText(userProfile.getUserBirthdate());
                Email.setText(userProfile.getUserEmail());
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

    }


}
