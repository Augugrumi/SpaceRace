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
