package com.example.myfirstapp.Chatroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.Timer;
import java.util.TimerTask;

public class Chat_With_Users_Activity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference myDatabase, MessageDatabase, myDatabase2;
    private ImageButton SendChatButton, SendImageButton;
    private EditText ChatInputText;
    private TextView Conversation_Content;

    private ImageButton Exit;
    private TextView TitleActionBar;

    private Boolean SendImageVisible = true;

    private PostStuffForChatAdapter postStuffForChatAdapter;
    private PostStuffForChatRoomGroupAdapter postStuffForChatRoomGroupAdapter;

    private String room_name, user_name;
    private String temp_key, TAG = "Test";
    private String message, messageNummeroTwee, key;

    private String LayoutPosition = "ARGS_SCROLL_POS";
    private String LayoutFloat = "ARGS_SCROLL_OFFSET";

    private String MyUid, Username, Date;
    private FirebaseAuth firebaseAuth;

    private List<PostStuffForChatRoom> MessagesList;

    private LinearLayoutManager linearLayoutManager;

    private RecyclerView ChatWindow;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private long Timestamp;

    private boolean isLoading, totallyLoaded;

    private RequestQueue requestQueue;

    SharedPrefNightMode sharedPrefNightMode;

    boolean notify = false;
    private int ItemCount;
    private int scrollPosition;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefNightMode = new SharedPrefNightMode(this);

        if(sharedPrefNightMode.loadNightModeState()==true){
            setTheme(R.style.AppTheme_Night);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__with__users_);

        SetupUI();
        LoadMessages();
        SendChat();
        SendImage();
        SetupDesign();

    }


 /*   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager manager = (LinearLayoutManager) ChatWindow.getLayoutManager();
        if (manager != null && manager instanceof LinearLayoutManager) {
            int firstItem = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
            Log.e("Test2", String.valueOf(firstItem));
            View firstItemView = manager.findViewByPosition(firstItem);
            float topOffset = firstItemView.getTop();
            outState.putInt(LayoutPosition, firstItem);
            outState.putFloat(LayoutFloat, topOffset);
            manager.scrollToPosition(firstItem);
            manager.scrollToPositionWithOffset(outState.getInt(LayoutPosition), (int) outState.getFloat(LayoutFloat));
            Log.e(TAG, "succes adapter");
            ItemCount = postStuffForChatRoomAdapterNúmeroDos.getItemCount();
        }
    }*/


    /*private boolean setChatScrollPosistion(int position, String chat_id){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position",position);
        return editor.commit();
    }
    private int getLastPosition(String chat_id){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt("position",0);
    }*/

    private void LoadMessages() {
        myDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                PostStuffForChatRoom postStuffForChatRoom = dataSnapshot.getValue(PostStuffForChatRoom.class);
                MessagesList.add(postStuffForChatRoom);
                postStuffForChatRoomGroupAdapter = new PostStuffForChatRoomGroupAdapter(Chat_With_Users_Activity.this, MessagesList);
                ChatWindow.setAdapter(postStuffForChatRoomGroupAdapter);
                postStuffForChatRoomGroupAdapter.notifyDataSetChanged();
                FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("date").setValue(System.currentTimeMillis());
                ChatWindow.scrollToPosition(postStuffForChatRoomGroupAdapter.getItemCount()-1);
               // ChatWindow.scrollToPosition(position);
               // setChatScrollPosistion(0, key );
              //  int RVPosition = getLastPosition(key);
              //  ChatWindow.scrollToPosition(RVPosition);
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

        /*myDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostStuffForChatRoom postStuffForChatRoom = snapshot.getValue(PostStuffForChatRoom.class);
                    MessagesList.add(postStuffForChatRoom);
                    Log.e(TAG, MessagesList.toString());
                    Log.e(TAG, String.valueOf(MessagesList.size()));


                }
              /*  int position = 0;
                LinearLayoutManager manager = (LinearLayoutManager) ChatWindow.getLayoutManager();
                if (manager != null) {
                    scrollPosition = manager.findFirstVisibleItemPosition();
                }
                if (manager != null) {
                    ChatWindow.scrollToPosition(scrollPosition);
                }
                postStuffForChatRoomGroupAdapter = new PostStuffForChatRoomGroupAdapter(Chat_With_Users_Activity.this, MessagesList);
                ChatWindow.setAdapter(postStuffForChatRoomGroupAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    private void SetupUI() {

        SendImageButton = findViewById(R.id.ibSendImageChat);

        SendImageButton.bringToFront();

        SendChatButton = findViewById(R.id.ibSendMessageChat);
        ChatInputText = findViewById(R.id.etChatInput);
        //  Conversation_Content = (TextView)findViewById(R.id.tvChatWindow);
        ChatWindow = findViewById(R.id.rvChatWindow);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        ChatWindow.setLayoutManager(linearLayoutManager);

        MessagesList = new ArrayList<>();
        //voor scrollen
        // Conversation_Content.setMovementMethod(new ScrollingMovementMethod());


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUid = user.getUid();


        Timestamp = System.currentTimeMillis();
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Timestamp);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());


        requestQueue = Volley.newRequestQueue(getApplicationContext());


        key = getIntent().getExtras().get("Key").toString();

        myDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms");
        // myDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key);

        myDatabase2 = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key).child("messages");

        message = ChatInputText.getText().toString();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String Input = ChatInputText.getText().toString();
                if(!Input.isEmpty()){
                    if(SendImageVisible){
                        SendImageVisible = false;
                    }
                }
                else {
                    SendImageVisible = true;
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if(SendImageVisible){
                            SendImageButton.setVisibility(View.VISIBLE);
                        }
                        else{
                            SendImageButton.setVisibility(View.INVISIBLE);
                        }

                    }
                });

            }
        }, 0, 100);

    }

    

    private void SendChat() {
        SendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageNummeroTwee = ChatInputText.getText().toString();

                if (messageNummeroTwee.isEmpty()) {
                    Toast.makeText(Chat_With_Users_Activity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                    //    LoadMessages();
                } else {
                    //  LoadMessages();
                    FirebaseDatabase.getInstance().getReference("users").child(MyUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Username = dataSnapshot.child("userName").getValue().toString();
                            Log.e(TAG, Username);
                            message = ChatInputText.getText().toString();
                            temp_key = myDatabase2.push().getKey();
                            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(message, "text", false, Timestamp, MyUid, "", temp_key);


                            myDatabase2.child(temp_key).setValue(postStuffForChatRoom);
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

    private void SendImage() {
        SendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
                Intent intent = new Intent(Chat_With_Users_Activity.this, ImageTemporaryViewingPrivateChat.class);
                intent.putExtra("mImageUri", mImageUri);
                intent.putExtra("UID", MyUid);
                intent.putExtra("Message", ChatInputText.getText().toString());
                intent.putExtra("key", key);
                startActivity(intent);
            }
        }
    }

    public void clear() {

        int size = MessagesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MessagesList.remove(0);

                String TAGTest = "ListEmpty";
                // Log.e(TAGTest, "tot 'for' gekomen");
            }

            postStuffForChatRoomGroupAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    private void SetupDesign() {

        //action bar ding

        final Toolbar toolbar = findViewById(R.id.action_bar_chatroom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = (ImageButton) toolbar.findViewById(R.id.exitchatprivate);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String TheChatKey = getIntent().getExtras().get("Key").toString();
        DatabaseReference GetName = FirebaseDatabase.getInstance().getReference("Chatrooms").child(TheChatKey).child("title");
        GetName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String TheName = dataSnapshot.getValue().toString();

                TitleActionBar = toolbar.findViewById(R.id.titlechatprivate);
                TitleActionBar.setText(TheName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void Notifications() {
        //voor notificaties
                   /* notify = true;

                    String msg = message;
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfileToDatabase userName = dataSnapshot.getValue(UserProfileToDatabase.class);

                            if (notify) {
                                senNotification(userName.getUserName(), message);
                            }
                            notify = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                   /* myDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                append_chat_conversation(dataSnapshot);

                //Notificatie maken


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                append_chat_conversation(dataSnapshot);

                //Notificatie maken

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
        });*/


//allemaal voor notificaties, negeer dit maar

   /* private void senNotification(String userName, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(MyUid, message,  user_name+":", R.drawable.neutral_profile_picture_nobackground);

                    Sender sender = new Sender(data, token.getToken());

                    try{
                        JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("JSON_RESPONSE", "onResponse: "+response.toString());

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("JSON_RESPONSE", "onResponse: "+error.toString());


                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {

                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAAvuDOxn0:APA91bGC2z_0nxG_1Z9-59thpomklhEBEM97Tqgnu8gfu_uQ_LMoInuL6EkjdIeRRFBc3rzDf5dFb9SnlKGtTjuUz2iYnay9mFF124F-zs9nNCBZLW9wQhuVaAK_yIIKLnNXw5HWI34Q");

                                return headers;
                            }
                        };


                        requestQueue.add(jsonObjectRequest);


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/






  /*  private String chat_msg, chat_user_name;

    private void append_chat_conversation (DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
        while(i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            Conversation_Content.append(
                    chat_user_name + " : "+ " \n" +chat_msg +" \n" + " \n"
            );

        }

    }*/

    }
}
