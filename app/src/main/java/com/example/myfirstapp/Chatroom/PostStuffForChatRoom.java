package com.example.myfirstapp.Chatroom;

public class PostStuffForChatRoom {
    private String mMessage, mType, mUID, mImageUrl;
    private Boolean mSeen;
    private long mDate;




    public PostStuffForChatRoom(String Message, String Type, Boolean Seen, long Date, String UID, String ImageURl) {
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

    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
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
