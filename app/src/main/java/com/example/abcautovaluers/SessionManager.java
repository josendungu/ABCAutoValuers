package com.example.abcautovaluers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences userSession;
    private SharedPreferences.Editor editor;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_SESSION_STATE = "state";

    public SessionManager(Context context){

        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void addMember(String email){

        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_SESSION_STATE, true);
        editor.commit();

    }

    public boolean checkSessionState(){

        return userSession.getBoolean(KEY_SESSION_STATE, false);

    }

    public void logoutUser(){

        editor.clear().commit();
    }

    public String getUserEmail(){

        return userSession.getString(KEY_EMAIL, null);

    }

}
