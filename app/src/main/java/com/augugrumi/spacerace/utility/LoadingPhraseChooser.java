package com.augugrumi.spacerace.utility;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;

import java.util.Random;

/**
 * Created by davide on 25/11/17.
 */

public class LoadingPhraseChooser {

    private static final Random RANDOM = new Random();

    private static final int[] TIPS = {
            R.string.loading_tip_1
    };

    private static final int[] ACTIONS = {
            R.string.loading_action_1
    };

    private static final int[] BACKGROUNDS = {
            R.drawable.loading_screen_pic_1,
            R.drawable.loading_screen_pic_2,
            R.drawable.loading_screen_pic_3
    };

    static String pickRandomTip () {

        return SpaceRace
                .getAppContext()
                .getResources()
                .getString(TIPS[TIPS.length <= 1? 0 : RANDOM.nextInt(TIPS.length -1)]);
    }

    static String pickRandomAction () {

        return SpaceRace
                .getAppContext()
                .getResources()
                .getString(ACTIONS[ACTIONS.length <= 1? 0 : RANDOM.nextInt(ACTIONS.length -1)]);
    }

    static int pickRandomBackground () {

        return BACKGROUNDS[BACKGROUNDS.length <= 1? 0 : RANDOM.nextInt(BACKGROUNDS.length -1)];
    }
}
