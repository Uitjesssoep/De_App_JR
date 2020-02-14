package com.example.myfirstapp.Chatroom;

import android.media.Image;

public class PostStuffForChatRoom {
    private String mMessage, mType, mDate, mUID, mImageUrl;
    private Boolean mSeen;




    public PostStuffForChatRoom(String Message, String Type, Boolean Seen, String Date, String UID, String ImageURl) {
        mMessage = Message;
        mType = Type;
        mSeen = Seen;
        mDate = Date;
        mUID = UID;
        mImageUrl = ImageURl;
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

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
