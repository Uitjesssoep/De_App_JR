package com.example.myfirstapp.Chatroom;

public class PostStuffForChatRoom {
    private String mMessage, mUID, mUserName, mDate;


    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String mUID) {
        this.mUID = mUID;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public PostStuffForChatRoom(String Message, String UID, String User_name, String Date) {
        mMessage = Message;
        mUID = UID;
        mUserName = User_name;
        mDate = Date;
    }

    public PostStuffForChatRoom() {
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String Message) {
        this.mMessage = Message;
    }

    public String getUID() {
        return mUID;
    }

    public void setUID(String UID) {
        this.mUID = UID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUser_name(String UserName) {
        this.mUserName = UserName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = Date;
    }
}
