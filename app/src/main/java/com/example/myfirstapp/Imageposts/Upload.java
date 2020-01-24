package com.example.myfirstapp.Imageposts;

public class Upload {

    private String mTitle;
    private String mUser_name;
    private String mContent;
    private String mUID;
    private String mKey;
    private String mDate;
    private String mType;

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public Upload(String Title, String User_name, String Content, String UID, String Key, String Date, String Type) {
        if (Title.trim().equals("")) {
            Title = " ";
        }
        mType = Type;
        mTitle = Title;
        mUser_name = User_name;
        mUID = UID;
        mKey = Key;
        mDate = Date;
        mContent = Content;
    }

    public Upload() {
//empty constructor nodig
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
