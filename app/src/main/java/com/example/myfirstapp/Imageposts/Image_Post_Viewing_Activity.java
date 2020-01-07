package com.example.myfirstapp.Imageposts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Image_Post_Viewing_Activity extends AppCompatActivity {
    private ImageView ImagePost;
    private TextView Title, Content, UserName, LikeCountDisplay, DislikeCountDisplay, NumberOfComments, user_name_gebruiker;
    private Button PostComment;
    private RecyclerView CommentView;
    private String key, MyUID, CommentMessage, temp_key;
    private EditText CommentSubstance;

    private ImageButton Like, Dislike;

    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;

    private int LikeCount, DislikeCount, CommentCount;

    private boolean Liked = false;
    private boolean Disliked = false;
    private boolean LikedCheck = false, DislikedCheck = false;

    private void SetupUI(){
        ImagePost = findViewById(R.id.ivImagePost);
        Title= findViewById(R.id.tvTitleOfImagePost);
        Content= findViewById(R.id.tvContentOfImagePost);
        UserName= findViewById(R.id.tvUsernameForImagePost);
        LikeCountDisplay = findViewById(R.id.tvLikeCounterImageItem2);
        DislikeCountDisplay = findViewById(R.id.tvDislikeCounterImageItem);
        Like = findViewById(R.id.ibLikeUpImageItem);
        Dislike = findViewById(R.id.ibLikeDownImageItem);
        NumberOfComments=findViewById(R.id.tvNumberOfCommentsForImagePosts);
        user_name_gebruiker=findViewById(R.id.tvUsernameGebruikerVerstoptInEenHoekjeOmdatIkGeenBetereManierWeetImage);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__post__viewing_);
    }



    private void LikeDislikeCount() {

        key = getIntent().getExtras().get("Key").toString();

        DatabaseLike = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Likes");
        DatabaseDislike = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Dislikes");

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

        DatabaseIsItLiked = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Likes");
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

        DatabaseIsItDisliked = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Dislikes");
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


        DatabaseLikeCount = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Likes");
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

        DatabaseDislikeCount = FirebaseDatabase.getInstance().getReference("General_Image_Posts").child(key).child("Dislikes");
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
