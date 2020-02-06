package com.example.myfirstapp.Imageposts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SecondActivity;
import com.example.myfirstapp.Textposts.Post_Viewing_Activity;
import com.example.myfirstapp.Textposts.StuffForPost;
import com.example.myfirstapp.Textposts.Upload_TextPost_Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Upload_Images_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth firebaseAuth;
    private TextView ChooseImage;
    private EditText Title;
    private LinearLayout SelectImage;
    private ImageView mImageView, ChooseImageVIew;
    private String MyUID, usernameString, temp_key, UriImage, Date, key;
    private Uri mImageUri;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageButton Exit;
    private CheckBox Anon;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private StorageTask mUploadTask;

    private void SetupUI() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        MyUID = user.getUid();
        ChooseImage = findViewById(R.id.tvChooseImage);
        ChooseImageVIew = findViewById(R.id.ivChooseImageUpload);
        SelectImage = findViewById(R.id.linlayhorSelectImage);
        Title = findViewById(R.id.etImageName);
        mImageView = findViewById(R.id.ivUploadedImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("General_Posts");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("General_Posts");
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());
        Anon = findViewById(R.id.cbPostAnonImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        SetupUI();
        SetupDesign();

        ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        ChooseImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
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

        final DatabaseReference PostCounter = FirebaseDatabase.getInstance().getReference("users").child(MyUID);
        PostCounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Counters") && dataSnapshot.child("Counters").hasChild("PostCount")){

                    String PostCountString = dataSnapshot.child("Counters").child("PostCount").getValue().toString();
                    int PostCountInt = Integer.parseInt(PostCountString);
                    PostCountInt = Integer.valueOf(PostCountInt + 1);
                    String NewPostCountString = Integer.toString(PostCountInt);
                    PostCounter.child("Counters").child("PostCount").setValue(NewPostCountString);

                }

                else{
                    PostCounter.child("Counters").child("PostCount").setValue("1");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(MyUID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(Anon.isChecked()){
                    usernameString = "[anonymous]";
                    temp_key = mDatabaseRef.push().getKey();
                    StuffForPost stuffForPost = new StuffForPost(Title.getText().toString().trim(),
                            usernameString, UriImage, MyUID, temp_key, Date, "Image");
                    mDatabaseRef.child(temp_key).setValue(stuffForPost);
                    Intent VNoD = new Intent(Upload_Images_Activity.this, Post_Viewing_Activity.class);
                    VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    VNoD.putExtra("Key", temp_key);
                    startActivity(VNoD);
                    finish();
                }
                if(!Anon.isChecked()) {
                    UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                    usernameString = userProfile.getUserName();

                    temp_key = mDatabaseRef.push().getKey();
                    StuffForPost stuffForPost = new StuffForPost(Title.getText().toString().trim(),
                            usernameString, UriImage, MyUID, temp_key, Date, "Image");
                    mDatabaseRef.child(temp_key).setValue(stuffForPost);
                    Intent VNoD = new Intent(Upload_Images_Activity.this, Post_Viewing_Activity.class);
                    VNoD.setFlags(FLAG_ACTIVITY_NEW_TASK);
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

    private void uploadFile() {
        if (mImageUri != null) {
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

                                    final ProgressDialog dialog = new ProgressDialog(Upload_Images_Activity.this);
                                    dialog.setTitle("Uploading image");
                                    dialog.setMessage("Please wait");
                                    dialog.show();
                                    hideKeyboard(Upload_Images_Activity.this);

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
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:
        Window window = Upload_Images_Activity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Upload_Images_Activity.this, R.color.slighly_darker_mainGreen));

        //action bar ding
        Toolbar toolbar = findViewById(R.id.action_bar_makeimagepost);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exitmakecommenttextpost);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_makecomment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post_comment:
                if (mUploadTask != null) {
                    Toast.makeText(Upload_Images_Activity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    String TitleString = Title.getText().toString();
                    if(TitleString.isEmpty()){
                        Toast.makeText(Upload_Images_Activity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        uploadFile();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
