package com.example.myfirstapp.Textposts;

public class CommentStuffForTextPostMyProf {

    private String Key;
    private String UID;
    private String OldKey;

    public CommentStuffForTextPostMyProf() {
    }

    public CommentStuffForTextPostMyProf(String key, String uid, String oldKey) {
        Key = key;
        UID = uid;
        OldKey = oldKey;
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
