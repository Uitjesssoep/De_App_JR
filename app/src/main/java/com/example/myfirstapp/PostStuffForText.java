package com.example.myfirstapp;

public class PostStuffForText {

    private String postKey;
    private String postTitle;
    private String postUsername;


    public PostStuffForText(String postKey, String postTitle, String postUsername) {
        this.postKey = postKey;
        this.postTitle = postTitle;
        this.postUsername = postUsername;
    }

    public PostStuffForText() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }
}
