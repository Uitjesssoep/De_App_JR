package com.example.myfirstapp.Chatroom;

public class PostStuffMakePrivateChat {

    private Boolean mSeen;
    private String mUID;
    private Long mDate;

    public PostStuffMakePrivateChat (Boolean seen, long Date, String UID) {
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
}
