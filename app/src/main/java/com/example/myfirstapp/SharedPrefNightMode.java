package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefNightMode {

    SharedPreferences sharedPreferences;

    public SharedPrefNightMode(Context context){
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public Boolean loadNightModeState (){
        Boolean state = sharedPreferences.getBoolean("NightMode", false);
        return state;
    }

}
