package com.example.abcautovaluers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences userSession;
    private final SharedPreferences.Editor editor;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_SESSION_STATE = "state";

    public SessionManager(Context context){

        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void addMember(String username, Boolean state){

        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_SESSION_STATE, state);
        editor.commit();

    }

    public boolean checkSessionState(){

        return userSession.getBoolean(KEY_SESSION_STATE, false);

    }

    public void logoutUser(){

        editor.clear().commit();
    }

    public String getUsername(){

        return userSession.getString(KEY_USERNAME, null);

    }

}
