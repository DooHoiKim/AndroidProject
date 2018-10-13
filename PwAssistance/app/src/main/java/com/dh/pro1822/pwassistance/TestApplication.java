package com.dh.pro1822.pwassistance;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class TestApplication extends Application {

    public static final String PACKAGE_NAME = "com.dh.pro1822.pwassistance";

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
