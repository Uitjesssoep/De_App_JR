package com.example.myfirstapp.Chatroom;

public class PostStuffForChatRoom {
    private String mMessage, mUID, mUserName, mDate;


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

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getUID() {
        return mUID;
    }

    public void setUID(String mUID) {
        this.mUID = mUID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
