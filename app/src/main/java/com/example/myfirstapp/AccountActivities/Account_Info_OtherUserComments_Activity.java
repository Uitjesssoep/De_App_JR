package com.example.myfirstapp.AccountActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Account_Info_OtherUserComments_Activity extends AppCompatActivity {
    private TextView RealName, UserName, BirthDate, Email, tvOtherUserUID;
    private ImageView ProfilePicture;

    private String PostKey, CommentKey, OtherUserUIDComments;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private static final String TAG = "AccountInfo_Other_UserC";


    private void SetupUI() {

        RealName = findViewById(R.id.tvRealNameAccountInfoOtherUserComments);
        UserName = findViewById(R.id.tvUsernameAccountInfoOtherUserComments);
        BirthDate = findViewById(R.id.tvBirthdayAccountInfoOtherUserComments);
        Email = findViewById(R.id.tvEmailAccountInfoOtherUserComments);
        ProfilePicture = findViewById(R.id.ivProfilePictureAccountInfoOtherUserComments);

        tvOtherUserUID = findViewById(R.id.tvHiddenOtherUserCommentsUIDPlaceholder);

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
                BirthDate.setText(userProfile.getUserBirthdate());
                Email.setText(userProfile.getUserEmail());
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
        setContentView(R.layout.activity_account__info__other_user_comments_);

        SetupUI();

    }
}
