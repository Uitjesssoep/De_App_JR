package com.example.myfirstapp.Chatroom;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    private DatabaseReference mDatabaseRef;
    private String Message;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_temporary_viewing_private_chat);
        SendImage = findViewById(R.id.ibSendImageToChat);
        UID = getIntent().getExtras().get("UID").toString();
        Message = getIntent().getExtras().get("Message").toString();
        mImageUri = Uri.parse(getIntent().getExtras().get("mImageUri").toString());
        Image = findViewById(R.id.ivImageTemporaryForChat);
        mStorageRef = FirebaseStorage.getInstance().getReference("Chat");
        MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Messages");
        ChatInputText = findViewById(R.id.etMessageWithImageForChat);

        Picasso.get().load(mImageUri).fit().centerCrop().into(Image);
        ChatInputText.setText(Message);
        calendar = Calendar.getInstance();
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

        String mMessage= ChatInputText.getText().toString();
        temp_key = mDatabaseRef.push().getKey();
        PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(mMessage, "image", false, Date, MyUID, UriImage);
        mDatabaseRef.child(MyUID).child(UID).child(temp_key).setValue(postStuffForChatRoom);
        mDatabaseRef.child(UID).child(MyUID).child(temp_key).setValue(postStuffForChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
      /*  Intent VNoD = new Intent(ImageTemporaryViewingPrivateChat.this, Layout_Manager_BottomNav_Activity.class);
        VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
        VNoD.putExtra("Type", "TextMake");
        VNoD.putExtra("Key", temp_key);
        startActivity(VNoD);*/
    }

}
