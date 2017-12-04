/**
* Copyright 2017 Davide Polonio <poloniodavide@gmail.com>, Federico Tavella
* <fede.fox16@gmail.com> and Marco Zanella <zanna0150@gmail.com>
* 
* This file is part of SpaceRace.
* 
* SpaceRace is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SpaceRace is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with SpaceRace.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.augugrumi.spacerace.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Space;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;

import java.util.Locale;


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
    private static final String DEFAULT_LANGUAGE = "NULL";
    private static final String LANGUAGE_KEY = "languageKey";


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

    public static String getLanguagePreference() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(SpaceRace.getAppContext());

        String lang = sharedPref.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE);

        if (lang.equals(DEFAULT_LANGUAGE)) {
            Locale current = SpaceRace.getAppContext().getResources()
                    .getConfiguration().locale;
            lang = current.getLanguage();
            setLanguagePreference(lang);
        }

        return lang;
    }

    public static void setLanguagePreference(String language) {
        Context context = SpaceRace.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LANGUAGE_KEY, language);
        editor.apply();
    }

}
