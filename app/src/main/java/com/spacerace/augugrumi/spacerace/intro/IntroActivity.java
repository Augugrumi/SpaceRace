package com.spacerace.augugrumi.spacerace.intro;

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

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getResources().getString(R.string.slide2Title));
        sliderPage2.setDescription(getResources().getString(R.string.slide2Subtitle));
        sliderPage2.setImageDrawable(R.drawable.treasurechest);
        sliderPage2.setBgColor(getResources().getColor(R.color.slide2Bg));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle(getResources().getString(R.string.slide3Title));
        sliderPage3.setDescription(getResources().getString(R.string.slide3Subtitle));
        sliderPage3.setBgColor(getResources().getColor(R.color.slide3Bg));

        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        showSeparator(false);
        showSkipButton(false);
        setProgressButtonEnabled(true);
        showStatusBar(false);
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
