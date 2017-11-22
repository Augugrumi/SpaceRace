package com.augugrumi.spacerace.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;


/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_FILE = "com.augugrumi";
    private static final String FIRST_RUN = "first_application_run";
    private static final int DEFAULT_MAP_ID = R.raw.dark_side_of_the_moon;
    private static final String MAP_STYLE_KEY = "mapStyleKey";


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

    public static int getMapStyle() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(SpaceRace.getAppContext());
        String styleRes = sharedPref.getString(MAP_STYLE_KEY, "NA");
        int style;
        switch (styleRes) {
            case "res/raw/mars.json":
                style = R.raw.mars;
                break;
            case "res/raw/dark_side_of_the_moon.json":
                style = R.raw.dark_side_of_the_moon;
                break;
            case "res/raw/light_side_of_the_moon.json":
                style = R.raw.light_side_of_the_moon;
                break;
            case "res/raw/uranus.json":
                style = R.raw.uranus;
                break;
            default:
                style = DEFAULT_MAP_ID;
                break;
        }
        return style;
    }
}
