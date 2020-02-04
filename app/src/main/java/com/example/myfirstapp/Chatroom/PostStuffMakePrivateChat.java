package com.example.myfirstapp.Chatroom;

public class PostStuffMakePrivateChat {

    private String mUser_name1;
    private String mUID1;
    private String mUID2;
    private String mKey;
    private String mDate;
    private String mUser_name2;

    public PostStuffMakePrivateChat (String User_name_1, String User_name_2, String UID1, String UID2, String Key, String Date) {
        mUser_name1 = User_name_1;
        mUser_name2 = User_name_2;
        mUID1 = UID1;
        mUID2 = UID2;
        mKey = Key;
        mDate = Date;
    }

    public PostStuffMakePrivateChat() {
    }

    public String getUser_name1() {
        return mUser_name1;
    }

    public void setUser_name1(String mUser_name1) {
        this.mUser_name1 = mUser_name1;
    }

    public String getUID1() {
        return mUID1;
    }

    public void setUID1(String mUID1) {
        this.mUID1 = mUID1;
    }

    public String getUID2() {
        return mUID2;
    }

    public void setUID2(String mUID2) {
        this.mUID2 = mUID2;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getUser_name2() {
        return mUser_name2;
    }

    public void setUser_name2(String mUser_name2) {
        this.mUser_name2 = mUser_name2;
    }
}
