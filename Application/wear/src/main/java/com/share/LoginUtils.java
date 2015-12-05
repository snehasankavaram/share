package com.share;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.james.sharedclasses.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

/**
 * Created by james on 12/4/15.
 */
public class LoginUtils {
    private static final String PREFS_NAME = "login.prefs";
    private static final String LOGIN_KEY = "username";
    private static final String PROFILE_KEY = "profile";


    public static void setLoginToken(Context context, String token) {
        setToken(context, LOGIN_KEY, token);
    }

    public static String getLoginToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(LOGIN_KEY, null);
    }

    public static void setProfile(Context context, Profile profile) {
        Gson gson = new Gson();
        setToken(context, PROFILE_KEY, gson.toJson(profile));
    }

    public static Profile getProfile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String profileString =  prefs.getString(PROFILE_KEY, null);
        if (profileString != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            return gson.fromJson(parser.parse(profileString).getAsJsonObject(), Profile.class);
        }
        return null;
    }

    private static void setToken(Context context, String key, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, token);
        editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        return (getLoginToken(context) != null);
    }
}
