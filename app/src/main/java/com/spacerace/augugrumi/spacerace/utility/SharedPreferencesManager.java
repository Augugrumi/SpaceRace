package com.spacerace.augugrumi.spacerace.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.spacerace.augugrumi.spacerace.SpaceRace;


/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_FILE = "com.spacerace.augugrumi";
    private static final String FIRST_RUN = "first_application_run";

    public static boolean getFirstApplicationRun() {
        Log.i("SHARED_PREFERENCE_MAN", "app run");

        Context context = SpaceRace.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPref.getBoolean(FIRST_RUN, false);
    }

    public static void setFirstApplicationRun(boolean run) {
        Log.i("SHARED_PREFERENCE_MAN", "app run " + run);
        Context context = SpaceRace.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FIRST_RUN, run);
        editor.apply();
    }
}
