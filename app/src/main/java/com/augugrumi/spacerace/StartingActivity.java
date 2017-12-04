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
