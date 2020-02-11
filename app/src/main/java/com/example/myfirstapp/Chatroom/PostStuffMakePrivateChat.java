package com.example.myfirstapp.Chatroom;

public class PostStuffMakePrivateChat {

    private Boolean mSeen;
    private String mDate, mUID;

    public PostStuffMakePrivateChat (Boolean seen, String Date, String UID) {
        mSeen = seen;
        mDate = Date;
        mUID = UID;
    }

    public PostStuffMakePrivateChat() {
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
