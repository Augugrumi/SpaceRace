package com.augugrumi.spacerace.utility.gameutility.piece;

/**
 * Created by davide on 10/11/17.
 */

public class PieceRectangularShape implements PieceShape {

    private int length;
    private int width;

    public PieceRectangularShape(int length, int width) {

        if (length >= 0) {
            this.length = length;
        } else {
            this.length = 0;
        }

        if (width >= 0) {
            this.width = width;
        } else {
            this.width = 0;
        }
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getLength() {
        return length;
    }
}
