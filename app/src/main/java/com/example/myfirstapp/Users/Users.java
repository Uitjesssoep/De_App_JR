package com.example.myfirstapp.Users;

import android.widget.ImageView;

public class Users {

    public String username, profilePictureUrl, birthdate, email, fullName, theUID;

    public Users() {
    }


    public String getUsername() {
        return username;
    }

    public Users(String ProfilePicture, String theUID, String userBirthdate, String userEmail, String userFullName, String UserName) {
        this.theUID = theUID;
        username = UserName;
        profilePictureUrl = ProfilePicture;
        birthdate = userBirthdate;
        email = userEmail;
        fullName = userFullName;
    }

    public String getTheUID() {
        return theUID;
    }



    public String getProfilePicture() {

        return profilePictureUrl;
    }

    public String getBrithdate() {

        return birthdate;
    }

    public String getEmail() {

        return email;
    }

    public String getFullName() {

        return fullName;
    }


}
