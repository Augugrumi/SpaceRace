package com.augugrumi.spacerace.pathCreator;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Deque;

/**
 * Created by dpolonio on 21/11/17.
 */

public class PathManger {

    Deque<PathCreator.DistanceFrom> path;

    public PathManger(@NonNull Deque<PathCreator.DistanceFrom> path) {

        this.path = path;
    }

    public PathManger(JSONArray fromJson) {

        try {
            for (int i = 0; i < fromJson.length(); i++) {
                JSONObject element = fromJson.getJSONObject(i);

                JSONArray start = element.getJSONArray("start");
                JSONArray end = element.getJSONArray("end");

                new PathCreator.DistanceFrom(
                        new LatLng(
                                start.getDouble(0),
                                start.getDouble(1)
                        ),
                        new LatLng(
                                end.getDouble(0),
                                end.getDouble(1)
                        ),
                        element.getDouble("distance")
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String toJson () {


        JSONArray array = new JSONArray();

        for (PathCreator.DistanceFrom f : path) {
            array.put(f.toString());
        }

        return array.toString();
    }

    public Deque<PathCreator.DistanceFrom> getPath() {

        return path;
    }
}
