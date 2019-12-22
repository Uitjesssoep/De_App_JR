package com.example.myfirstapp;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mMyUID;
    private String mUsername;
    private String mtemp_key;


    public Upload() {
//empty constructor nodig
    }

    public Upload(String name, String imageUrl, String MyUID, String Username, String temp_key) {
        if(name.trim().equals("")){
            name = "No Name";
        }

        mName= name;
        mImageUrl=imageUrl;
        mMyUID=MyUID;
        mUsername=Username;
        mtemp_key=temp_key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name){
        mName=name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getMyUID(){
        return mMyUID;
    }

    public String getUsername(){
        return mUsername;
    }

    private String gettemp_key(){
        return mtemp_key;
    }
}
