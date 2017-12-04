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

package com.augugrumi.spacerace;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Locale;

public class SpaceRacePreferenceActivity extends PreferenceActivity {

    private static SpaceRacePreferenceActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit();
        instance = this;
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final ListPreference list = (ListPreference) findPreference("languageKey");
            list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    updateLanguage((String)newValue);
                    instance.recreate();
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();

            final Preference button = findPreference("signInOut");
            final GoogleSignInAccount account =
                    GoogleSignIn.getLastSignedInAccount(SpaceRace.getAppContext());
            final GoogleSignInClient mGoogleSignInClient =
                    GoogleSignIn.getClient(SpaceRace.getAppContext(),
                            GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

            if (account != null) {
                button.setTitle(R.string.sign_out_button);
                button.setSummary(R.string.sign_out_summary_button);
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mGoogleSignInClient.signOut();
                        MyPreferenceFragment.this.onResume();
                        return true;
                    }
                });
            } else {
                button.setTitle(R.string.sign_in_button);
                button.setSummary(getString(R.string.sign_in_summary_button));
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivityForResult(mGoogleSignInClient.getSignInIntent(), 1002);
                        MyPreferenceFragment.this.onResume();
                        return true;
                    }
                });
            }
        }
    }

    public static void updateLanguage(String selectedLanguage) {
        if (!"".equals(selectedLanguage) &&
                !selectedLanguage.equals(SharedPreferencesManager.getLanguagePreference())) {
            SharedPreferencesManager.setLanguagePreference(selectedLanguage);
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            SpaceRace.getAppContext().getResources().updateConfiguration(config,
                    SpaceRace.getAppContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
