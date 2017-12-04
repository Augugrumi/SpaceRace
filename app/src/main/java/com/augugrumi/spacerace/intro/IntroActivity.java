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

package com.augugrumi.spacerace.intro;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.augugrumi.spacerace.MainActivity;
import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.intro.slides.SlideOneFragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Slides creation
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getResources().getString(R.string.slide2Title));
        sliderPage2.setDescription(getResources().getString(R.string.slide2Subtitle));
        sliderPage2.setImageDrawable(R.drawable.treasurechest);
        sliderPage2.setBgColor(getResources().getColor(R.color.slide2BgDark));

        // Slides creation
        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle(getResources().getString(R.string.slide3Title));
        sliderPage3.setDescription(getResources().getString(R.string.slide3Subtitle));
        sliderPage3.setImageDrawable(R.drawable.crossedswords);
        sliderPage3.setBgColor(getResources().getColor(R.color.slide3Bg));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle(getResources().getString(R.string.slide4Title));
        sliderPage4.setDescription(getResources().getString(R.string.slide4Subtitle));
        sliderPage4.setImageDrawable(R.drawable.shield_permissions);
        sliderPage4.setBgColor(getResources().getColor(R.color.slide4Bg));

        SliderPage sliderPage5 = new SliderPage();
        sliderPage5.setTitle(getResources().getString(R.string.slide5Title));
        sliderPage5.setDescription(getResources().getString(R.string.slide5Subtitle));
        sliderPage5.setImageDrawable(R.drawable.astronaut);
        sliderPage5.setBgColor(getResources().getColor(R.color.mainBg));


        // Including slides...
        addSlide(new SlideOneFragment());
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));
        addSlide(AppIntroFragment.newInstance(sliderPage4));
        addSlide(AppIntroFragment.newInstance(sliderPage5));

        // Slider configuration
        showSeparator(false);
        showSkipButton(false);
        setProgressButtonEnabled(true);
        showStatusBar(false);
        setVibrate(false);
        setDepthAnimation();
        setDoneText(getString(R.string.end_intro));

        // Permissions
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
