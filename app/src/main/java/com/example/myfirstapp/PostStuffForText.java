package com.example.myfirstapp;

public class PostStuffForText {

    private String mTitle;
    private String mUser_name;
    private String mContent;
    private String mUID;
    private String mKey;

    public PostStuffForText(String Title, String User_name, String Content, String UID, String Key) {
        mTitle = Title;
        mUser_name = User_name;
        mContent = Content;
        mUID = UID;
        mKey = Key;
    }

    public PostStuffForText() {
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

    public String getContent() {
        return mContent;
    }

    public void setContent(String Content) {
        this.mContent = Content;
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
}
