package com.shruti.lofo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TOKEN = "token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(int userId, String name, String email, String phone, String token) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public boolean isLoggedIn () {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void updateProfileSession(String newName, String newPhone){
        editor.putString(KEY_NAME, newName);
        editor.putString(KEY_PHONE, newPhone);
        editor.apply();
    }


    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
    public int getUserId() { return sharedPreferences.getInt(KEY_USER_ID, -1); }

    public String getName () {return sharedPreferences.getString(KEY_NAME, "CAMPUS USER");}

    public String getEmail () {return sharedPreferences.getString(KEY_EMAIL, "****@hcdc.edu.ph");}

    public String getPhone () {return sharedPreferences.getString(KEY_PHONE, "000-000-0000");}

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
