
package com.example.myfirstapp.Imageposts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.Layout_Manager_BottomNav_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SharedPrefNightMode;
import com.example.myfirstapp.Textposts.CommentStuffForTextPost;
import com.example.myfirstapp.Textposts.CommentStuffForTextPostAdapter;
import com.example.myfirstapp.photoview.PhotoView;
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
    private PhotoView ImageContent;
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

    private String UID, mDate, Message;
    private Uri ImageUri;

    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;
    private FirebaseAuth firebaseAuth;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private int LikeCount, DislikeCount, CommentCount;
    private static final String TAG = "Text_Post_Viewing";

    SharedPrefNightMode sharedPrefNightMode;


    private void SetupUI() {

       /* Title = findViewById(R.id.tvTitleOfImage);
        UserName = findViewById(R.id.tvUsernameForImage);
        LikeCountDisplay = findViewById(R.id.tvLikeCounterForImage);
        DislikeCountDisplay = findViewById(R.id.tvDislikeCounterForImage);
        Like = findViewById(R.id.ibLikeUpForImage);
        Dislike = findViewById(R.id.ibLikeDownForImage);
        Date = findViewById(R.id.tvDateOfPostImage);*/

        ImageContent = findViewById(R.id.ivImageContentImage);
        //PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(ImageContent);
        // photoViewAttacher.update();

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        MyUID = firebaseAuth.getCurrentUser().getUid().toString();


        /*inal String ThePostKey = getIntent().getExtras().get("key").toString();
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
        });*/


    }

    private void clear() {

        int size = commentStuffForTextPostList.size();
        if (size > 0) {
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

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if (sharedPrefNightMode.loadNightModeState() == true) {
            setTheme(R.style.AppTheme_Night);
        } else {
            setTheme(R.style.AppTheme);
            setLightStatusBar(ImagePostViewing.this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewingactivity2);

        key = getIntent().getExtras().get("key").toString();

        SetupUI();
        LoadData();
    }

    private void LoadData() {

        if (key.equals("no key")) {
            ImageUri = Uri.parse(getIntent().getExtras().get("ImageUri").toString());
            Message = getIntent().getExtras().get("Message").toString();
            mDate = getIntent().getExtras().get("Date").toString();
            UID = getIntent().getExtras().get("UID").toString();
            Picasso.get().load(ImageUri).into(ImageContent);

        } else {


        final DatabaseReference PostType = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("type");

      /*  DatabaseReference TitleRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("title");

        TitleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Title.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagePostViewing.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });*/

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

        /*DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("user_name");

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
        });*/}
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

                                if (dataSnapshot.hasChild(PostUIDForCheck)) {

                                    if (MyUIdForCheck.equals(PostUIDForCheck)) {

                                        Intent GoToMyProfileAfterCheck = new Intent(ImagePostViewing.this, Layout_Manager_BottomNav_Activity.class);
                                        GoToMyProfileAfterCheck.putExtra("Type", "Account");
                                        startActivity(GoToMyProfileAfterCheck);

                                    } else {

                                        if (AnonCheck.equals(AnonToCheck)) {

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(ImagePostViewing.this);
                                            dialog.setTitle("This user has posted anonymously");
                                            dialog.setMessage("You cannot view this user because this user has decided to post anonymously");
                                            AlertDialog alertDialog = dialog.create();
                                            alertDialog.show();

                                        } else {

                                            Intent GoToProfile = new Intent(ImagePostViewing.this, Account_Info_OtherUser_Activity.class);
                                            GoToProfile.putExtra("Key", ThePostKey);
                                            startActivity(GoToProfile);

                                        }
                                    }

                                } else {

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

        key = getIntent().getExtras().get("key").toString();

        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Likes");
        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Posts").child(key).child("Dislikes");

        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DislikedCheck) {
                    DatabaseDislike.child(MyUID).removeValue();
                    Liked = true;

                    DatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (Liked) {

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
                } else {
                    Liked = true;

                    DatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (Liked) {

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

                if (LikedCheck) {
                    DatabaseLike.child(MyUID).removeValue();

                    Disliked = true;

                    DatabaseDislike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (Disliked) {

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
                } else {
                    Disliked = true;

                    DatabaseDislike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (Disliked) {

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

                } else {

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

                } else {

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

    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColorLogin)); // optional
        }
    }

}

