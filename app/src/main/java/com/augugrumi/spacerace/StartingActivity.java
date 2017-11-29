package com.augugrumi.spacerace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.augugrumi.spacerace.intro.IntroActivity;
import com.augugrumi.spacerace.utility.LanguageManager;
import com.augugrumi.spacerace.utility.QuestionAnswerManager;
import com.augugrumi.spacerace.utility.SharedPreferencesManager;
import com.google.android.gms.maps.model.LatLng;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageManager.languageManagement(this);

        Intent intent;

        if (!SharedPreferencesManager.getFirstApplicationRun()) {

            Log.d("INTRO", "First run detected, launching sliders...");
            SharedPreferencesManager.setFirstApplicationRun(true);

            intent = new Intent(this, IntroActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(intent);
        finish();
    }

}
