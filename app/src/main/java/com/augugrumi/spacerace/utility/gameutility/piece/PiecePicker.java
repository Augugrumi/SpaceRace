package com.augugrumi.spacerace.utility.gameutility.piece;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;
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

    @NonNull
    public static int pickRandomPieceResource () {

        return listOfPieces[(int)(Math.random() * (listOfPieces.length - 1))];
    }

    @NonNull
    public static BitmapDescriptor pickRandomPieceBitMap (@NonNull PieceShape shape) {

        Bitmap toScale = BitmapFactory.decodeResource(SpaceRace.getAppContext().getResources(), pickRandomPieceResource());
        toScale = Bitmap.createScaledBitmap(toScale, shape.getWidth(), shape.getLength(), false);

        return BitmapDescriptorFactory.fromBitmap(toScale);
    }
}
