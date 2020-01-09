package com.example.myfirstapp.AccountActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private TextView BirthdatePickSetup, PlaceHolderUNameSetup, PlaceHolderEmailSetup, ErrorFullName, ErrorBirthDate, TermsAndDataPolicy,TermsAndDataPolicy2;
    private Button ContinueSetup;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private String usernameToDatabase, useremailToDatabase, userfullnameToDatabase, userbirthdateToDatabase, UriImage, UID;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private static int PICK_IMAGE = 234;
    private Uri imagePath;
    private StorageReference imageReference;

    private Boolean PickedImage = false;

    private String TAGTEST = "CheckmetJuultje";


    //afbeelding kiezen uit gallerij

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            Log.d("TAG", imagePath.toString());
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

        SetupDesign();

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FullNameSetup = (EditText) findViewById(R.id.etFullNameSetup);
        ContinueSetup = (Button) findViewById(R.id.btnContinueSetupProfile);
        ProfilePictureSetup = (ImageView) findViewById(R.id.ivProfilePictureFirstSetup);
        BirthdatePickSetup = (TextView) findViewById(R.id.tvSelectBirthdateSetup);
        PlaceHolderEmailSetup = (TextView) findViewById(R.id.tvPlaceHolderEmailSetup);
        PlaceHolderUNameSetup = (TextView) findViewById(R.id.tvPlaceHolderUserNameSetup);
        ErrorFullName = findViewById(R.id.tvFullNameErrorFirstSetup);
        ErrorBirthDate = findViewById(R.id.tvBirthdayErrorFirstSetup);
        TermsAndDataPolicy = findViewById(R.id.tvTermsAndDataPolicy);
        TermsAndDataPolicy2 = findViewById(R.id.tvBySigningUpYouAgree);

        TermsAndDataPolicy.setPaintFlags(TermsAndDataPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TermsAndDataPolicy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TermsAndDataPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ErrorFullName.setVisibility(View.INVISIBLE);
        ErrorBirthDate.setVisibility(View.INVISIBLE);

        FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

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

        //continue knop

        ContinueSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {

                    Log.e(TAGTEST, "Validate = true");

                    MakeProfileAuth();


                } else {

                }

            }
        });

    }

    private void SetupDesign() {

        //voor het geven van kleur aan de status bar:

        Window window = Profile_First_Setup.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(Profile_First_Setup.this, R.color.statusBarColorLogin));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    // kijkt of alles klopt

    private Boolean validateData() {
        Boolean result = false;

        usernameToDatabase = PlaceHolderUNameSetup.getText().toString().trim();
        useremailToDatabase = PlaceHolderEmailSetup.getText().toString().trim();

        userfullnameToDatabase = FullNameSetup.getText().toString().trim();
        userbirthdateToDatabase = BirthdatePickSetup.getText().toString();

        int FullnameLength = userfullnameToDatabase.length();

        Calendar cal2 = Calendar.getInstance();
        int yearCheck = cal2.get(Calendar.YEAR);
        int monthCheck = cal2.get(Calendar.MONTH);
        int dayCheck = cal2.get(Calendar.DAY_OF_MONTH);

        ErrorFullName.setVisibility(View.INVISIBLE);
        ErrorBirthDate.setVisibility(View.INVISIBLE);

        FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        if (userfullnameToDatabase.isEmpty()) {
            ErrorFullName.setText("Please enter your name");
            ErrorFullName.setVisibility(View.VISIBLE);
            FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
        }
        else {

            if (userbirthdateToDatabase.equals("Select day of birth")) {
                ErrorBirthDate.setText("Please enter your birthday");
                ErrorBirthDate.setVisibility(View.VISIBLE);
            }
            else {

                if (!userfullnameToDatabase.matches("[a-zA-Z ]*")) {
                    ErrorFullName.setText("Make sure your name contains only alphabetic characters and spaces");
                    ErrorFullName.setVisibility(View.VISIBLE);
                    FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                }
                else {

                    if(FullnameLength > 31){
                        ErrorFullName.setText("Make sure your entered name is not longer than 30 characters");
                        ErrorFullName.setVisibility(View.VISIBLE);
                        FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                    }
                    else{

                        if(userbirthdateToDatabase.contains("2021")){
                            ErrorBirthDate.setText("Please enter a possible birthday");
                            ErrorBirthDate.setVisibility(View.VISIBLE);
                        }
                        else{
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }


    //upload data naar database

    private void getDownloadURL(){
        final StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("ProfilePictures").child(UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                UriImage = uri.toString();


                DatabaseShit();

                sendEmailVerification();


            }
        });}


    private void MakeProfileAuth() {

        //dingen uit intent halen

        String username = getIntent().getExtras().get("username").toString();
        String password = getIntent().getExtras().get("password").toString();
        String email = getIntent().getExtras().get("email").toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendUserDataToDatabaseFirstSetup();
                } else {
                    Toast.makeText(Profile_First_Setup.this, "Registration failed, please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserDataToDatabaseFirstSetup() {

        UID = firebaseAuth.getUid();


        //profielfoto shit

        StorageReference storageReference = firebaseStorage.getReference();

        if (imagePath != null) {
            imageReference = storageReference.child("ProfilePictures").child(firebaseAuth.getUid());
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

                    getDownloadURL();

                }
            });
        } else {

            String TAG = "ProfilePicEmpty";
            Log.e(TAG, "The profile pic is empty");

            StorageReference imageReference2 = storageReference.child("ProfilePictures").child(firebaseAuth.getUid());

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
                    Log.e(TAGTEST, "Upload pf success");
                    getDownloadURL();
                }
            });

        }
    }



    private void DatabaseShit(){

        //krijg database shit
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef684 = firebaseDatabase.getReference("users").child(UID); //elke gebruiker heeft een unieke uid, deze hebben we natuurlijk nodig als we zijn gegevens op de database zetten

        String username = getIntent().getExtras().get("username").toString();
        String password = getIntent().getExtras().get("password").toString();
        String email = getIntent().getExtras().get("email").toString();

        //username apart in database zetten om te controleren of die al bestaat
        DatabaseReference UploadUsername = firebaseDatabase.getReference("Usernames").child(username);
        UploadUsername.setValue("exists");

        //uploaden naar database
        UserProfileToDatabase userProfile = new UserProfileToDatabase(UriImage, UID, username, email, userfullnameToDatabase, userbirthdateToDatabase);
        myRef684.setValue(userProfile);
        Log.e(TAGTEST, "usertodatabase bereikt!");
    }


    private void sendEmailVerification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Intent intent = new Intent(Profile_First_Setup.this, SignUp_Success_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        firebaseAuth.signOut();
                        startActivity(intent);

                    }

                    else{
                        Toast.makeText(Profile_First_Setup.this, "Verification mail has not been send, please try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
