package com.augugrumi.spacerace.utility.gameutility;

import com.augugrumi.spacerace.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by dpolonio on 09/11/17.
 */

public class PiecePicker {

    private static int[] listOfPieces = {
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

    public static int pickRandomPieceResource () {

        return listOfPieces[(int)(Math.random() * (listOfPieces.length - 1))];
    }

    public static BitmapDescriptor pickRandomPieceBitMap () {

        return BitmapDescriptorFactory.fromResource(pickRandomPieceResource());
    }
}
