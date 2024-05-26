package com.example.expensetracker.utils;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SharedPreferencesManager with the application context
        SharedPreferencesManager.getInstance(this);
    }
}
