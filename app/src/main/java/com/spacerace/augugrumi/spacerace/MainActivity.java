package com.spacerace.augugrumi.spacerace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.spacerace.augugrumi.spacerace.intro.IntroActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.spacerace.augugrumi", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            Log.d("INTRO", "First run detected, launching sliders...");

            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}
