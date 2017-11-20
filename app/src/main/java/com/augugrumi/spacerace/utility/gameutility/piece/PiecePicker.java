package com.augugrumi.spacerace.utility.gameutility.piece;

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

    private final static int[] LIST_OF_PIECES = {
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

    private final static int FINAL_GOAL = R.drawable.piece_direct_hit;
    private final static int START = R.drawable.piece_european_castle;
    private final static int MIDDLE_GOAL = R.drawable.piece_gem_stone;
    private final static int HINT = R.drawable.piece_scroll;

    public static int pickRandomPieceResource() {

        return LIST_OF_PIECES[(int)(Math.random() * (LIST_OF_PIECES.length - 1))];
    }

    @NonNull
    public static BitmapDescriptor getPiece(@NonNull PieceShape shape, int id) {

        Bitmap toScale = BitmapFactory.decodeResource(SpaceRace.getAppContext().getResources(), id);
        toScale = Bitmap.createScaledBitmap(toScale, shape.getWidth(), shape.getLength(), false);

        return BitmapDescriptorFactory.fromBitmap(toScale);
    }

    @NonNull
    public static BitmapDescriptor pickRandomPieceBitMap(@NonNull PieceShape shape) {

        return getPiece(shape, pickRandomPieceResource());
    }

    @NonNull
    public static BitmapDescriptor getFinalGoal(@NonNull PieceShape shape) {

        return getPiece(shape, FINAL_GOAL);
    }

    @NonNull
    public static BitmapDescriptor getStartGoal(@NonNull PieceShape shape) {

        return getPiece(shape, START);
    }

    @NonNull
    public static BitmapDescriptor getMiddleGoal(@NonNull PieceShape shape) {

        return getPiece(shape, MIDDLE_GOAL);
    }

    @NonNull
    public static BitmapDescriptor getHint(@NonNull PieceShape shape) {

        return getPiece(shape, HINT);
    }
}
