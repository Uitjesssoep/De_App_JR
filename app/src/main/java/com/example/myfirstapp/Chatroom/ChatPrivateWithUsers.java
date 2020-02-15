package com.example.myfirstapp.Chatroom;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.myfirstapp.SharedPrefNightMode;
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

    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference myDatabase, MessageDatabase, myDatabase2;
    private ImageButton SendChatButton, SendImageButton;
    private EditText ChatInputText;
    private TextView Conversation_Content;


    private PostStuffForChatAdapter postStuffForChatAdapter;
    private PostStuffForChatRoomAdapterNúmeroDos postStuffForChatRoomAdapterNúmeroDos;

    private String room_name, user_name, Key;
    private String temp_key, TAG = "Test";
    private String message, messageNummeroTwee, UID;

    private Uri mImageUri;

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

    SharedPrefNightMode sharedPrefNightMode;

    boolean notify = false;
    private int ItemCount;
    private int scrollPosition;
    private String Keyold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_private_with_users);
        SetupUI();
        TakeCareOfThings();
        LoadMessages();
        SendImage();
        SendChat();

    }

    private void PositionManager() {
    }


    private void SetupUI() {
        SendImageButton = findViewById(R.id.ibSendImageChatPrivate);
        SendChatButton = findViewById(R.id.ibSendMessageChatPrivate);
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
        ChatWindow.setAdapter(postStuffForChatRoomAdapterNúmeroDos);


    }

    private void LoadMessages() {
        MessageDatabase.child(MyUid).child(UID).orderByChild("mDate").addChildEventListener(new ChildEventListener() {
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


        //sortDate();

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
                myDatabase2.child(UID).child(MyUid).setValue(postStuffMakePrivateChat2);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Intent intent = new Intent(ChatPrivateWithUsers.this, ImageTemporaryViewingPrivateChat.class);
                intent.putExtra("mImageUri", mImageUri);
                intent.putExtra("UID", UID);
                intent.putExtra("Message", ChatInputText.getText().toString());
                startActivity(intent);
            }
        }
    }


    private String getFileExtension(Uri uri) { //om extension van bestand te krijgen
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void SendImage(){
        SendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
                            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(message, "text", false, Date, MyUid, "");
                            temp_key = MessageDatabase.push().getKey();
                            MessageDatabase.child(MyUid).child(UID).child(temp_key).setValue(postStuffForChatRoom);
                            MessageDatabase.child(UID).child(MyUid).child(temp_key).setValue(postStuffForChatRoom);
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
