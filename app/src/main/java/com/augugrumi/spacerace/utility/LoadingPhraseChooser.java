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
            R.drawable.loading_screen_pic_3,
            R.drawable.loading_screen_pic_4
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
