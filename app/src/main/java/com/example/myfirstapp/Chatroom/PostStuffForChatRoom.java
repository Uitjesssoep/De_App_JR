package com.example.myfirstapp.Chatroom;

public class PostStuffForChatRoom {
    private String mMessage, mType, mDate, mUID;
    private Boolean mSeen;


    public PostStuffForChatRoom(String Message, String Type, Boolean Seen, String Date, String UID) {
        mMessage = Message;
        mType = Type;
        mSeen = Seen;
        mDate = Date;
        mUID = UID;
    }

    public PostStuffForChatRoom() {
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public Boolean getmSeen() {
        return mSeen;
    }

    public void setmSeen(Boolean mSeen) {
        this.mSeen = mSeen;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String mUID) {
        this.mUID = mUID;
    }
}
