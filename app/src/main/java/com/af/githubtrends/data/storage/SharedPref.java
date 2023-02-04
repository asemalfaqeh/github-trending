package com.af.githubtrends.data.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static final SharedPref sharePref = new SharedPref();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static final String FAV_ITEMS = "fav_items";

    private SharedPref() {}

    public static SharedPref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }

    public void saveString(String objStr, String key) {
        editor.putString(objStr,key);
        editor.apply();
    }

    public String getStringObj(String key) {
        return sharedPreferences.getString(key, "");
    }

}
