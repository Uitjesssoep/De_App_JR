package com.example.myfirstapp.Chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_Activity;
import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Textposts.General_Feed_Activity;
import com.example.myfirstapp.Textposts.PostStuffForText;
import com.example.myfirstapp.Textposts.PostStuffForTextAdapter;
import com.example.myfirstapp.Textposts.Text_Post_Viewing_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chat_Room_MakeOrSearch_Activity extends AppCompatActivity {

    private RecyclerView RoomList;
    private List<PostStuffForChat> postStuffForChatList;
    private PostStuffForChatAdapter postStuffForChatAdapter;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Chatrooms");
    private DatabaseReference DatabaseDislike, DatabaseLike;

    private String MyUID, key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room__make_or_search_);

        SetupUI();

        rooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PostStuffForChat postStuffForChat = postSnapshot.getValue(PostStuffForChat.class);
                    postStuffForChatList.add(postStuffForChat);
                }

                postStuffForChatAdapter = new PostStuffForChatAdapter(Chat_Room_MakeOrSearch_Activity.this, postStuffForChatList);
                RoomList.setAdapter(postStuffForChatAdapter);

                postStuffForChatAdapter.setOnItemClickListener(new PostStuffForChatAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        key = postStuffForChatList.get(position).getKey().toString();
                        Intent Test2 = new Intent(getApplicationContext(), Chat_With_Users_Activity.class);
                        Test2.putExtra("Key", key);
                        startActivity(Test2);
                        finish();
                    }

                    @Override
                    public void onDeleteIconClick(int position) {
                        final String TAGCheck = "DeleteTextPost";
                        Log.e(TAGCheck, "Deleting Text Post After Click");

                        final String ThePostKey = postStuffForChatList.get(position).getKey().toString();

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Chat_Room_MakeOrSearch_Activity.this);
                        dialog.setTitle("Delete your post?");
                        dialog.setMessage("Deleting this post cannot be undone! Are you sure you want to delete it?");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference DeleteThePost = FirebaseDatabase.getInstance().getReference("Chatrooms").child(ThePostKey);
                                DeleteThePost.removeValue();

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

                    @Override
                    public void onUserNameClick(int position) {
                        final String PostKey = postStuffForChatList.get(position).getKey().toString();

                        DatabaseReference CheckIfMyUID = FirebaseDatabase.getInstance().getReference("Chatrooms").child(PostKey).child("uid");
                        CheckIfMyUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String MyUIDCheck2 = FirebaseAuth.getInstance().getUid().toString();
                                final String PostUID2 = dataSnapshot.getValue().toString();

                                DatabaseReference CheckIfDeleted = FirebaseDatabase.getInstance().getReference("users");
                                CheckIfDeleted.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(PostUID2)){

                                            if(MyUIDCheck2.equals(PostUID2)){

                                                Intent GoToMyProfile = new Intent(Chat_Room_MakeOrSearch_Activity.this, Account_Info_Activity.class);
                                                startActivity(GoToMyProfile);

                                            }
                                            else{

                                                Intent GoToProfile = new Intent(Chat_Room_MakeOrSearch_Activity.this, Account_Info_OtherUser_Activity.class);
                                                GoToProfile.putExtra("Key", PostKey);
                                                startActivity(GoToProfile);

                                            }

                                        }

                                        else{

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(Chat_Room_MakeOrSearch_Activity.this);
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
                    public void onUpvoteClick(int position) {
                        key = postStuffForChatList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){

                                    DatabaseDislike.child(MyUID).removeValue();
                                    DatabaseLike.child(MyUID).setValue("RandomLike");

                                }


                                else{

                                    DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){
                                                DatabaseLike.child(MyUID).removeValue();
                                            }

                                            else{
                                                DatabaseLike.child(MyUID).setValue("RandomLike");
                                            }

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

                    @Override
                    public void onDownvoteClick(int position) {
                        key = postStuffForChatList.get(position).getKey().toString();
                        MyUID = firebaseAuth.getCurrentUser().getUid().toString();
                        DatabaseLike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Likes");
                        DatabaseDislike = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("Dislikes");
                        final String TAGDownvote = "VoteCheck";

                        DatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(MyUID)){
                                    DatabaseLike.child(MyUID).removeValue();
                                    DatabaseDislike.child(MyUID).setValue("RandomDislike");
                                }

                                else{

                                    DatabaseDislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.hasChild(MyUID)){

                                                DatabaseDislike.child(MyUID).removeValue();

                                            }

                                            else{

                                                DatabaseDislike.child(MyUID).setValue("RandomDislike");

                                            }

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

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetupUI() {

        RoomList = findViewById(R.id.rvChatroomFeed);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RoomList.setLayoutManager(new LinearLayoutManager(this));

        postStuffForChatList = new ArrayList<>();

    }

}
