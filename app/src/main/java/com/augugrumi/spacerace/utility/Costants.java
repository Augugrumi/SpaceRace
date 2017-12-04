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

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 02/12/17
 */

/**
 * Declare all costants that are for the confguration of the application and may change
 * in time
 */
public final class Costants {
    private Costants(){}

    //path calculation mode costants
    public static final String MODE_DEBUG = "DEBUG";
    public static final String MODE_RELEASE = "RELEASE";
    public static final String MODE = MODE_DEBUG;

    //path costants
    public final static double MAX_DISTANCE_FIRST_HOP = 2.5;
    public final static double MIN_DISTANCE_FIRST_HOP = 0.2;
    public static final int HOP_MIN_NUM = 2;

    //map displaying
    public static int DEFAULT_PIECE_DISPLAYED = R.drawable.ic_account_balance_black_48dp;
    public static final int ICON_DIMENSION = 90;

    //pieces
    public final static int[] LIST_OF_PIECES = {
            R.drawable.piece_alien_monster,
            R.drawable.piece_billed_cap,
            R.drawable.piece_extraterrestrial_alien,
            R.drawable.piece_flying_saucer,
            R.drawable.piece_mage,
            R.drawable.piece_male_astronaut,
            R.drawable.piece_male_singer,
            R.drawable.piece_moyai,
            R.drawable.piece_robot_face,
            R.drawable.piece_rocket,
            R.drawable.piece_satellite,
            R.drawable.piece_satellite_antenna,
            R.drawable.piece_sleuth_or_spy,
            R.drawable.piece_telescope,
            R.drawable.piece_unicorn_face
    };
    public final static int FINAL_GOAL = R.drawable.piece_direct_hit;
    public final static int START = R.drawable.piece_european_castle;
    public final static int MIDDLE_GOAL = R.drawable.piece_gem_stone;
    public final static int HINT = R.drawable.piece_scroll;
    public static final int PIECE_SIZE = 95;

    //maps api url
    public final static String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com";

    //credits
    public final static String FACEBOOK_URI = "https://fb.me/spaceracepsw";
    public final static String GITHUB_URI = "https://github.com/Augugrumi";

    //show on map distance
    public static final double KM_DISTANCE_MARKER = 0.20;
    public static final double KM_DISTANCE_HINT = 0.020;

    //multiplayer opponents number
    public static final int MIN_NUMBER_OF_PLAYERS = 1;
    public static final int MAX_NUMBER_OF_PLAYERS = 1;
}
