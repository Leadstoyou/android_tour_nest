package com.example.tour_nest.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.tour_nest.model.User;
import com.google.gson.Gson;

public class SharedPrefHelper {
    private static final String PREF_NAME = "TourNestPrefs";
    private static final String KEY_USER = "user";

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String userJson = sharedPreferences.getString(KEY_USER, null);

        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public static boolean isLoggedIn(Context context) {
        return getUser(context) != null;
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }
}
