package com.example.myfirstapp.Chatroom;

public class PostStuffForChat {

    private String mTitle;
    private String mUser_name;
    private String mUID;
    private String mKey;
    private String mDate;

    public PostStuffForChat(String Title, String User_name, String UID, String Key, String Date) {
        mTitle = Title;
        mUser_name = User_name;
        mUID = UID;
        mKey = Key;
        mDate = Date;
    }

    public PostStuffForChat() {
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public String getUser_name() {
        return mUser_name;
    }

    public void setUser_name(String User_name) {
        mUser_name = User_name;
    }

    public String getUID() {
        return mUID;
    }

    public void setUID(String UID) {
        this.mUID = UID;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String Key) {
        this.mKey = Key;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = Date;
    }

}
