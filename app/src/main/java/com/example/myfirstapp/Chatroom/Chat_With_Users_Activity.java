package com.example.myfirstapp.Chatroom;

import android.content.Intent;
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
import com.example.myfirstapp.Textposts.General_Feed_Activity;
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

public class Chat_With_Users_Activity extends AppCompatActivity {


    private DatabaseReference myDatabase, MessageDatabase;
    private Button SendChatButton;
    private EditText ChatInputText;
    private TextView Conversation_Content;

    private PostStuffForChatRoomAdapter postStuffForChatRoomAdapter;
    private PostStuffForChatAdapter postStuffForChatAdapter;

    private String room_name, user_name;
    private String temp_key, TAG = "Test";
    private String message, messageNummeroTwee, key;

    private String MyUid, Username, Date;
    private FirebaseAuth firebaseAuth;

    private ArrayList<PostStuffForChatRoom> MessagesList;

    private RecyclerView ChatWindow;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private RequestQueue requestQueue;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__with__users_);

        SetupUI();
        LoadMessages();
        SendChat();

    }

    private void LoadMessages() {
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MessagesList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostStuffForChatRoom postStuffForChatRoom = snapshot.getValue(PostStuffForChatRoom.class);
                    MessagesList.add(postStuffForChatRoom);

                }
                postStuffForChatRoomAdapter = new PostStuffForChatRoomAdapter(Chat_With_Users_Activity.this, MessagesList);
                ChatWindow.setAdapter(postStuffForChatRoomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetupUI() {
        SendChatButton = findViewById(R.id.btnSendMessageChat);
        ChatInputText = findViewById(R.id.etChatInput);
        //  Conversation_Content = (TextView)findViewById(R.id.tvChatWindow);
        ChatWindow = findViewById(R.id.rvChatWindow);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        ChatWindow.setLayoutManager(linearLayoutManager);

        MessagesList = new ArrayList<>();
        //voor scrollen
        // Conversation_Content.setMovementMethod(new ScrollingMovementMethod());


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        MyUid = user.getUid();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date = dateFormat.format(calendar.getTime());


        requestQueue = Volley.newRequestQueue(getApplicationContext());


        key = getIntent().getExtras().get("Key").toString();

        myDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms");
        // myDatabase = FirebaseDatabase.getInstance().getReference("Chatrooms").child(key);

        message = ChatInputText.getText().toString();
    }

    private void SendChat() {
        SendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageNummeroTwee = ChatInputText.getText().toString();

                if (messageNummeroTwee.isEmpty()) {
                    Toast.makeText(Chat_With_Users_Activity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference("users").child(MyUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Username = dataSnapshot.child("userName").getValue().toString();
                            Log.e(TAG, Username);
                            message = ChatInputText.getText().toString();
                            PostStuffForChatRoom postStuffForChatRoom = new PostStuffForChatRoom(message, MyUid, Username, Date);

                            temp_key = myDatabase.child(key).push().getKey();
                            myDatabase.child(key).child("messages").child(temp_key).setValue(postStuffForChatRoom);
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

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            Conversation_Content.append(
                    chat_user_name + " : "+ " \n" +chat_msg +" \n" + " \n"
            );

        }

    }*/

    }


    public void onBackPressed() {
        Intent Back = new Intent(Chat_With_Users_Activity.this, General_Feed_Activity.class);
        startActivity(Back);
        finish();
    }

}
