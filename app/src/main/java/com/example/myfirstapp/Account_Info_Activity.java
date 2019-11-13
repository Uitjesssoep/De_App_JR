package com.example.myfirstapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Account_Info_Activity extends AppCompatActivity {


    //hey

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private Button LogOut;
    private Button ProfileSettings;

    private ImageView ProfilePictureProfile;
    private TextView RealNameProfile, EmailProfile, BirthdayProfile, UsernameProfile;
    private TextView DeleteAccount;

    private String userIDforDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__info_);
//the big test!!!
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        LogOut = (Button)findViewById(R.id.btnLogOut);
        ProfileSettings = (Button)findViewById(R.id.btnProfileSettings);
        ProfilePictureProfile = (ImageView)findViewById(R.id.ivProfilePictureAccountInfo);
        RealNameProfile = (TextView)findViewById(R.id.tvRealNameAccountInfo);
        EmailProfile = (TextView)findViewById(R.id.tvEmailAccountInfo);
        BirthdayProfile = (TextView)findViewById(R.id.tvBirthdayAccountInfo);
        UsernameProfile = (TextView)findViewById(R.id.tvUsernameAccountInfo);
        DeleteAccount = (TextView)findViewById(R.id.tvDeleteAccount);


        userIDforDelete = firebaseAuth.getUid().toString();


        //underline de delete account text
        DeleteAccount.setPaintFlags(DeleteAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Account_Info_Activity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting your account will permanently delete your account and all of its contents and cannot be reversed");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Account_Info_Activity.this, "Account deleted", Toast.LENGTH_LONG).show();
                                    firebaseAuth.signOut();
                                    deleteUser(userIDforDelete);

                                    startActivity(new Intent(Account_Info_Activity.this, MainActivity.class));
                                    finish();
                                }

                                else{
                                    Toast.makeText(Account_Info_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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
            }
        });


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images").child("ProfilePicture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePictureProfile);
                }
        });


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Account_Info_Activity.this, MainActivity.class));
            }
        });

        ProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account_Info_Activity.this.finish();
                startActivity(new Intent(Account_Info_Activity.this, Profile_Settings_Activity.class));
            }
        });


        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                RealNameProfile.setText(userProfile.getUserFullName());
                UsernameProfile.setText(userProfile.getUserName());
                BirthdayProfile.setText(userProfile.getUserBirthdate());
                EmailProfile.setText(userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(Account_Info_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
        }
    });


    }

    private void deleteUser(String userID){
        DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("users").child(userID);
        StorageReference srUser = FirebaseStorage.getInstance().getReference(userID).child("Images").child("ProfilePicture");

        drUser.removeValue();
        srUser.delete();
    }


}
