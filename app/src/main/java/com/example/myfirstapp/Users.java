package com.example.myfirstapp;

import android.widget.ImageView;

public class Users {

    public String username, profilePictureUrl, birthdate, email, fullName, theUID;

    public Users() {
    }


    public Users(String Profilepicture, String theUID, String userBirthdate, String userEmail, String userFullName, String userName) {
        this.theUID = theUID;
        username = userName;
        profilePictureUrl = Profilepicture;
        birthdate = userBirthdate;
        email = userEmail;
        fullName = userFullName;
    }

    public String getTheUID() {
        return theUID;
    }

    public String getUsernameFollow() {


        return username;
    }

    public String getProfilePicture() {

        return profilePictureUrl;
    }

    public String getBrithdateFollow() {

        return birthdate;
    }

    public String getEmailFollow() {

        return email;
    }

    public String getFullName() {

        return fullName;
    }


}
