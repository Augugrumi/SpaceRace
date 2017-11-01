package com.spacerace.augugrumi.spacerace;

import android.app.Application;
import android.content.Context;

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
        instance = this;
    }

    public static Context getAppContext() {
        return instance;
    }
}