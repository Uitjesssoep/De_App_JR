package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_PC_Activity extends AppCompatActivity {

    private ImageButton Exit;
    private TextView TitleType, CurrentTitle, CurrentContent;
    private EditText EditTitle, EditContent;
    private View divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__pc_);

        SetupUI();
        SetupDesign();

    }

    private void SetupUI() {

        TitleType = findViewById(R.id.tvAddEditToPC);
        CurrentTitle = findViewById(R.id.tvCurentTitleOrComment);
        CurrentContent = findViewById(R.id.tvCurrentContentPC);
        EditTitle = findViewById(R.id.etAddEditToPCTitleComment);
        EditContent = findViewById(R.id.etAddEditToPCContent);
        divider = findViewById(R.id.dividerForPC);

        String TextPost = "TextPost";
        String ImagePost = "ImagePost";
        String Comment = "Comment";
        String Type = getIntent().getExtras().get("Type").toString();

        if(Type.equals(TextPost)){
            TitleType.setText("Edit post:");
            String TextKey = getIntent().getExtras().get("Key").toString();

            DatabaseReference GetData = FirebaseDatabase.getInstance().getReference("General_Posts").child(TextKey);
            GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String NowTitle = dataSnapshot.child("title").getValue().toString();
                    CurrentTitle.setText(NowTitle);

                    String NowContent = dataSnapshot.child("content").getValue().toString();
                    CurrentContent.setText(NowContent);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        if(Type.equals(ImagePost)){

            EditContent.setVisibility(View.GONE);
            CurrentContent.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);

            TitleType.setText("Edit post:");
            String ImageKey = getIntent().getExtras().get("Key").toString();

            DatabaseReference GetData = FirebaseDatabase.getInstance().getReference("General_Posts").child(ImageKey);
            GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String NowTitle = dataSnapshot.child("title").getValue().toString();
                    CurrentTitle.setText(NowTitle);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        if(Type.equals(Comment)){

            EditContent.setVisibility(View.GONE);
            CurrentContent.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);

            TitleType.setText("Edit comment:");
            String PostKey = getIntent().getExtras().get("PostKey").toString();
            String CommentKey = getIntent().getExtras().get("CommentKey").toString();

            DatabaseReference GetData = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey).child("Comments").child(CommentKey);
            GetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String NowComment = dataSnapshot.child("content").getValue().toString();
                    CurrentTitle.setText(NowComment);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

    private void SetupDesign() {

        Window window = Edit_PC_Activity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Edit_PC_Activity.this, R.color.slighly_darker_mainGreen));

        Toolbar toolbar = findViewById(R.id.action_bar_editPC);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Exit = toolbar.findViewById(R.id.exitmakecommenttextpost);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_makecomment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post_comment:
                MakeEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void MakeEdit() {

        String TextPost = "TextPost";
        String ImagePost = "ImagePost";
        String Comment = "Comment";
        String Type = getIntent().getExtras().get("Type").toString();

        if(Type.equals(TextPost)){

            if(!EditTitle.getText().toString().isEmpty()) {
                String AddedToTitle = EditTitle.getText().toString();
                String NowTitle = CurrentTitle.getText().toString();
                String NewTitle = NowTitle + "\n[EDIT]: " + AddedToTitle;

                String TextKey = getIntent().getExtras().get("Key").toString();
                DatabaseReference AddEditsTextPost = FirebaseDatabase.getInstance().getReference("General_Posts").child(TextKey);

                AddEditsTextPost.child("title").setValue(NewTitle);
            }
            if(!EditContent.getText().toString().isEmpty()) {
                String AddedToContent = EditContent.getText().toString();
                String NowContent = CurrentContent.getText().toString();
                String NewContent = NowContent + "\n\n[EDIT]: " + AddedToContent;

                String TextKey = getIntent().getExtras().get("Key").toString();
                DatabaseReference AddEditsTextPost = FirebaseDatabase.getInstance().getReference("General_Posts").child(TextKey);

                AddEditsTextPost.child("content").setValue(NewContent);
            }

            finish();
        }

        if(Type.equals(ImagePost)){

            if(!EditTitle.getText().toString().isEmpty()) {
                String AddedToTitle = EditTitle.getText().toString();
                String NowTitle = CurrentTitle.getText().toString();
                String NewTitle = NowTitle + "\n[EDIT]: " + AddedToTitle;

                String TextKey = getIntent().getExtras().get("Key").toString();
                DatabaseReference AddEditsTextPost = FirebaseDatabase.getInstance().getReference("General_Posts").child(TextKey);

                AddEditsTextPost.child("title").setValue(NewTitle);
            }

            finish();
        }

        if(Type.equals(Comment)){

            if(!EditTitle.getText().toString().isEmpty()) {
                String AddedToComment = EditTitle.getText().toString();
                String NowComment = CurrentTitle.getText().toString();
                String NewComment = NowComment + "\n\n[EDIT]: " + AddedToComment;

                String PostKey = getIntent().getExtras().get("PostKey").toString();
                String CommentKey = getIntent().getExtras().get("CommentKey").toString();
                DatabaseReference AddEditsTextPost = FirebaseDatabase.getInstance().getReference("General_Posts").child(PostKey).child("Comments").child(CommentKey);

                AddEditsTextPost.child("content").setValue(NewComment);
            }

            finish();
        }

    }
}
