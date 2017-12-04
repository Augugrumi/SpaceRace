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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.augugrumi.spacerace.utility.LanguageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.championswimmer.libsocialbuttons.fabs.FABFacebook;

import static com.augugrumi.spacerace.utility.Costants.FACEBOOK_URI;
import static com.augugrumi.spacerace.utility.Costants.GITHUB_URI;

public class CreditsActivity extends AppCompatActivity {
    @BindView(R.id.facebook)
    FABFacebook fabFacebook;

    @BindView(R.id.github)
    ImageButton fabGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        ButterKnife.bind(this);

        LanguageManager.languageManagement(this);
    }

    @OnClick(R.id.facebook)
    public void goOnFacebookPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URI));
        startActivity(browserIntent);
    }

    @OnClick(R.id.github)
    public void goOnGithubPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URI));
        startActivity(browserIntent);
    }
}
