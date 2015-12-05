package com.example.james.sharedclasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by james on 12/4/15.
 */
public class LoginUtils {
    private static final String PREFS_NAME = "login.prefs";
    private static final String LOGIN_KEY = "username";
    private static final String PROFILE_KEY = "profile";
    private static final String CONTACTS_KEY = "contacts";


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

    public static void setContacts(Context context, ArrayList<Contact> contacts) {
        Gson gson = new Gson();
        setToken(context, CONTACTS_KEY, gson.toJson(contacts));
    }

    public static ArrayList<Contact> getContacts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String stringContacts = prefs.getString(CONTACTS_KEY, null);
        if (stringContacts != null) {
            Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(stringContacts, type);
        }
        return new ArrayList<>();
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
