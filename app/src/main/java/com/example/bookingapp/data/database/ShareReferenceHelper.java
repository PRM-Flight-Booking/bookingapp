package com.example.bookingapp.data.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ShareReferenceHelper {
    private static final String PREF_NAME = "MyPrefs";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    public ShareReferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void save(String tag, Object object) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(object);
        editor.putString(tag, json);
        editor.apply();
    }
    public <T> T get(String tag, Class<T> clazz) {
        String json = sharedPreferences.getString(tag, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }

    public void remove(String tag) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(tag);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
