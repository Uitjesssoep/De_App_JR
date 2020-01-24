package com.example.myfirstapp.Textposts;

public class StuffForPost {

    private String mTitle;
    private String mUser_name;
    private String mContent;
    private String mUID;
    private String mKey;
    private String mDate;
    private String mType;


    public StuffForPost(String Title, String User_name, String Content, String UID, String Key, String Date, String Type) {
        mTitle = Title;
        mUser_name = User_name;
        mContent = Content;
        mUID = UID;
        mKey = Key;
        mDate = Date;
        mType = Type;
    }

    public StuffForPost() {
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = Date;
    }
}
