package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Search_Other_Users_Activity extends AppCompatActivity {


    private EditText SearchBar;
    private ImageButton SearchIcon;
    private RecyclerView ResultList;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
 /*       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__other__users);

        SetupUI();

        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseUserSearch();

            }
        });

    }

    private void SetupUI(){

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        SearchBar = findViewById(R.id.etSearchBar_OtherUsers);
        SearchIcon = findViewById(R.id.ibtnSearchIcon_OtherUsers);
        ResultList = findViewById(R.id.rvSearchOtherUsers_ResultList);

    }


    private void firebaseUserSearch(){

        FirebaseRecyclerAdapter<Users_ForListLayout, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users_ForListLayout, UsersViewHolder>(

                Users_ForListLayout.class,
                R.layout.search_other_users_list_layout,
                UsersViewHolder.class,
                mUserDatabase
                //yeah

        ) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users_ForListLayout model) {

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };

    }


    //code heel sterk afgeleid van internet, weet niet precies hoe het werkt helaas

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }*/
    }


}

