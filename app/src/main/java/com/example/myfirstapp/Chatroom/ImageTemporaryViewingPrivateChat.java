package com.example.myfirstapp.Chatroom;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.myfirstapp.Chatroom.Chatrooms_Post_Activity.hideKeyboard;

public class ImageTemporaryViewingPrivateChat extends AppCompatActivity {
    private ImageView Image;
    private ImageButton SendImage;
    private EditText ChatInputText;
    private Uri mImageUri;
    private String mImageUriString;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private String MyUID, usernameString, temp_key, UriImage, Date, key, UID;
    private DatabaseReference mDatabaseRef, mDatabaseRefRooms;
    private String Message;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private long Timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_temporary_viewing_private_chat);
        SendImage = findViewById(R.id.ibSendImageToChat);
        UID = getIntent().getExtras().get("UID").toString();
        Message = getIntent().getExtras().get("Message").toString();
        mImageUri = Uri.parse(getIntent().getExtras().get("mImageUri").toString());
        Image = findViewById(R.id.ivImageTemporaryForChat);
        key = getIntent().getExtras().get("key").toString();

        mStorageRef = FirebaseStorage.getInstance().getReference("Chat");
        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Messages");
        ChatInputText = findViewById(R.id.etMessageWithImageForChat);
        Timestamp = System.currentTimeMillis();
        Picasso.get().load(mImageUri).into(Image);
        ChatInputText.setText(Message);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Timestamp);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());

        SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
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

                                    SendImage();
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

    private void SendImage() {

        if (key.equals("no key")) {
            String mMessage = ChatInputText.getText().toString();
            temp_key = mDatabaseRef.push().getKey();
            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(mMessage, "image", false, System.currentTimeMillis(), MyUID, UriImage);
            mDatabaseRef.child(MyUID).child(UID).child(temp_key).setValue(postStuffForChatRoom);
            mDatabaseRef.child(UID).child(MyUID).child(temp_key).setValue(postStuffForChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            });
        } else {
            String mMessage = ChatInputText.getText().toString();

            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(mMessage, "image", false, System.currentTimeMillis(), MyUID, UriImage);
            mDatabaseRefRooms = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("messages");
            temp_key = mDatabaseRefRooms.push().getKey();
            Log.e("key", key );
            mDatabaseRefRooms.child(temp_key).setValue(postStuffForChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            });

        }
      /*  Intent VNoD = new Intent(ImageTemporaryViewingPrivateChat.this, Layout_Manager_BottomNav_Activity.class);
        VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
        VNoD.putExtra("Type", "TextMake");
        VNoD.putExtra("Key", temp_key);
        startActivity(VNoD);*/
    }

}
