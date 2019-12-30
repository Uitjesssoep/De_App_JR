package com.example.myfirstapp;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class Upload {
    private String mTitle;
    private String mUser_name;
    private String mImageUrl;
    private String mUID;
    private String mKey;
    private String mDate;


    public Upload() {
//empty constructor nodig
    }

    public Upload(String Title, String User_name, String imageUrl, String UID, String Key, String Date) {
        if (Title.trim().equals("")) {
            Title = "No Name";
        }
        mTitle = Title;
        mUser_name = User_name;
        mUID = UID;
        mKey = Key;
        mDate = Date;
        mImageUrl = imageUrl;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
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
