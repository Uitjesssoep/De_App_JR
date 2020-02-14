package com.example.myfirstapp.Chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Imageposts.Upload_Images_Activity;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Textposts.StuffForPost;
import com.example.myfirstapp.Users.UID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.myfirstapp.Chatroom.Chatrooms_Post_Activity.hideKeyboard;

public class ImageTemporaryViewingPrivateChat extends AppCompatActivity {
    private ImageView Image = findViewById(R.id.ivImageTemporaryForChat);
    private ImageButton SendImage = findViewById(R.id.ibSendImageToChat);
    private EditText ChatInputText;
    private Uri mImageUri = (Uri) getIntent().getExtras().get("ImageUri");
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private String MyUID, usernameString, temp_key, UriImage, Date, key, UID;
    private DatabaseReference mDatabaseRef;
    private String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_temporary_viewing_private_chat);

        mStorageRef = FirebaseStorage.getInstance().getReference("General_Posts");
        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UID = getIntent().getExtras().get("UID").toString();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("messages").child(MyUID).child(UID);
        ChatInputText = findViewById(R.id.etChatInputPrivate);
        Message = getIntent().getExtras().get("Message").toString();
        Picasso.get().load(mImageUri).fit().centerCrop().into(Image);
        ChatInputText.setText(Message);
        SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getFileExtension(Uri uri) { //om extension van bestand te krijgen
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {

            final ProgressDialog dialog = new ProgressDialog(ImageTemporaryViewingPrivateChat.this);
            dialog.setTitle("Uploading image");
            dialog.setMessage("Please wait");
            dialog.show();
            hideKeyboard(ImageTemporaryViewingPrivateChat.this);

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UriImage = uri.toString();

                                    setDatabase();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageTemporaryViewingPrivateChat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

    private void setDatabase() {

        final DatabaseReference PostCounter = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        PostCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("PostCount")) {

                    String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                    int PostCountInt = Integer.parseInt(PostCountString);
                    PostCountInt = Integer.valueOf(PostCountInt + 1);
                    String NewPostCountString = Integer.toString(PostCountInt);
                    PostCounter.child("Counters").child("PostCount").setValue(NewPostCountString);

                } else {
                    PostCounter.child("Counters").child("PostCount").setValue("1");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(MyUID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (Anon.isChecked()) {
                    usernameString = "[anonymous]";
                    temp_key = mDatabaseRef.push().getKey();
                    StuffForPost stuffForPost = new StuffForPost(Title.getText().toString().trim(),
                            usernameString, UriImage, MyUID, temp_key, Date, "Image");
                    mDatabaseRef.child(temp_key).setValue(stuffForPost);
                    Intent VNoD = new Intent(Upload_Images_Activity.this, Layout_Manager_BottomNav_Activity.class);
                    VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    VNoD.putExtra("Type", "TextMake");
                    VNoD.putExtra("Key", temp_key);
                    startActivity(VNoD);
                    finish();
                }
                if (!Anon.isChecked()) {
                    UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                    usernameString = userProfile.getUserName();

                    temp_key = mDatabaseRef.push().getKey();
                    StuffForPost stuffForPost = new StuffForPost(Title.getText().toString().trim(),
                            usernameString, UriImage, MyUID, temp_key, Date, "Image");
                    mDatabaseRef.child(temp_key).setValue(stuffForPost);
                    Intent VNoD = new Intent(Upload_Images_Activity.this, Layout_Manager_BottomNav_Activity.class);
                    VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    VNoD.putExtra("Type", "TextMake");
                    VNoD.putExtra("Key", temp_key);
                    startActivity(VNoD);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Upload_Images_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
