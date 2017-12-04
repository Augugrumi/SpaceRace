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

package com.augugrumi.spacerace.pathCreator;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by dpolonio on 21/11/17.
 */

public class PathManager {

    private Deque<PathCreator.DistanceFrom> path;

    public PathManager(@NonNull Deque<PathCreator.DistanceFrom> path) {

        this.path = path;
    }

    public PathManager(JSONArray fromJson) {
        path = new ArrayDeque<>();
        try {
            for (int i = 0; i < fromJson.length(); i++) {

                JSONObject element = new JSONObject(fromJson.getString(i));

                JSONArray start = element.getJSONArray("start");
                JSONArray end = element.getJSONArray("end");

                path.add(new PathCreator.DistanceFrom(
                        new LatLng(
                                start.getDouble(0),
                                start.getDouble(1)
                        ),
                        new LatLng(
                                end.getDouble(0),
                                end.getDouble(1)
                        ),
                        element.getDouble("distance")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String toJson () {


        JSONArray array = new JSONArray();

        for (PathCreator.DistanceFrom f : path) {
            try {
                array.put(f.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array.toString();
    }

    public Deque<PathCreator.DistanceFrom> getPath() {

        return path;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }
}
