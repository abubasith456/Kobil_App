package com.example.kobilapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static SharedPreference instance;

    public static SharedPreference getInstance() {
        if (instance == null) {
            instance = new SharedPreference();
        }
        return instance;
    }

    public void saveInt(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("AppFieldValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
