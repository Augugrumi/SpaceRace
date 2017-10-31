package com.spacerace.augugrumi.spacerace.intro;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.spacerace.augugrumi.spacerace.R;
import com.spacerace.augugrumi.spacerace.intro.slides.SlideOneFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // In order to add slides as fragments
        addSlide(new SlideOneFragment());

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Boh");
        sliderPage1.setDescription("Slide fatta col builder della libreria");
        sliderPage1.setBgColor(getResources().getColor(R.color.slide2Bg));
        addSlide(AppIntroFragment.newInstance(sliderPage1));


        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        showSkipButton(false);
        setProgressButtonEnabled(true);

        setVibrate(false);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
