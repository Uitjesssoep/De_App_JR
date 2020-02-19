package com.example.myfirstapp.AccountActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class Deleting_Account_Activity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private String MyUID;
    SharedPrefNightMode sharedPrefNightMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleting__account_);

        setTheme(R.style.AppTheme);


        SetupUI();

        String userIDforDelete = firebaseAuth.getUid().toString();
        deleteUser(userIDforDelete);

    }

    private void SetupUI() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        MyUID=firebaseAuth.getCurrentUser().getUid();

    }

    private void deleteUser(String userID){
        final DatabaseReference drUser = firebaseDatabase.getReference("users").child(userID);
        final StorageReference srUser = firebaseStorage.getReference("ProfilePictures").child(firebaseAuth.getUid());
        final DatabaseReference drChat =firebaseDatabase.getReference("Messages").child(MyUID);
        final DatabaseReference drChatPrivatae = firebaseDatabase.getReference("Private Chatrooms").child(MyUID);
        final StorageReference srChat = firebaseStorage.getReference("ChatPrivate");



        drUser.child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String username = dataSnapshot.getValue().toString();

                DatabaseReference RemoveUsername = firebaseDatabase.getReference("Usernames").child(username);
                RemoveUsername.removeValue();

                drUser.removeValue();
                srUser.delete();

                drChat.removeValue();
                srChat.delete();
                drChatPrivatae.removeValue();

                DeleteAccount();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DeleteAccount() {
        Log.e("Test", "DeleteAccount bereikt ");
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Deleting_Account_Activity.this, "Account deleted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Deleting_Account_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                    firebaseAuth.signOut();
                }

                else{
                    Toast.makeText(Deleting_Account_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onBackPressed(){

    }

}
