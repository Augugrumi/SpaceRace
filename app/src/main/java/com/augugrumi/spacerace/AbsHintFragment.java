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

package com.augugrumi.spacerace;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by dpolonio on 30/11/17.
 */

public abstract class AbsHintFragment extends Fragment {
    protected MapActivity parent;
    protected LatLng actualPoi;
    protected LatLng nextPoi;
    protected ArrayList<ViewGroup> layouts;

    protected void showView(ViewGroup view) {
        for (ViewGroup v : layouts) {
            v.setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    public abstract void setHintData ();

    @Override
    public void onStart() {
        super.onStart();
        setHintData();
    }

    public void setPOI(LatLng actualPoi, LatLng nextPoi) {
        Log.d("SET_POI", actualPoi.toString());
        this.actualPoi = actualPoi;
        this.nextPoi = nextPoi;
    }
}
