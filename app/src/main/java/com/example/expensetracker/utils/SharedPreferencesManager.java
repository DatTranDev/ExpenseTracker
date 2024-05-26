package com.example.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREFERENCES_NAME = "expensetracker_prefs";
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveObject(String key, Object data) {
        sharedPreferences.edit().putString(key, new Gson().toJson(data)).apply();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = sharedPreferences.getString(key, null);
        return json == null ? null : new Gson().fromJson(json, clazz);
    }

    public <T> void saveList(String key, List<T> list) {
        sharedPreferences.edit().putString(key, new Gson().toJson(list)).apply();
    }

    public <T> List<T> getList(String key, Type typeOfT) {
        String json = sharedPreferences.getString(key, null);
        return json == null ? null : new Gson().fromJson(json, typeOfT);
    }
    public void removeKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
