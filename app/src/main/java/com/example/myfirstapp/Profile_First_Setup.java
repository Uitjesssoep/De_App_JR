package com.example.myfirstapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Profile_First_Setup extends AppCompatActivity {


    private static final String TAG = "Profile_First_Setup";

    private EditText FullNameSetup;
    private ImageView ProfilePictureSetup;
    private TextView BirthdatePickSetup, PlaceHolderUNameSetup, PlaceHolderEmailSetup;
    private Button ContinueSetup;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private String usernameToDatabase, useremailToDatabase, userfullnameToDatabase, userbirthdateToDatabase;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private static int PICK_IMAGE = 234;
    Uri imagePath;

    private Boolean PickedImage = false;



    //afbeelding kiezen uit gallerij

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                ProfilePictureSetup.setImageBitmap(bitmap);
                PickedImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__first__setup);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FullNameSetup = (EditText)findViewById(R.id.etFullNameSetup);
        ContinueSetup = (Button)findViewById(R.id.btnContinueSetupProfile);
        ProfilePictureSetup = (ImageView)findViewById(R.id.ivProfilePictureFirstSetup);
        BirthdatePickSetup = (TextView)findViewById(R.id.tvSelectBirthdateSetup);
        PlaceHolderEmailSetup = (TextView)findViewById(R.id.tvPlaceHolderEmailSetup);
        PlaceHolderUNameSetup = (TextView)findViewById(R.id.tvPlaceHolderUserNameSetup);








        //profielfoto gebeuren

        ProfilePictureSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");  //alle image types worden geaccepteerd
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
            }
        });






        //date picker deel

        BirthdatePickSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Profile_First_Setup.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
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
                BirthdatePickSetup.setText(date);
            }
        };




        //haal overige data van database zodat het later compleet kan worden geüpload

        if(firebaseAuth.getUid() != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null){
                        UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                            PlaceHolderUNameSetup.setText(userProfile.getUserName());
                            PlaceHolderEmailSetup.setText(userProfile.getUserEmail());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile_First_Setup.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //continue knop

        ContinueSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    sendUserDataToDatabaseFirstSetup();
                    Intent intent = new Intent(Profile_First_Setup.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    firebaseAuth.signOut();
                    startActivity(intent);
                    Toast.makeText(Profile_First_Setup.this, "Success! Please verify your email so you can login!", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(Profile_First_Setup.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    //zorg dat je niet terug kunt met de 'back' knop

    public void onBackPressed(){
        Toast.makeText(Profile_First_Setup.this, "Can't go back now, please finish the registration first", Toast.LENGTH_SHORT).show();
    }



    // kijkt of alles klopt

    private Boolean validateData(){
        Boolean result = false;

        usernameToDatabase = PlaceHolderUNameSetup.getText().toString().trim();
        useremailToDatabase = PlaceHolderEmailSetup.getText().toString().trim();

        userfullnameToDatabase = FullNameSetup.getText().toString().trim();
        userbirthdateToDatabase = BirthdatePickSetup.getText().toString();


        final StorageReference storageReference = firebaseStorage.getReference();
        StorageReference myRef6 = storageReference.child(firebaseAuth.getUid());


        if(userfullnameToDatabase.isEmpty()){
            Toast.makeText(Profile_First_Setup.this, "Please enter your name", Toast.LENGTH_LONG).show();
        }

        else{
            if(userbirthdateToDatabase.equals("Select day of birth")){
                Toast.makeText(Profile_First_Setup.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
            }

            else{
                if(!userfullnameToDatabase.matches("[a-zA-Z ]*")){
                    Toast.makeText(Profile_First_Setup.this, "Please make sure your full name contains only letters (a-z, A-Z) and/or spaces ( )", Toast.LENGTH_LONG).show();
                }

                else{

                    if(imagePath != null){
                        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("ProfilePicture");
                        UploadTask uploadTask = imageReference.putFile(imagePath);


                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile_First_Setup.this, "Couldn't upload image to database", Toast.LENGTH_LONG).show();
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });
                    }

                    else{

                        String TAG = "ProfilePicEmpty";
                        Log.e(TAG, "The profile pic is empty");

                        //Bitmap bm = BitmapFactory.decodeResource(ProfilePictureSetup.getResources(), R.drawable.neutral_profile_picture_nobackground);


                        StorageReference imageReference2 = storageReference.child(firebaseAuth.getUid()).child("Images").child("ProfilePicture");

                        ProfilePictureSetup.setDrawingCacheEnabled(true);
                        ProfilePictureSetup.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) ProfilePictureSetup.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = imageReference2.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });

                    }


                    result = true;
                }
            }
        }

        return result;
    }



    //upload data naar database

    private void sendUserDataToDatabaseFirstSetup (){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef684 = firebaseDatabase.getReference("users").child(firebaseAuth.getUid()); //elke gebruiker heeft een unieke uid, deze hebben we natuurlijk nodig als we zijn gegevens op de database zetten
        UserProfileToDatabase userProfile = new UserProfileToDatabase(usernameToDatabase, useremailToDatabase, userfullnameToDatabase, userbirthdateToDatabase);
        myRef684.setValue(userProfile);
    }

}
