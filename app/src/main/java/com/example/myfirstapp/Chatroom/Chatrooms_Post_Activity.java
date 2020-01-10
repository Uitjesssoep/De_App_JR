package com.example.myfirstapp.Chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.R;

public class Chatrooms_Post_Activity extends AppCompatActivity {

    EditText Title;
    Button Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrooms__post_);

        SetupUI();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TitleString = Title.getText().toString();

                if(TitleString.isEmpty()){
                    Toast.makeText(Chatrooms_Post_Activity.this, "You must have a title", Toast.LENGTH_LONG);
                }
                else{

                }

            }
        });
    }

    private void SetupUI() {

        Title = findViewById(R.id.etTitleChatroom);
        Post = findViewById(R.id.btnPostChatRoom);

    }
}
