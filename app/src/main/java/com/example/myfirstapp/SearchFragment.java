package com.example.myfirstapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
//import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.AccountActivities.Account_Info_OtherUser_Activity_Users;
import com.example.myfirstapp.AccountActivities.UserProfileToDatabase;
import com.example.myfirstapp.Users.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPrefNightMode sharedPrefNightMode = new SharedPrefNightMode(getActivity());

        if(sharedPrefNightMode.loadNightModeState()==true){
            getContext().setTheme(R.style.AppTheme_Night);
        }
        else getContext().setTheme(R.style.AppTheme);

        return inflater.inflate(R.layout.fragment_fragment_searchlayout, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Refresh();
        CheckInternet();

    }

    private void CheckInternet() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (connected) {

        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("No internet connection");
            dialog.setMessage("Please connect to the internet and try again");
            dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            });

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
    }

    private void Refresh() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final ProgressBar ProgressCircle = getView().findViewById(R.id.pbLoadingUserListToFollow_fragment);
        final RecyclerView recyclerView = getView().findViewById(R.id.recycler_viewUserList_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference databaseReferenceUIDlist = firebaseDatabase.getReference("users");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        final List<UserProfileToDatabase> list = new ArrayList<>();
        List<String> UIDlist = new ArrayList<>();
        final UserAdapter adapter = null;
        final String MyUID = firebaseAuth.getUid();
        final DatabaseReference datarefUID = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("following");
        final DatabaseReference datarefFollowing = FirebaseDatabase.getInstance().getReference().child("users");

        DatabaseReference databaseReference = firebaseDatabase.getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserProfileToDatabase users = postSnapshot.getValue(UserProfileToDatabase.class);
                    list.add(users);
                    int position;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getTheUID().equals(MyUID)) {
                            position = i;
                            list.remove(position);
                            Log.d("list", list.toString());
                        }
                    }

                }

                UserAdapter adapter;

                adapter = new UserAdapter(getActivity(), list);

                final UserAdapter userAdapter = new UserAdapter(getActivity(), list);
                final SearchView searchView = getView().findViewById(R.id.svSearchUsers);

                searchView.setIconifiedByDefault(true);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        userAdapter.getFilter().filter(s);
                        return false;
                    }
                });

                recyclerView.setAdapter(userAdapter);
                ProgressCircle.setVisibility(View.INVISIBLE);

                userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String UIDOtherUser = list.get(position).getTheUID();
                        Log.e("Checkj", "test");
                        Intent intent = new Intent(getActivity().getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);

                        startActivity(intent);
                    }

                    @Override
                    public void onUserNameClick(int position) {
                        String UIDOtherUser = list.get(position).getTheUID();
                        Intent intent = new Intent(getActivity().getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onProfilePictureClick(int position) {
                        String UIDOtherUser = list.get(position).getTheUID();
                        Log.e("Check", UIDOtherUser);
                        Intent intent = new Intent(getActivity().getApplicationContext(), Account_Info_OtherUser_Activity_Users.class);
                        intent.putExtra("UID", UIDOtherUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onFollowClick(int position) {
                        final String UIDOtherUser = list.get(position).getTheUID();
                        final String UsernameOtherUser = list.get(position).getUserName();
                        final DatabaseReference datarefOtherUID = FirebaseDatabase.getInstance().getReference().child("users").child(UIDOtherUser).child("followers");
                        datarefUID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(UIDOtherUser)) {
                                    Log.e("Check", "TRUEE");
                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                    dialog.setTitle("Unfollow");
                                    dialog.setMessage("Are you sure you want to unfollow this user?");
                                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            datarefUID.child(UIDOtherUser).removeValue();
                                            datarefOtherUID.child(MyUID).removeValue();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                } else {

                                    DatabaseReference datarefFollower = FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("userName");

                                    datarefFollower.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String userNameFollower = dataSnapshot.getValue().toString();
                                            datarefFollowing.child(UIDOtherUser).child("followers").child(MyUID).setValue(userNameFollower);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    datarefFollowing.child(MyUID).child("following").child(UIDOtherUser).setValue(UsernameOtherUser);
                                    Log.e("Check", "FALSEEE");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }

            public void clear() {
                int size = list.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        list.remove(0);
                    }
                    adapter.notifyItemRangeRemoved(0, size);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                ProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }
}
