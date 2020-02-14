package com.example.myfirstapp.Chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myfirstapp.R;
import com.squareup.picasso.Picasso;

public class ImageTemporaryViewingPrivateChat extends AppCompatActivity {
    ImageView Image = findViewById(R.id.ivImageTemporaryForChat);
    Button SendImage = findViewById(R.id.btSendImageToChat);
    Uri mImageUri = (Uri) getIntent().getExtras().get("ImageUri");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_temporary_viewing_private_chat);

        Picasso.get().load(mImageUri).fit().centerCrop().into(Image);
        SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
