package com.augugrumi.spacerace.utility;

import android.content.Context;
import android.content.res.Configuration;

import com.augugrumi.spacerace.SpaceRace;

import java.util.Locale;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 26/11/17
 */

public class LanguageManager {

    private LanguageManager(){}

    public static void languageManagement(Context context) {
        String languageToLoad = SharedPreferencesManager.getLanguagePreference();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

}
