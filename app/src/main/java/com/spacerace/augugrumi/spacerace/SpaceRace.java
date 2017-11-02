package com.spacerace.augugrumi.spacerace;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * @author Marco Zanella
 * @version 0.01
 * date 01/11/17
 */

public class MyApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("SHARED", "SI VOLA");

        instance = this;
    }

    public static Context getAppContext() {
        return instance;
    }
}