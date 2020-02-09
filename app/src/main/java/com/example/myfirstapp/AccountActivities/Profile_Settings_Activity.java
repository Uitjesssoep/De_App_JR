package com.example.myfirstapp.AccountActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.App_Settings_Activity;
import com.example.myfirstapp.Credits_Activity;
import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class Profile_Settings_Activity extends AppCompatActivity {


    private static final String TAG = "Profile_Settings_Act";

    private ImageView ChangeProfilePicture;
    private ImageButton Exit;
    private EditText DisplayNameLayout;
    private TextView ErrorDisplayName;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;


    private static int PICK_IMAGE = 123; // geef altijd een unieke requestcode zodat het kan worden gecontroleerd en niks dubbel is dadelijk
    Uri imagePath;

    String ChangeNameString, ChangeEmailString, ChangeBirthdateString, ChangeFullNameString, Profilepicture, UID;



    // Image moet kunnen worden gekozen uit gallerij, code is van documentatie van internet
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                ChangeProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__settings);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference();
        StorageReference myRef1 = storageReference.child(firebaseAuth.getUid());
        ErrorDisplayName = findViewById(R.id.tvFullNameErrorProfileSettings);

        ErrorDisplayName.setVisibility(View.GONE);

        DisplayNameLayout = findViewById(R.id.inputlayoutDisplayNameUpdate);

        ChangeProfilePicture = (ImageView) findViewById(R.id.ivProfilePictureChange);

        SetupDesign();

        ChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");  //alle image types worden geaccepteerd
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
            }
        });



        StorageReference myref2 = firebaseStorage.getReference();
        myref2.child("ProfilePictures").child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
        Picasso.get().load(uri).fit().centerCrop().into(ChangeProfilePicture);
        Profilepicture=uri.toString();
            }
        });


        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DisplayNameLayout.setText(dataSnapshot.child("userFullName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile_Settings_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ClickSave() {

        ErrorDisplayName.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();

        final StorageReference storageReference = firebaseStorage.getReference();
        StorageReference myRef1 = storageReference.child(firebaseAuth.getUid());
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        UID = firebaseAuth.getUid();
        ChangeFullNameString = DisplayNameLayout.getText().toString();
        int DNLength = ChangeFullNameString.length();
        DisplayNameLayout.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        if (ChangeFullNameString.isEmpty()) {
            ErrorDisplayName.setVisibility(View.VISIBLE);
            ErrorDisplayName.setText("Please enter a display name");
            DisplayNameLayout.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
        }

        else {

            if(DNLength < 3){
                ErrorDisplayName.setVisibility(View.VISIBLE);
                ErrorDisplayName.setText("Make sure your display name is at least 3 characters long");
                DisplayNameLayout.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
            }
            else{
                if (imagePath != null) {

                    StorageReference imageReference = storageReference.child("ProfilePictures").child(firebaseAuth.getUid());
                    UploadTask uploadTask = imageReference.putFile(imagePath);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile_Settings_Activity.this, "Couldn't upload image to database", Toast.LENGTH_LONG).show();
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });

                    DatabaseReference GetName = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
                    GetName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ChangeNameString = dataSnapshot.child("userName").getValue().toString();
                            ChangeEmailString = dataSnapshot.child("userEmail").getValue().toString();
                            ChangeBirthdateString = dataSnapshot.child("userBirthdate").getValue().toString();

                            UserProfileToDatabase userProfileToDatabase = new UserProfileToDatabase(Profilepicture, UID, ChangeNameString, ChangeEmailString, ChangeFullNameString, ChangeBirthdateString);

                            databaseReference.setValue(userProfileToDatabase);

                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {

                    DatabaseReference GetName = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
                    GetName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ChangeNameString = dataSnapshot.child("userName").getValue().toString();
                            ChangeEmailString = dataSnapshot.child("userEmail").getValue().toString();
                            ChangeBirthdateString = dataSnapshot.child("userBirthdate").getValue().toString();

                            UserProfileToDatabase userProfileToDatabase = new UserProfileToDatabase(Profilepicture, UID, ChangeNameString, ChangeEmailString, ChangeFullNameString, ChangeBirthdateString);

                            databaseReference.setValue(userProfileToDatabase);

                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = Profile_Settings_Activity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Profile_Settings_Activity.this, R.color.slighly_darker_mainGreen));

        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar_display);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exitdisplaysettings);
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
        inflater.inflate(R.menu.menu_actionbar_savesettings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){


            case R.id.action_save_settings:

                ClickSave();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
