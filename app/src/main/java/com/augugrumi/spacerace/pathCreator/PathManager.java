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
