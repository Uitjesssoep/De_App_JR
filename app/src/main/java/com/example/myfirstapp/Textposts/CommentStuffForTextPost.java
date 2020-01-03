package com.example.myfirstapp.Textposts;

public class CommentStuffForTextPost {

    private String Content;
    private String Date;
    private String User_name;
    private String Key;
    private String UID;
    private String OldKey;

    public CommentStuffForTextPost() {
    }

    public CommentStuffForTextPost(String content, String date, String user_name, String key, String uid, String oldKey) {
        Content = content;
        Date = date;
        User_name = user_name;
        Key = key;
        UID = uid;
        OldKey = oldKey;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String uid) {
        this.UID = uid;
    }

    public String getOldKey() {
        return OldKey;
    }

    public void setOldKey(String oldKey) {
        OldKey = oldKey;
    }
}
