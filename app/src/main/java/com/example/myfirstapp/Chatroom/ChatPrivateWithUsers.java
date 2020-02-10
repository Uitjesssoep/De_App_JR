package com.example.myfirstapp.Chatroom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatPrivateWithUsers extends AppCompatActivity {

    private DatabaseReference myDatabase, MessageDatabase, myDatabase2;
    private Button SendChatButton;
    private EditText ChatInputText;
    private TextView Conversation_Content;


    private PostStuffForChatAdapter postStuffForChatAdapter;
    private PostStuffForChatRoomAdapterNúmeroDos postStuffForChatRoomAdapterNúmeroDos;

    private String room_name, user_name, Key;
    private String temp_key, TAG = "Test";
    private String message, messageNummeroTwee, UID;

    private String LayoutPosition = "ARGS_SCROLL_POS";
    private String LayoutFloat = "ARGS_SCROLL_OFFSET";

    private String MyUid, Username, Date;
    private FirebaseAuth firebaseAuth;

    private List<PostStuffForChatRoom> MessagesList;

    private RecyclerView ChatWindow;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private RequestQueue requestQueue;


    boolean notify = false;
    private int ItemCount;
    private int scrollPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_private_with_users);
        SetupUI();
        FindIntel();
        SendChat();
    }

    private void PositionManager(){
    }

    private void LoadMessages() {
        myDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();

                for (DataSnapshot snapshot : dataSnapshot.child(Key).child("messages").getChildren()) {
                    PostStuffForChatRoom postStuffForChatRoom = snapshot.getValue(PostStuffForChatRoom.class);
                    MessagesList.add(postStuffForChatRoom);
                    Log.e(TAG, MessagesList.toString());
                    Log.e(TAG, String.valueOf(MessagesList.size()));

                }

             /*   int position = 0;
                LinearLayoutManager manager = (LinearLayoutManager) ChatWindow.getLayoutManager();
                if (manager != null) {
                    scrollPosition = manager.findFirstVisibleItemPosition();
                }
                if (manager != null) {
                    ChatWindow.scrollToPosition(scrollPosition);
                }*/
                postStuffForChatRoomAdapterNúmeroDos = new PostStuffForChatRoomAdapterNúmeroDos(ChatPrivateWithUsers.this, MessagesList);
                ChatWindow.setAdapter(postStuffForChatRoomAdapterNúmeroDos);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetupUI() {
        SendChatButton = findViewById(R.id.btnSendMessageChatPrivate);
        ChatInputText = findViewById(R.id.etChatInputPrivate);
        //  Conversation_Content = (TextView)findViewById(R.id.tvChatWindow);
        ChatWindow = findViewById(R.id.rvChatWindowPrivate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        ChatWindow.setLayoutManager(linearLayoutManager);

        MessagesList = new ArrayList<>();
        //voor scrollen
        // Conversation_Content.setMovementMethod(new ScrollingMovementMethod());


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUid = user.getUid();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        UID = getIntent().getExtras().get("UID").toString();
        Log.e("UID", UID);
       /* if (getIntent().getExtras().get("Key") == null) {
            key = MyUid + " + " + UID;
        } else {
            key = getIntent().getExtras().get("Key").toString();
        }*/


        // myDatabase = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(Key).child("messages");
        // myDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key);

        myDatabase2 = FirebaseDatabase.getInstance().getReference("Private Chatrooms");

        message = ChatInputText.getText().toString();
    }

    private void SendChat() {
        SendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageNummeroTwee = ChatInputText.getText().toString();

                if (messageNummeroTwee.isEmpty()) {
                    Toast.makeText(ChatPrivateWithUsers.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference("users").child(MyUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Username = dataSnapshot.child("userName").getValue().toString();
                            Log.e(TAG, Username);
                            message = ChatInputText.getText().toString();
                            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(message, MyUid, Username, Date);

                            myDatabase = FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(Key).child("messages");
                            Log.e(TAG, Key );
                            temp_key = myDatabase.push().getKey();
                            myDatabase.child(temp_key).setValue(postStuffForChatRoom);
                            Log.e(TAG, "gepushed");
                            ChatInputText.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

    }

    private void FindIntel() {

        myDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue().toString().contains(MyUid) && dataSnapshot.getValue().toString().contains(UID)) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PostStuffMakePrivateChat postStuffMakePrivateChat = snapshot.getValue(PostStuffMakePrivateChat.class);
                            Key = postStuffMakePrivateChat.getKey();
                            LoadMessages();
                        }

                    } else {
                        Key = MyUid + " + " + UID;
                        MakeChatroom();
                    }

                } else {
                    Key = MyUid + " + " + UID;
                    MakeChatroom();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void MakeChatroom() {
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Username1 = dataSnapshot.child(UID).child("userName").getValue().toString();
                String Username2 = dataSnapshot.child(MyUid).child("userName").getValue().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
                String Date = dateFormat.format(calendar.getTime());
                PostStuffMakePrivateChat postStuffMakePrivateChat = new PostStuffMakePrivateChat(Username1, Username2, UID, MyUid, Key, Date);

                myDatabase2.child(Key).setValue(postStuffMakePrivateChat);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference("Private Chatrooms").child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("messages")) {
                    Log.e(TAG, "FALSE");

                    myDatabase2.child(Key).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void clear() {

        int size = MessagesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MessagesList.remove(0);

                String TAGTest = "ListEmpty";
                // Log.e(TAGTest, "tot 'for' gekomen");
            }

            postStuffForChatRoomAdapterNúmeroDos.notifyItemRangeRemoved(0, size);
        }
    }


}
