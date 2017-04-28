package com.am.demo.taskapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by malbor806 on 28.04.2017.
 */

public class TaskAplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
