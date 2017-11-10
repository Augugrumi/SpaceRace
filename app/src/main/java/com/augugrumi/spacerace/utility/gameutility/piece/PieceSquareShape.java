package com.augugrumi.spacerace.utility.gameutility.piece;

/**
 * Created by davide on 10/11/17.
 */

public class PieceSquareShape implements PieceShape {

    private int l;

    public PieceSquareShape (int l) {

        if (l >= 0) {
            this.l = l;
        } else {
            this.l = 0;
        }

    }

    @Override
    public int getWidth() {
        return l;
    }

    @Override
    public int getLength() {
        return l;
    }
}
