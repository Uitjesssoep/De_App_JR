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

    public String getMessage()  {
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

    public String getmUserName() {
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
