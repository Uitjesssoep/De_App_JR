package com.example.myfirstapp.AccountActivities;

public class UserProfileToDatabase {

    public String userBirthdate;
    public String userName;
    public String userEmail;
    public String userFullName;
    public String userPassword;
    public String profilePicture;
    public String theUID;

    public UserProfileToDatabase(){
        //een lege functie moet samenkomen met de getters and setters, vandaar dat deze functie er is
    }




    public UserProfileToDatabase(String profilePicture, String theUID, String userName, String userEmail, String userFullName, String userBirthdate) {
        this.profilePicture = profilePicture;
        this.theUID = theUID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.userBirthdate = userBirthdate;
    }


    //getters and setters:

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTheUID() {
        return theUID;
    }
    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public String getUserBirthdate() {
        return userBirthdate;
    }

    public void setUserBirthdate(String userBirthdate) {
        this.userBirthdate = userBirthdate;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
