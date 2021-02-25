package com.example.abcautovaluers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences userSession;
    private final SharedPreferences.Editor editor;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_LOGGED_STATE = "logged_state";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IS_ADMIN = "is_admin";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SESSION_STATE = "state";

    public SessionManager(Context context){

        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void addMember(User user, Boolean state){

        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putBoolean(KEY_IS_ADMIN, user.getAdmin());
        editor.putBoolean(KEY_SESSION_STATE, state);
        editor.commit();

    }

    public User getUserDetails(){

        String username = userSession.getString(KEY_USERNAME, null);
        String password = userSession.getString(KEY_PASSWORD, null);
        String email = userSession.getString(KEY_EMAIL, null);
        Boolean isAdmin = userSession.getBoolean(KEY_IS_ADMIN, false);

        return new User(username, password, email, isAdmin);

    }

    public boolean checkSessionState(){

        return userSession.getBoolean(KEY_SESSION_STATE, false);

    }

    public void setLoggedState(Boolean state){

        editor.putBoolean(KEY_LOGGED_STATE, state);
        editor.commit();

    }

    public void logoutUser(){

        editor.clear().commit();
    }

    public boolean checkLoginState(){

        return userSession.getBoolean(KEY_LOGGED_STATE, false);
    }

    public String getUsername(){

        return userSession.getString(KEY_USERNAME, null);

    }

}
