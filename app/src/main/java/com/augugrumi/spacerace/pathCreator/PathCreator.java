package com.augugrumi.spacerace.pathCreator;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;
import com.augugrumi.spacerace.utility.CoordinatesUtility;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dpolonio on 15/11/17.
 */

public class PathCreator {

    private class DistanceFrom {

        private LatLng start;
        private LatLng end;
        private double distance;

        public DistanceFrom(@NonNull LatLng start, @NonNull LatLng end, double distance) {

            this.start = start;
            this.end = end;
            this.distance = distance;
        }
    }

    /**
     * Max distance in meters
     */
    private int maxDistance = 5000;
    /**
     * Min distance in meters
     */
    private int minDistance = 500;
    private LatLng initialPosition = null;

    public PathCreator(@NonNull LatLng initialPosition, int minDistance, int maxDistance) {

        this.initialPosition = initialPosition;

        if ((maxDistance >= 0 && minDistance >= 0) && maxDistance > minDistance) {
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }
    }

    public LatLng getInitialPosition () {
        return initialPosition;
    }

    public int getMaxDistance () {
        return maxDistance;
    }

    public int getMinDistance () {
        return minDistance;
    }

    private List<FutureTask<DistanceFrom>> calculateDistanceFromStart(List<LatLng> points) {

        final String toMatch = "legs=[{distance={text=";
        List<FutureTask<DistanceFrom>> res = new ArrayList<>();
        final ExecutorService threadManager = Executors.newCachedThreadPool();
        final String init = this.initialPosition.latitude + "," + this.initialPosition.longitude;

        for (final LatLng destination : points) {

            final String dest = destination.latitude + "," + destination.longitude;
            final Call<Object> path = new PathRetrival().getDirections(init, dest);

            FutureTask<DistanceFrom> task = new FutureTask<DistanceFrom>(new Callable<DistanceFrom>() {
                @Override
                public DistanceFrom call() throws Exception {
                    Response<Object> response = path.execute();
                    try {

                        Log.d("POS_FINDER", "ORIGINAL JSON: " + response.body().toString());

                        int pos = response.body().toString().indexOf(toMatch);
                        if (pos >= 0) {

                            pos += toMatch.length();

                            String semiSanitized = response.body().toString().substring(pos, pos + 10);
                            pos = semiSanitized.indexOf('m');
                            String sanitized = semiSanitized.substring(0, pos + 1);
                            sanitized = sanitized.trim();
                            sanitized = sanitized.replaceAll(" ", "");

                            double distance = -1;

                            if (sanitized.indexOf("km") != 0) {
                                Log.d("POS_FINDER", "DISTANCE IN KM");

                                distance = Double.parseDouble(sanitized.substring(0, sanitized.length() - 2));

                                Log.d("POS_FINDER", "Res in kilometers is: " + distance);
                                distance *= 1000; // We need the distance in meters
                            } else if (sanitized.indexOf('m') != 0) {
                                Log.d("POS_FINDER", "DISTANCE IN METERS");

                                distance = Double.parseDouble(sanitized.substring(0, sanitized.length() - 1));

                                Log.d("POS_FINDER", "Res in meters is: " + distance);
                            }

                            return new DistanceFrom(initialPosition, destination, distance);

                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                    return new DistanceFrom(initialPosition, destination, -1);
                }
            });
            threadManager.execute(task);
        }


        return res;
    }

    public Queue<LatLng> generatePath() {

        /* TODO write something able to find a suitable path for the gamers
        I'll just try to briefly explain the idea:
        The idea is to find nodes inside a certain range, in a way that the sum of all the ranges
        is less than maxDistance but greater than minDistance. In order to achieve that we need to
        have the distance (the path between two nodes) with Google Maps API
        (https://developers.google.com/maps/documentation/directions/intro#Waypoints) of the
        possible candidates for the next "hop".

        Another thought: we need to calculate at runtime only the distance from the current user
        position to the next node, since all the places are static and we can already write it down,
        saving computational time.
         */
        final double MAX_DISTANCE_FIRST_HOP = 0.700;
        final double MIN_DISTANCE_FIRST_HOP = 0.200;

        Resources r = SpaceRace.getAppContext().getResources();

        // FIXME I don't need the distance of all the points!
        List<LatLng> buildingsPositions = new ArrayList<>();
        for (LatLng position : PositionsLoader.getPositions()) {
            double distance = CoordinatesUtility.distance(
                    position.latitude,
                    position.longitude,
                    initialPosition.latitude,
                    initialPosition.longitude);

            if (distance < MAX_DISTANCE_FIRST_HOP && distance > MIN_DISTANCE_FIRST_HOP) {

                Log.d("POS_FINDER_MATCH", "ADDING CANDIDATE: " + position.toString());
                buildingsPositions.add(position);
            }
        }

        for (FutureTask<DistanceFrom> distanceFromFutureTask : calculateDistanceFromStart(buildingsPositions)) {

            try {
                DistanceFrom distance = distanceFromFutureTask.get();
                Log.d("POS_FINDER", distance.end + " " + distance.distance);

                // TODO finish the calculations

            } catch (InterruptedException | ExecutionException  e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
