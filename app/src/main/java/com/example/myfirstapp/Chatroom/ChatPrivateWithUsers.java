package com.example.myfirstapp.Chatroom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

    private List<String> list;

    private List<PostStuffMakePrivateChat> listRoom;

    boolean notify = false;
    private int ItemCount;
    private int scrollPosition;
    private String Keyold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_private_with_users);
        SetupUI();
        TakeCareOfThings();
        LoadMessages();
        SendChat();
    }

    private void PositionManager() {
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

        list = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        UID = getIntent().getExtras().get("UID").toString();
        Log.e("UID", UID);

        MessageDatabase = FirebaseDatabase.getInstance().getReference("Messages");

        myDatabase2 = FirebaseDatabase.getInstance().getReference("Private Chatrooms");
        message = ChatInputText.getText().toString();

        listRoom = new ArrayList<>();

        postStuffForChatRoomAdapterNúmeroDos = new PostStuffForChatRoomAdapterNúmeroDos(ChatPrivateWithUsers.this, MessagesList);
    }

    private void LoadMessages() {
        MessageDatabase.child(MyUid).child(UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             PostStuffForChatRoom postStuffForChatRoom = dataSnapshot.getValue(PostStuffForChatRoom.class);

             MessagesList.add(postStuffForChatRoom);
             postStuffForChatRoomAdapterNúmeroDos.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        MessageDatabase.child(MyUid).child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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

                ChatWindow.setAdapter(postStuffForChatRoomAdapterNúmeroDos);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void TakeCareOfThings() {
        myDatabase2.child(MyUid).child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
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
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
                String Date = dateFormat.format(calendar.getTime());
                PostStuffMakePrivateChat postStuffMakePrivateChat1 = new PostStuffMakePrivateChat(false, Date, UID);
                PostStuffMakePrivateChat postStuffMakePrivateChat2 = new PostStuffMakePrivateChat(false, Date, MyUid);
                myDatabase2.child(MyUid).child(UID).setValue(postStuffMakePrivateChat1);
                myDatabase2.child(UID).child(MyUid).setValue(postStuffMakePrivateChat1);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                            message = ChatInputText.getText().toString();
                            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(message, "text", false , Date, MyUid);
                            temp_key = MessageDatabase.push().getKey();
                            MessageDatabase.child(MyUid).child(UID).child(temp_key).setValue(postStuffForChatRoom);
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

  /*  private void FindIntel() {

        myDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, UID);
                Log.e(TAG, MyUid);
                clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String TheJey = MyUid + " + " + UID;

                        String Test = snapshot.child("key").getValue().toString();
                        Log.e("TheKey", TheJey);

                        int childrencount = (int) dataSnapshot.getChildrenCount();
                        Log.e("Childrencount", String.valueOf(childrencount) );
                        PostStuffMakePrivateChat postStuffMakePrivateChat = snapshot.getValue(PostStuffMakePrivateChat.class);
                        Keyold = postStuffMakePrivateChat.getKey();
                        listRoom.add(postStuffMakePrivateChat);

                        if (listRoom.size()==childrencount){
                            for (int i = 0; i < listRoom.size(); i++) {
                                int position;
                                position = i;

                                if (!(listRoom.get(i).getKey().contains(MyUid) && listRoom.get(i).getKey().contains(UID))) {
                                    listRoom.remove(i);
                                }
                                if (listRoom.isEmpty()){
                                    Key = MyUid + " + " + UID;
                                    Log.e(TAG, "makechatroom");
                                    MakeChatroom();
                                }
                            }
                        }




                        if (postStuffMakePrivateChat.getKey().contains(UID) && postStuffMakePrivateChat.getKey().contains(MyUid)) {
                            listRoom.add(postStuffMakePrivateChat);
                        }
                        if (!listRoom.isEmpty()) {
                            Key = listRoom.get(0).getKey();
                        }
                        final int size = listRoom.size();
                        Log.e(TAG, "add bereikt");

                        for (int i = 0; i < listRoom.size(); i++) {
                            int position;
                            position = i;

                            if (listRoom.get(i).getKey().contains(MyUid) && listRoom.get(i).getKey().contains(UID)) {
                                // Key = listRoom.get(position).getKey();
                                Log.e(TAG, "remove bereikt");
                                Log.e("list", listRoom.toString());

                                //} else {
                                listRoom.remove(i);
                                Log.e("listtest", String.valueOf(position));
                                Log.e("listtest2", String.valueOf(listRoom.size()));

                                if (listRoom.isEmpty()) {
                                    Key = MyUid + " + " + UID;
                                    Log.e(TAG, "makechatroom");
                                    MakeChatroom();
                                }


                            }
                           if (!(listRoom.get(i).getKey().contains(MyUid) && listRoom.get(i).getKey().contains(UID))){
                                Key = MyUid + " + " + UID;
                                MakeChatroom();
                                Log.e(TAG, "if listroom.isEmpty bereikt");
                            }

                        }


                        Log.e(TAG, Keyold);
                        //        LoadMessages();
                    }
                   if (Keyold.contains(MyUid) && Keyold.contains(UID)) {
                        list.add(Keyold);

                        Key = list.get(0);
                        Log.e("else", Key);


                    }
                    if (list.isEmpty()) {
                        Key = MyUid + " + " + UID;
                        MakeChatroom();
                        Log.e("IF", Key);
                    }

                } else {
                    Key = MyUid + " + " + UID;
                    MakeChatroom();
                    Log.e("ELSE", Key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }*/



  /*  @Override
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
    }*/

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
