package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Upload_Images_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private FirebaseAuth firebaseAuth;
    private Button ChooseImage, Post;
    private EditText Title;
    private ImageView mImageView;
    private ProgressBar mProgressbar;
    private String MyUID, usernameString, temp_key, UriImage, Date;
    private TextView user_name_gebruiker;
    private Uri mImageUri;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private StorageTask mUploadTask;

    private void SetupUI(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        MyUID = user.getUid();
        ChooseImage = findViewById(R.id.btnChooseImage);
        Post = findViewById(R.id.btPostImage);
        Title = findViewById(R.id.etImageName);
        mImageView = findViewById(R.id.ivUploadedImage);
        user_name_gebruiker = findViewById(R.id.tvUserNameHiddenDing);
        mProgressbar = findViewById(R.id.pbUploadingImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("General_Image_Posts");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("General_Image_Posts");
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);
        SetupUI();

        ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(Upload_Images_Activity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }else
                uploadFile();
            }
        });
            }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).fit().centerCrop().into(mImageView);
        }
    }


    private String getFileExtension(Uri uri) { //om extension van bestand te krijgen
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void setDatabase() {
      DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(MyUID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                user_name_gebruiker.setText(userProfile.getUserName());

                usernameString = user_name_gebruiker.getText().toString();

                temp_key = mDatabaseRef.push().getKey();
                Upload upload = new Upload(Title.getText().toString().trim(),
                        usernameString, UriImage, MyUID, temp_key, Date);
                mDatabaseRef.child(temp_key).setValue(upload);
                Intent VNoD = new Intent(Upload_Images_Activity.this, SecondActivity.class);
                startActivity(VNoD);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Upload_Images_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressbar.setProgress(0);
                                }
                            },500);


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(Upload_Images_Activity.this, "Post successful", Toast.LENGTH_SHORT).show();
                                    UriImage = uri.toString();
                                /*   Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                            uri.toString(), MyUID, usernameString, temp_key);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);*/
                                    setDatabase();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Images_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressbar.setProgress((int) progress);
                        }
                    });}
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

 /*   private void openImagesActivity(){
        Intent intent = new Intent(this, ImagesFeed.class);
        startActivity(intent);
    }*/
}
