package com.example.myfirstapp.AccountActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.Content_Policy_Activity;
import com.example.myfirstapp.Data_Policy_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;
import com.example.myfirstapp.Terms_of_Use_Activity;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Profile_First_Setup extends AppCompatActivity {


    private static final String TAG = "Profile_First_Setup";

    private EditText FullNameSetup;
    private ImageView ProfilePictureSetup;
    private TextView PlaceHolderUNameSetup, PlaceHolderEmailSetup, ErrorFullName, TermsAndDataPolicy;
    private Button ContinueSetup;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private String usernameToDatabase, useremailToDatabase, userfullnameToDatabase, userbirthdateToDatabase, UriImage, UID;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private static int PICK_IMAGE = 234;
    private Uri imagePath;
    private StorageReference imageReference;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private Boolean PickedImage = false;

    private String TAGTEST = "CheckmetJuultje";
    SharedPrefNightMode sharedPrefNightMode;


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

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else {
            setTheme(R.style.AppTheme);
            setLightStatusBar(Profile_First_Setup.this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__first__setup);



        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FullNameSetup = (EditText) findViewById(R.id.etFullNameSetup);
        ContinueSetup = (Button) findViewById(R.id.btnContinueSetupProfile);
        ProfilePictureSetup = (ImageView) findViewById(R.id.ivProfilePictureFirstSetup);
        PlaceHolderEmailSetup = (TextView) findViewById(R.id.tvPlaceHolderEmailSetup);
        PlaceHolderUNameSetup = (TextView) findViewById(R.id.tvPlaceHolderUserNameSetup);
        ErrorFullName = findViewById(R.id.tvFullNameErrorFirstSetup);
        TermsAndDataPolicy = findViewById(R.id.tvBySigningUpYouAgree);


        //set terms en data enzo underline ook en meer gebeurens
        String TheText = "By signing up you agree to our Terms and that you have read our Data Policy and Content Policy";
        SpannableString spannebleString = new SpannableString(TheText);
        ClickableSpan TermsSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Profile_First_Setup.this, Terms_of_Use_Activity.class);
                startActivity(intent);
            }
        };
        ClickableSpan DataSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Profile_First_Setup.this, Data_Policy_Activity.class);
                startActivity(intent);
            }
        };
        ClickableSpan ContentSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Profile_First_Setup.this, Content_Policy_Activity.class);
                startActivity(intent);
            }
        };

        spannebleString.setSpan(TermsSpan, 31, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannebleString.setSpan(DataSpan, 64, 75, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannebleString.setSpan(ContentSpan, 80, 94, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TermsAndDataPolicy.setText(spannebleString);
        TermsAndDataPolicy.setMovementMethod(LinkMovementMethod.getInstance());


        ErrorFullName.setVisibility(View.INVISIBLE);

        FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        //profielfoto gebeuren

        ProfilePictureSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");  //alle image types worden geaccepteerd
                startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE);
            }
        });

        //continue knop

        ContinueSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {

                    Log.e(TAGTEST, "Validate = true");

                    final ProgressDialog dialog = new ProgressDialog(Profile_First_Setup.this);
                    dialog.setTitle("Creating your account!");
                    dialog.setMessage("Please wait");
                    dialog.show();

                    MakeProfileAuth();


                } else {

                }

            }
        });

    }

    // kijkt of alles klopt

    private Boolean validateData() {
        Boolean result = false;

        usernameToDatabase = PlaceHolderUNameSetup.getText().toString().trim();
        useremailToDatabase = PlaceHolderEmailSetup.getText().toString().trim();

        userfullnameToDatabase = FullNameSetup.getText().toString().trim();

        int FullnameLength = userfullnameToDatabase.length();

        Calendar cal2 = Calendar.getInstance();
        int yearCheck = cal2.get(Calendar.YEAR);
        int monthCheck = cal2.get(Calendar.MONTH);
        int dayCheck = cal2.get(Calendar.DAY_OF_MONTH);

        ErrorFullName.setVisibility(View.INVISIBLE);

        FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login);

        if (userfullnameToDatabase.isEmpty()) {
            ErrorFullName.setText("Please enter a display name");
            ErrorFullName.setVisibility(View.VISIBLE);
            FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
        }
        else {

                if (userfullnameToDatabase.length() < 3) {
                    ErrorFullName.setText("Make sure your display name is at least 3 characters long");
                    ErrorFullName.setVisibility(View.VISIBLE);
                    FullNameSetup.setBackgroundResource(R.drawable.edittext_roundedcorners_login_error);
                }
                else {
                    result = true;
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

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String Date = dateFormat.format(calendar.getTime());

        //username apart in database zetten om te controleren of die al bestaat
        DatabaseReference UploadUsername = firebaseDatabase.getReference("Usernames").child(username);
        UploadUsername.setValue(UID);

        //uploaden naar database
        UserProfileToDatabase userProfile = new UserProfileToDatabase(UriImage, UID, username, email, userfullnameToDatabase, Date);
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

    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColorLogin)); // optional
        }
    }

}
