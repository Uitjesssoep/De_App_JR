package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Text_Post_Viewing_Activity extends AppCompatActivity {


    private TextView Title, Content, UserName, LikeCountDisplay, DislikeCountDisplay, CommentView, NumberOfComments, user_name_gebruiker;
    private FirebaseDatabase firebaseDatabase;
    private String key, MyUID, CommentMessage, temp_key;
    private ImageButton Like, Dislike;
    private Button PostComment;
    private EditText CommentSubstance;
    private boolean Liked = false;
    private boolean Disliked = false;
    private boolean LikedCheck = false, DislikedCheck = false;

    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference DatabaseCommentStuff;
    private FirebaseAuth firebaseAuth;

    private int LikeCount, DislikeCount;
    private static final String TAG = "Text_Post_Viewing";


    private void SetupUI() {

        Title = findViewById(R.id.tvTitleOfTextPost);
        Content = findViewById(R.id.tvContentOfTextPost);
        UserName = findViewById(R.id.tvUsernameForTextPost);
        LikeCountDisplay = findViewById(R.id.tvLikeCounterForTextPostViewing);
        DislikeCountDisplay = findViewById(R.id.tvDislikeCounterForTextPostViewing);
        Like = findViewById(R.id.ibLikeUpForTextPostViewing);
        Dislike = findViewById(R.id.ibLikeDownForTextPostViewing);

        CommentView = findViewById(R.id.tvCommentWindowForTextPosts);
        NumberOfComments = findViewById(R.id.tvNumberOfCommentsForTextPosts);
        PostComment = findViewById(R.id.btnPostCommentOnTextPost);
        CommentSubstance = findViewById(R.id.etAddCommentForTextPost);
        user_name_gebruiker = findViewById(R.id.tvUsernameGebruikerVerstoptInEenHoekjeOmdatIkGeenBetereManierWeet);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        MyUID = firebaseAuth.getCurrentUser().getUid().toString();

        CommentView.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__post__viewing);

        setTitle("Text Post");

        SetupUI();

        LikeDislikeCount();

        CommentOnPost();

        key = getIntent().getExtras().get("Key").toString();

        DatabaseReference TitleRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Title");

        TitleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Title.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Text_Post_Viewing_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference ContentRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Content");

        ContentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Content.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Text_Post_Viewing_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("User_name");

        UserNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Text_Post_Viewing_Activity.this, "Couldn't retrieve data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CommentOnPost() {

        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                user_name_gebruiker.setText(userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Text_Post_Viewing_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        key = getIntent().getExtras().get("Key").toString();

        DatabaseCommentStuff = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Comments");

        PostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommentMessage = CommentSubstance.getText().toString();

                if(CommentMessage.isEmpty()){
                    Toast.makeText(Text_Post_Viewing_Activity.this, "Can't post an empty comment", Toast.LENGTH_SHORT).show();
                }

                else{

                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = DatabaseCommentStuff.push().getKey();
                    DatabaseCommentStuff.updateChildren(map);

                    DatabaseReference message_root = DatabaseCommentStuff.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("name", user_name_gebruiker.getText().toString());
                    map2.put("message", CommentSubstance.getText().toString());

                    message_root.updateChildren(map2);

                    CommentSubstance.setText("");

                }

            }
        });

    }

    private void LikeDislikeCount() {

        key = getIntent().getExtras().get("Key").toString();

        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Likes");
        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Dislikes");

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

        DatabaseIsItLiked = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Likes");
        DatabaseIsItLiked.keepSynced(true);
        DatabaseIsItLiked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {

                    Like.setImageResource(R.drawable.pijl_omhoog_geklikt);
                    LikedCheck = true;

                }

                else{

                    Like.setImageResource(R.drawable.pijl_omhoog_neutraal);
                    LikedCheck = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseIsItDisliked = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Dislikes");
        DatabaseIsItDisliked.keepSynced(true);
        DatabaseIsItDisliked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(MyUID)) {

                    Dislike.setImageResource(R.drawable.pijl_omlaag_geklikt);
                    DislikedCheck = true;

                }

                else{

                    Dislike.setImageResource(R.drawable.pijl_omlaag_neutraal);
                    DislikedCheck = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseLikeCount = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Likes");
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

        DatabaseDislikeCount = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Dislikes");
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


}
