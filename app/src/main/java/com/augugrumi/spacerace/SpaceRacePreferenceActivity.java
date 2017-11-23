package com.augugrumi.spacerace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.augugrumi.spacerace.utility.gameutility.BaseGameUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Locale;

public class SpaceRacePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit();
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
                    updateLanguage(SharedPreferencesManager.getLanguagePreference());
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
                button.setSummary(getString(R.string.sign_out_summary_button)+account.getDisplayName());
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
        if (!"".equals(selectedLanguage)) {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            SpaceRace.getAppContext().getResources().updateConfiguration(config,
                    SpaceRace.getAppContext().getResources().getDisplayMetrics());

        }
    }

}
