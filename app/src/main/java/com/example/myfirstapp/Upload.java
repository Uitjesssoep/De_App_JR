package com.example.myfirstapp;

public class Upload {
    private String Name;
    private String ImageUrl;

    public Upload() {
//empty constructor nodig
    }

    public Upload(String name, String imageUrl) {
        if(name.trim().equals("")){
            name = "No Name";
        }

        Name= name;
        ImageUrl=imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name){
        Name=name;
    }

    public String getmImageUrl(){
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl=imageUrl;
    }
}
