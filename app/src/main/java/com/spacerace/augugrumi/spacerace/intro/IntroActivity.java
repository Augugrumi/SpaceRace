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

        // Slides creation
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getResources().getString(R.string.slide2Title));
        sliderPage2.setDescription(getResources().getString(R.string.slide2Subtitle));
        sliderPage2.setImageDrawable(R.drawable.treasurechest);
        sliderPage2.setBgColor(getResources().getColor(R.color.slide2BgDark));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle(getResources().getString(R.string.slide3Title));
        sliderPage3.setDescription(getResources().getString(R.string.slide3Subtitle));
        sliderPage3.setBgColor(getResources().getColor(R.color.slide3BgPlayGames));

        // Including slides...
        addSlide(new SlideOneFragment());
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        // Slider configuration
        showSeparator(false);
        showSkipButton(false);
        setProgressButtonEnabled(true);
        showStatusBar(false);
        setVibrate(false);
        setDepthAnimation();

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
