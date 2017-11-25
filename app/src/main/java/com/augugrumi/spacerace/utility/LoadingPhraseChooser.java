package com.augugrumi.spacerace.utility;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;

/**
 * Created by davide on 25/11/17.
 */

public class LoadingPhraseChooser {

    private static final int[] TIPS = {
            R.string.loading_tip_1
    };

    private static final int[] ACTIONS = {
            R.string.loading_action_1
    };

    static String pickRandomTip () {

        return SpaceRace
                .getAppContext()
                .getResources()
                .getString(TIPS[(int)(Math.random() * (TIPS.length - 1))]);
    }

    static String pickRandomAction () {

        return SpaceRace
                .getAppContext()
                .getResources()
                .getString(ACTIONS[(int)(Math.random() * (ACTIONS.length - 1))]);
    }
}
