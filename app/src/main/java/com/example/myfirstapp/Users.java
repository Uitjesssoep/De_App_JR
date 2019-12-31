package com.example.myfirstapp;

import android.widget.ImageView;

public class Users {

    public String UserName;
    public String ProfilePictureUrl;

    public Users(){}

    public Users(String Username, String Profilepicture){
        UserName = Username;
        ProfilePictureUrl = Profilepicture;
    }

    public String getUsernameFollow(){


        return UserName;}
    public String getProfilePicture(){

        return ProfilePictureUrl;}




}
