
package com.example.myfirstapp.Imageposts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.Edit_PC_Activity;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Report_TextPost_Activity;
import com.example.myfirstapp.Textposts.CommentStuffForTextPost;
import com.example.myfirstapp.Textposts.CommentStuffForTextPostAdapter;
import com.example.myfirstapp.Textposts.Post_Viewing_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ImagePostViewing extends AppCompatActivity {
    private ImageView ImageContent;
    //private TextView Username, Date, Title;
    private TextView Title, UserName, LikeCountDisplay, DislikeCountDisplay, NumberOfComments, Date;

    private RecyclerView CommentView;
    private List<CommentStuffForTextPost> commentStuffForTextPostList;
    private CommentStuffForTextPostAdapter commentStuffForTextPostAdapter;

    private FirebaseDatabase firebaseDatabase;
    private String key, MyUID, CommentMessage, temp_key;
    private ImageButton Like, Dislike, Exit;
    private EditText CommentSubstance;
  //  private ImageView ImageContent;

    private boolean Liked = false;
    private boolean Disliked = false;
    private boolean LikedCheck = false, DislikedCheck = false;

    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;
    private FirebaseAuth firebaseAuth;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private int LikeCount, DislikeCount, CommentCount;
    private static final String TAG = "Text_Post_Viewing";


    private void SetupUI() {

        Title = findViewById(R.id.tvTitleOfImage);
        UserName = findViewById(R.id.tvUsernameForImage);
        LikeCountDisplay = findViewById(R.id.tvLikeCounterForImage);
        DislikeCountDisplay = findViewById(R.id.tvDislikeCounterForImage);
        Like = findViewById(R.id.ibLikeUpForImage);
        Dislike = findViewById(R.id.ibLikeDownForImage);
        Date = findViewById(R.id.tvDateOfPostImage);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        MyUID = firebaseAuth.getCurrentUser().getUid().toString();


        final String ThePostKey = getIntent().getExtras().get("Key").toString();
        final String MyUID3 = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference GetPostUID = FirebaseDatabase.getInstance().getReference("General_Posts").child(ThePostKey).child("uid");
        GetPostUID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(MyUID3.equals(dataSnapshot.getValue().toString())){

                    DatabaseReference GetMyUsername = FirebaseDatabase.getInstance().getReference("users").child(MyUID3).child("userName");
                    GetMyUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String MyUserName = dataSnapshot.getValue().toString();

                            UserName.setTextColor(getResources().getColor(R.color.colorAccent));
                            UserName.setText(MyUserName);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void clear() {

        int size = commentStuffForTextPostList.size();
        if(size > 0){
            for (int i = 0; i < size; i++) {
                commentStuffForTextPostList.remove(0);

                String TAGTest = "ListEmpty";
                Log.e(TAGTest, "tot 'for' gekomen");
            }

            commentStuffForTextPostAdapter.notifyItemRangeRemoved(0, size);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post_viewing);

        key = getIntent().getExtras().get("Key").toString();

        final DatabaseReference CheckIfNotDeleted = FirebaseDatabase.getInstance().getReference("General_Posts");
        CheckIfNotDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(key)){

                    SetupUI();

                    SetupDesign();

                    LikeDislikeCount();

                    LookAtPostersProfile();

                    LoadData();

                }

                else{

                    final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ImagePostViewing.this);
                    dialog.setTitle("This post has been deleted");
                    dialog.setMessage("This post has been deleted, you can no longer view it.");

                    dialog.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(ImagePostViewing.this, Layout_Manager_BottomNav_Activity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    android.app.AlertDialog alertDialog = dialog.create();
                    alertDialog.show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LoadData() {

        final DatabaseReference PostType = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("type");

        DatabaseReference TitleRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("title");

        TitleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Title.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagePostViewing.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference ContentRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("content");
        ContentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                PostType.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            Picasso.get().load(dataSnapshot.getValue(String.class)).into(ImageContent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagePostViewing.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("user_name");

        UserNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagePostViewing.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference PostDateRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("date");
        PostDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String PostDate = dataSnapshot.getValue().toString();
                Date.setText(PostDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void LookAtPostersProfile() {

        UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String ThePostKey = getIntent().getExtras().get("Key").toString();

                DatabaseReference CheckIfMyUIDCheck = FirebaseDatabase.getInstance().getReference("General_Posts").child(ThePostKey);
                CheckIfMyUIDCheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String MyUIdForCheck = FirebaseAuth.getInstance().getUid().toString();
                        final String PostUIDForCheck = dataSnapshot.child("uid").getValue().toString();
                        final String AnonToCheck = dataSnapshot.child("user_name").getValue().toString();
                        final String AnonCheck = "[anonymous]";

                        DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                        CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(PostUIDForCheck)){

                                    if(MyUIdForCheck.equals(PostUIDForCheck)){

                                        Intent GoToMyProfileAfterCheck = new Intent(ImagePostViewing.this, Account_Info_Activity.class);
                                        startActivity(GoToMyProfileAfterCheck);

                                    }
                                    else {

                                        if(AnonCheck.equals(AnonToCheck)){

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(ImagePostViewing.this);
                                            dialog.setTitle("This user has posted anonymously");
                                            dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                            AlertDialog alertDialog = dialog.create();
                                            alertDialog.show();

                                        }

                                        else{

                                            Intent GoToProfile = new Intent(ImagePostViewing.this, Account_Info_OtherUser_Activity.class);
                                            GoToProfile.putExtra("Key", ThePostKey);
                                            startActivity(GoToProfile);

                                        }
                                    }

                                }

                                else{

                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ImagePostViewing.this);
                                    dialog.setTitle("This user has been deleted");
                                    dialog.setMessage("You can no longer view this user");
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void LikeDislikeCount() {

        key = getIntent().getExtras().get("Key").toString();

        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");

        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(DislikedCheck){
                    DatabaseDislike.child(MyUID).removeValue();
                    Liked = true;

                    DatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(Liked) {

                                if (dataSnapshot.hasChild(MyUID)) {

                                    DatabaseLike.child(MyUID).removeValue();
                                    Liked = false;

                                } else {

                                    DatabaseLike.child(MyUID).setValue("RandomLike");
                                    Liked = false;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Liked = true;

                    DatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(Liked) {

                                if (dataSnapshot.hasChild(MyUID)) {

                                    DatabaseLike.child(MyUID).removeValue();
                                    Liked = false;

                                } else {

                                    DatabaseLike.child(MyUID).setValue("RandomLike");
                                    Liked = false;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        Dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LikedCheck){
                    DatabaseLike.child(MyUID).removeValue();

                    Disliked = true;

                    DatabaseDislike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(Disliked) {

                                if (dataSnapshot.hasChild(MyUID)) {

                                    DatabaseDislike.child(MyUID).removeValue();
                                    Disliked = false;

                                } else {

                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                    Disliked = false;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Disliked = true;

                    DatabaseDislike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(Disliked) {

                                if (dataSnapshot.hasChild(MyUID)) {

                                    DatabaseDislike.child(MyUID).removeValue();
                                    Disliked = false;

                                } else {

                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                    Disliked = false;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        DatabaseIsItLiked = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
        DatabaseIsItLiked.keepSynced(true);
        DatabaseIsItLiked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {

                    Like.setImageResource(R.drawable.ic_keyboard_arrow_up_green_24dp);
                    LikedCheck = true;

                }

                else{

                    Like.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    LikedCheck = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseIsItDisliked = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");
        DatabaseIsItDisliked.keepSynced(true);
        DatabaseIsItDisliked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {

                    Dislike.setImageResource(R.drawable.ic_keyboard_arrow_down_green_24dp);
                    DislikedCheck = true;

                }

                else{

                    Dislike.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    DislikedCheck = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseLikeCount = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
        DatabaseLikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeCount = (int) dataSnapshot.getChildrenCount();
                LikeCountDisplay.setText("" + LikeCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseDislikeCount = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");
        DatabaseDislikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DislikeCount = (int) dataSnapshot.getChildrenCount();
                DislikeCountDisplay.setText("" + DislikeCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupDesign() {
        //voor het geven van kleur aan de status bar:

        Window window = ImagePostViewing.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(ImagePostViewing.this, R.color.slighly_darker_mainGreen));


        //action bar ding

        Toolbar toolbar = findViewById(R.id.action_bar_textpostviewing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exittextpostviewing);
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
        inflater.inflate(R.menu.menu_actionbar_bookmark_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem notsaved = menu.findItem(R.id.action_bookmark_unselected);
        final MenuItem saved = menu.findItem(R.id.action_bookmark_selected);

        final MenuItem Report = menu.findItem(R.id.action_settings);
        final MenuItem Delete = menu.findItem(R.id.action_delete);
        final MenuItem Edit = menu.findItem(R.id.action_edit);

        String Key = getIntent().getExtras().get("Key").toString();
        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("General_Posts").child(Key);
        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String PostUID = dataSnapshot.child("uid").getValue().toString();
                String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(PostUID.equals(MyUID)){
                    Report.setVisible(false);
                }
                else {
                    Delete.setVisible(false);
                    Edit.setVisible(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference CheckIfSaved = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("SavedPosts");
        CheckIfSaved.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(getIntent().getExtras().get("Key").toString())){
                    notsaved.setVisible(false);
                    saved.setVisible(true);
                }
                else{
                    notsaved.setVisible(true);
                    saved.setVisible(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:

                Intent intent = new Intent(ImagePostViewing.this, Report_TextPost_Activity.class);
                intent.putExtra("Titel", Title.getText().toString());
                intent.putExtra("User", UserName.getText().toString());
                intent.putExtra("Key", getIntent().getExtras().get("Key").toString());
                intent.putExtra("Soort", "post");
                startActivity(intent);

                break;

            case R.id.action_refresh_feed:

                break;

            case R.id.action_delete:

                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
                dialog.setTitle("Delete your post?");
                dialog.setMessage("Deleting this post cannot be undone! Are you sure you want to delete it?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("General_Posts").child(getIntent().getExtras().get("Key").toString());
                        DeleteThePost.removeValue();
                        Intent intent = new Intent(ImagePostViewing.this, Layout_Manager_BottomNav_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                android.app.AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                break;

            case R.id.action_edit:

                final String TheUltimatePostKey = getIntent().getExtras().get("Key").toString();
                DatabaseReference GetType = FirebaseDatabase.getInstance().getReference("General_Posts").child(TheUltimatePostKey).child("type");
                GetType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Type = dataSnapshot.getValue().toString();
                        Intent intent684 = new Intent(ImagePostViewing.this, Edit_PC_Activity.class);
                        String Text = "Text";
                        if(Text.equals(Type)){
                            intent684.putExtra("Type", "TextPost");
                            intent684.putExtra("Key", TheUltimatePostKey);
                            startActivity(intent684);
                        }
                        String Image = "Image";
                        if(Image.equals(Type)){
                            intent684.putExtra("Type", "ImagePost");
                            intent684.putExtra("Key", TheUltimatePostKey);
                            startActivity(intent684);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                break;

            case R.id.action_bookmark_unselected:

                BookmarkDingen();

                break;

            case R.id.action_bookmark_selected:

                BookmarkDingen();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void BookmarkDingen() {

        Log.e("Bookmark", "Save pushed");

        final String KeyPost = getIntent().getExtras().get("Key").toString();

        final DatabaseReference SaveThePost = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        SaveThePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("SavedPosts").hasChild(KeyPost)){
                    Log.e("Bookmark", "Unsave bereikt");
                    SaveThePost.child("SavedPosts").child(KeyPost).removeValue();
                }
                else{
                    Log.e("Bookmark", "Save bereikt");
                    SaveThePost.child("SavedPosts").child(KeyPost).setValue("added");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

