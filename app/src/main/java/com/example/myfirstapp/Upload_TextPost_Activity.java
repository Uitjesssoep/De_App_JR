package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Upload_TextPost_Activity extends AppCompatActivity {


    private EditText Title, Content;
    private CheckBox DisableComments, VisibleForEveryone;
    private Button Post;

    private String TitleContentOne, TitleContentTwo, TextContentOne, TextContentTwo;


    private void SetupUI() {

        Title = findViewById(R.id.etTitleTextPost);
        Content = findViewById(R.id.etContentTextPost);
        DisableComments = findViewById(R.id.cbDisableComments);
        VisibleForEveryone = findViewById(R.id.cbVisibleForEveryone);
        Post = findViewById(R.id.btnPostTextPost);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__text_post);

        SetupUI();

        TitleContentOne = Title.getText().toString();
        TextContentOne = Content.getText().toString();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TitleContentOne.isEmpty() || TextContentOne.isEmpty()){
                    Toast.makeText(Upload_TextPost_Activity.this, "Please fill in a title and the content", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void ClickPostButton() {



    }


}
