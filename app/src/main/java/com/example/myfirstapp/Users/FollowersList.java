package com.example.myfirstapp.Users;

public class FollowersList {
    private String userName;
    private String theUID;

    public FollowersList(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public FollowersList(String UserName, String TheUID){
        userName = UserName;
        theUID = TheUID;

    }


}
