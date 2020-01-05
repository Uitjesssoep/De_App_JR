package com.example.myfirstapp;

public class UserProfileToDatabase {

    public String userBirthdate;
    public String userName;
    public String userEmail;
    public String userFullName;
    public String userPassword;
    public String Profilepicture;
    public String UID;

    public UserProfileToDatabase(){
        //een lege functie moet samenkomen met de getters and setters, vandaar dat deze functie er is
    }




    public UserProfileToDatabase(String Profilepicture, String UID, String userName, String userEmail, String userFullName, String userBirthdate) {
        this.Profilepicture = Profilepicture;
        this.UID = UID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.userBirthdate = userBirthdate;
    }


    //getters and setters:

    public String getProfilepicture() {
        return Profilepicture;
    }
    public void setProfilepicture(String Profilepicture) {
        Profilepicture = Profilepicture;
    }
    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
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
