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

package com.augugrumi.spacerace.utility.gameutility.piece;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import static com.augugrumi.spacerace.utility.Costants.FINAL_GOAL;
import static com.augugrumi.spacerace.utility.Costants.HINT;
import static com.augugrumi.spacerace.utility.Costants.LIST_OF_PIECES;
import static com.augugrumi.spacerace.utility.Costants.MIDDLE_GOAL;
import static com.augugrumi.spacerace.utility.Costants.START;

/**
 * Created by dpolonio on 09/11/17.
 */

public class PiecePicker {

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
