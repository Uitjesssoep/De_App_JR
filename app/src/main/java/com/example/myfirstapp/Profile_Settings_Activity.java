package com.example.myfirstapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class Profile_Settings_Activity extends AppCompatActivity {


    private static final String TAG = "Profile_Settings_Act";

    private EditText ChangeFullName;
    private TextView ChangeBirthdate;
    private ImageView ChangeProfilePicture;
    private Button SaveChangesProfile, ChangePasswordButton;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

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

        ChangeFullName = (EditText)findViewById(R.id.etFullNameChange);
        ChangeBirthdate = (TextView) findViewById(R.id.tvBirthdateChange);
        ChangeProfilePicture = (ImageView) findViewById(R.id.ivProfilePictureChange);
        SaveChangesProfile = (Button) findViewById(R.id.btnSaveChangesProfileSettings);
        ChangePasswordButton = (Button)findViewById(R.id.btnChangePassword);





        //date picker deel drop ik maar ff hier

        ChangeBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Profile_Settings_Activity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yy: " + day + month + year);

                String date = day + "/" + month + "/" + year;
                ChangeBirthdate.setText(date);
            }
        };







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
        myref2.child(firebaseAuth.getUid()).child("Images").child("ProfilePicture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
        Picasso.get().load(uri).fit().centerCrop().into(ChangeProfilePicture);
            }
        });


        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                ChangeFullName.setText(userProfile.getUserFullName());
                ChangeBirthdate.setText(userProfile.getUserBirthdate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile_Settings_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });


        SaveChangesProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profilepicture = "e";
                UID = firebaseAuth.getUid();
                ChangeBirthdateString = ChangeBirthdate.getText().toString();
                ChangeFullNameString = ChangeFullName.getText().toString();

                                if (ChangeFullNameString.isEmpty()) {
                                    Toast.makeText(Profile_Settings_Activity.this, "Please make sure you have filled in your full name", Toast.LENGTH_LONG).show();
                                }

                                else{
                                    if(!ChangeFullNameString.matches("[a-zA-Z ]*")){
                                        Toast.makeText(Profile_Settings_Activity.this, "Please make sure your full name contains only letters (a-z, A-Z) and spaces ( )", Toast.LENGTH_LONG).show();
                                    }

                                    else{

                                        if(imagePath != null) {

                                            StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("ProfilePicture");
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
                                            GetName.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    ChangeNameString = dataSnapshot.child("userName").getValue().toString();
                                                    ChangeEmailString = dataSnapshot.child("userEmail").getValue().toString();

                                                    UserProfileToDatabase userProfileToDatabase = new UserProfileToDatabase(Profilepicture, UID, ChangeNameString, ChangeEmailString, ChangeFullNameString, ChangeBirthdateString);

                                                    databaseReference.setValue(userProfileToDatabase);

                                                    startActivity(new Intent(Profile_Settings_Activity.this, Loading_Screen_Activity.class));
                                                    finish();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        else{

                                            DatabaseReference GetName = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
                                            GetName.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    ChangeNameString = dataSnapshot.child("userName").getValue().toString();
                                                    ChangeEmailString = dataSnapshot.child("userEmail").getValue().toString();

                                                    UserProfileToDatabase userProfileToDatabase = new UserProfileToDatabase(Profilepicture, UID, ChangeNameString, ChangeEmailString, ChangeFullNameString, ChangeBirthdateString);

                                                    databaseReference.setValue(userProfileToDatabase);

                                                    startActivity(new Intent(Profile_Settings_Activity.this, Loading_Screen_Activity.class));
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
        });

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Profile_Settings_Activity.this, Change_Password_Activity.class));
            }
        });

    }

    public void onBackPressed(){
        startActivity(new Intent(Profile_Settings_Activity.this, Account_Info_Activity.class));
        finish();
    }

}
