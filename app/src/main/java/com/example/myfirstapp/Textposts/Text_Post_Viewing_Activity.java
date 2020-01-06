package com.example.myfirstapp.Textposts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.Account_Info_Activity;
import com.example.myfirstapp.Account_Info_OtherUserComments_Activity;
import com.example.myfirstapp.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.Deleting_Account_Activity;
import com.example.myfirstapp.General_Feed_Activity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.UserProfileToDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Text_Post_Viewing_Activity extends AppCompatActivity {


    private TextView Title, Content, UserName, LikeCountDisplay, DislikeCountDisplay, NumberOfComments, user_name_gebruiker;

    private RecyclerView CommentView;
    private List<CommentStuffForTextPost> commentStuffForTextPostList;
    private CommentStuffForTextPostAdapter commentStuffForTextPostAdapter;

    private FirebaseDatabase firebaseDatabase;
    private String key, MyUID, CommentMessage, temp_key;
    private ImageButton Like, Dislike;
    private Button PostComment;
    private EditText CommentSubstance;
    private boolean Liked = false;
    private boolean Disliked = false;
    private boolean LikedCheck = false, DislikedCheck = false;

    private DatabaseReference DatabaseLike, DatabaseDislike, DatabaseIsItLiked, DatabaseIsItDisliked, DatabaseLikeCount, DatabaseDislikeCount;
    private DatabaseReference DatabaseCommentStuff, DatabaseCommentCount;
    private FirebaseAuth firebaseAuth;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String Date;

    private int LikeCount, DislikeCount, CommentCount;
    private static final String TAG = "Text_Post_Viewing";


    private void SetupUI() {

        Title = findViewById(R.id.tvTitleOfTextPost);
        Content = findViewById(R.id.tvContentOfTextPost);
        UserName = findViewById(R.id.tvUsernameForTextPost);
        LikeCountDisplay = findViewById(R.id.tvLikeCounterForTextPostViewing);
        DislikeCountDisplay = findViewById(R.id.tvDislikeCounterForTextPostViewing);
        Like = findViewById(R.id.ibLikeUpForTextPostViewing);
        Dislike = findViewById(R.id.ibLikeDownForTextPostViewing);

        CommentView = findViewById(R.id.rvCommentsTextPost);
        CommentView.setLayoutManager(new LinearLayoutManager(this));
        commentStuffForTextPostList = new ArrayList<>();

        NumberOfComments = findViewById(R.id.tvNumberOfCommentsForTextPosts);
        PostComment = findViewById(R.id.btnPostCommentOnTextPost);
        CommentSubstance = findViewById(R.id.etAddCommentForTextPost);
        user_name_gebruiker = findViewById(R.id.tvUsernameGebruikerVerstoptInEenHoekjeOmdatIkGeenBetereManierWeet);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        MyUID = firebaseAuth.getCurrentUser().getUid().toString();

        key = getIntent().getExtras().get("Key").toString();
        DatabaseReference CheckIfCommentsDisabledDatabase = firebaseDatabase.getReference("General_Text_Posts").child(key);
        CheckIfCommentsDisabledDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("DisabledComments")){
                    CommentView.setVisibility(View.GONE);
                    NumberOfComments.setVisibility(View.GONE);
                    PostComment.setVisibility(View.GONE);
                    CommentSubstance.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__post__viewing);

        SetupUI();

        LikeDislikeCount();

        CommentOnPost();

        LookAtPostersProfile();

        key = getIntent().getExtras().get("Key").toString();

        DatabaseReference TitleRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("title");

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

        DatabaseReference ContentRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("content");

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

        DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("user_name");

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

    private void LookAtPostersProfile() {

        UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String ThePostKey = getIntent().getExtras().get("Key").toString();

                DatabaseReference CheckIfMyUIDCheck = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(ThePostKey).child("uid");
                CheckIfMyUIDCheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String MyUIdForCheck = FirebaseAuth.getInstance().getUid().toString();
                        final String PostUIDForCheck = dataSnapshot.getValue().toString();

                        DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                        CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(PostUIDForCheck)){

                                    if(MyUIdForCheck.equals(PostUIDForCheck)){

                                        Intent GoToMyProfileAfterCheck = new Intent(Text_Post_Viewing_Activity.this, Account_Info_Activity.class);
                                        startActivity(GoToMyProfileAfterCheck);

                                    }
                                    else {

                                        Intent GoToProfile = new Intent(Text_Post_Viewing_Activity.this, Account_Info_OtherUser_Activity.class);
                                        GoToProfile.putExtra("Key", ThePostKey);
                                        startActivity(GoToProfile);

                                    }

                                }

                                else{

                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(Text_Post_Viewing_Activity.this);
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


    private void CommentOnPost() {

        //test

        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        ReloadComments();

        PostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommentMessage = CommentSubstance.getText().toString();
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                Date = dateFormat.format(calendar.getTime());

                if(CommentMessage.isEmpty()){
                    Toast.makeText(Text_Post_Viewing_Activity.this, "Can't post an empty comment", Toast.LENGTH_SHORT).show();
                }

                else{

                    DatabaseReference GetUserName = firebaseDatabase.getReference("users").child(MyUID);
                    GetUserName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);

                            String userName = userProfile.getUserName().toString();

                            temp_key = DatabaseCommentStuff.push().getKey();
                            CommentStuffForTextPost commentStuffForTextPost = new CommentStuffForTextPost(CommentMessage, Date, userName, temp_key, MyUID, key);
                            DatabaseCommentStuff.child(temp_key).setValue(commentStuffForTextPost);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    CommentSubstance.setText("");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }

            }
        });


        DatabaseCommentCount = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Comments");
        DatabaseCommentCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CommentCount = (int) dataSnapshot.getChildrenCount();
                NumberOfComments.setText("Number of comments: " + CommentCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ReloadComments() {

        DatabaseCommentStuff = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(key).child("Comments");


        DatabaseCommentStuff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                clear();


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentStuffForTextPost commentStuffForTextPost = dataSnapshot1.getValue(CommentStuffForTextPost.class);
                    commentStuffForTextPostList.add(commentStuffForTextPost);
                }

                commentStuffForTextPostAdapter = new CommentStuffForTextPostAdapter(Text_Post_Viewing_Activity.this, commentStuffForTextPostList);
                CommentView.setAdapter(commentStuffForTextPostAdapter);

                commentStuffForTextPostAdapter.setOnItemClickListener(new CommentStuffForTextPostAdapter.OnItemClickListener() {
                    @Override
                    public void onUserNameClick(int position) {
                        final String CommentKey = commentStuffForTextPostList.get(position).getKey().toString();
                        final String PostKey = commentStuffForTextPostList.get(position).getOldKey().toString();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("General_Text_Posts").child(PostKey).child("Comments").child(CommentKey).child("uid");
                        CheckIfMyUID.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String MyUIDCheck = FirebaseAuth.getInstance().getUid().toString();
                                final String CommentUID = dataSnapshot.getValue().toString();

                                DatabaseReference UserIfDeleted = firebaseDatabase.getReference("users");
                                UserIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(CommentUID)){


                                            if(MyUIDCheck.equals(CommentUID)){

                                                Intent GoToMyProfile = new Intent(Text_Post_Viewing_Activity.this, Account_Info_Activity.class);
                                                startActivity(GoToMyProfile);

                                            }

                                            else{

                                                Intent GoToProfile = new Intent(Text_Post_Viewing_Activity.this, Account_Info_OtherUserComments_Activity.class);
                                                GoToProfile.putExtra("CommentKey", CommentKey);
                                                GoToProfile.putExtra("PostKey", PostKey);
                                                startActivity(GoToProfile);

                                            }
                                        }
                                        else{

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(Text_Post_Viewing_Activity.this);
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

                    @Override
                    public void onDeleteCommentClick(int position) {
                        String TAG5 = "CheckDeleteCommentClick";
                        Log.e(TAG5, "er is geclickt");

                        final String CommentKey = commentStuffForTextPostList.get(position).getKey().toString();

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Text_Post_Viewing_Activity.this);
                        dialog.setTitle("Delete your comment?");
                        dialog.setMessage("Deleting this comment cannot be undone! Are you sure you want to delete it?");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference DeleteVisible = FirebaseDatabase.getInstance()
                                        .getReference("General_Text_Posts")
                                        .child(key)
                                        .child("Comments");
                                DeleteVisible.child(CommentKey).removeValue();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }
                });

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

    public void onBackPressed(){
        Intent Back = new Intent(Text_Post_Viewing_Activity.this, General_Feed_Activity.class);
        startActivity(Back);
        finish();
    }

}
