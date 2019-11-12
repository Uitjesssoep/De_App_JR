package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;
import java.util.Set;

public class Chat_Room_MakeOrSearch_Activity extends AppCompatActivity {


    private Button add_room;
    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private TextView user_name_gebruiker;

    private DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("Rooms");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room__make_or_search_);

        user_name_gebruiker = (TextView)findViewById(R.id.tvUserNameStorage);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        add_room = (Button)findViewById(R.id.btnCreateRoomChat);
        room_name = (EditText)findViewById(R.id.etRoomCreate);
        listView = (ListView)findViewById(R.id.lvRoomsForChat);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);

        listView.setAdapter(arrayAdapter);


        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(), "");
                rooms.updateChildren(map);
                room_name.setText("");

            }
        });

        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase userProfile = dataSnapshot.getValue(UserProfileToDatabase.class);
                user_name_gebruiker.setText(userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chat_Room_MakeOrSearch_Activity.this, "Couldn't retrieve data from database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), Chat_With_Users_Activity.class);
                intent.putExtra("room_name",((TextView)view).getText().toString() );
                intent.putExtra("user_name", user_name_gebruiker.getText().toString());
                startActivity(intent);

            }
        });

    }
}
