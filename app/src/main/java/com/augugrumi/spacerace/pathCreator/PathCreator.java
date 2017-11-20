package com.augugrumi.spacerace.pathCreator;

import android.support.annotation.NonNull;
import android.util.Log;

import com.augugrumi.spacerace.utility.CoordinatesUtility;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final static double MAX_DISTANCE_FIRST_HOP = 0.700;
    private final static double MIN_DISTANCE_FIRST_HOP = 0.200;

    public class DistanceFrom {

        private LatLng start;
        private LatLng end;
        private double distance;

        DistanceFrom(@NonNull LatLng start, @NonNull LatLng end, double distance) {

            this.start = start;
            this.end = end;
            this.distance = distance;
        }

        public LatLng getStart() {
            return start;
        }
        public LatLng getEnd() {
            return end;
        }
        public double getDistance() {
            return distance;
        }
    }

    /**
     * Max distance in meters
     */
    private double maxDistance = 5;
    /**
     * Min distance in meters
     */
    private double minDistance = 0.5;
    private LatLng initialPosition = null;

    public PathCreator(@NonNull LatLng initialPosition, double minDistance, double maxDistance) {

        this.initialPosition = initialPosition;

        if ((maxDistance >= 0 && minDistance >= 0) && maxDistance > minDistance) {
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }
    }

    public LatLng getInitialPosition() {
        return initialPosition;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public double getMinDistance() {
        return minDistance;
    }

    private List<FutureTask<DistanceFrom>> calculateDistanceFromStart(@NonNull final LatLng start, @NonNull List<LatLng> points) {

        final String toMatch = "legs=[{distance={text=";
        List<FutureTask<DistanceFrom>> res = new ArrayList<>();
        final ExecutorService threadManager = Executors.newCachedThreadPool();
        final String init = start.latitude + "," + start.longitude;

        for (final LatLng destination : points) {

            final String dest = destination.latitude + "," + destination.longitude;
            final Call<Object> path = new PathRetrieval().getDirections(init, dest);

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

                            return new DistanceFrom(start, destination, distance);

                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                    return new DistanceFrom(start, destination, -1);
                }
            });
            res.add(task);
            threadManager.execute(task);
        }


        return res;
    }

    public void getDistanceBetweenPoints() {

        for (LatLng init : PositionsLoader.getPositions()) {

            for (FutureTask<DistanceFrom> f : calculateDistanceFromStart(
                    init,
                    PositionsLoader.getPositions()
            )) {
                try {
                    DistanceFrom df = f.get();

                    Log.d("DISTANCE_FROM", df.start + " " + df.end + ". Distance: " + df.distance + " meters");
                } catch (InterruptedException | ExecutionException  e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Deque<DistanceFrom>> generatePaths() {

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

        List<Deque<DistanceFrom>> res = new ArrayList<>();
        List<FutureTask<DistanceFrom>> effectiveDistanceFromStart = calculateDistanceFromStart(
                initialPosition,
                getInRange(
                        initialPosition,
                        MIN_DISTANCE_FIRST_HOP,
                        MAX_DISTANCE_FIRST_HOP
                ));

        Collections.shuffle(effectiveDistanceFromStart); // Randomizing the points...

        for (FutureTask<DistanceFrom> distanceFromFutureTask : effectiveDistanceFromStart) {

            Deque<DistanceFrom> path = new ArrayDeque<>();
            try {
                DistanceFrom distance = distanceFromFutureTask.get();
                Map<LatLng, Boolean> visitedTable = new HashMap<>();

                visitedTable.put(distance.end, true);

                path.addLast(distance);
                path.addAll(pathChooser(distance, maxDistance - (distance.distance/1000), visitedTable));

                res.add(path);

            } catch (InterruptedException | ExecutionException  e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private List<LatLng> getInRange(@NonNull LatLng pos, double min, double max) {
        List<LatLng> buildingsPositions = new ArrayList<>();
        for (LatLng position : PositionsLoader.getPositions()) {
            double distance = CoordinatesUtility.distance(
                    position.latitude,
                    position.longitude,
                    pos.latitude,
                    pos.longitude);

            Log.d("DISTANCE", "distance between " + position.toString() + " and " + pos.toString() + "= " + distance);

            if (distance <= max && distance >= min) {

                Log.d("POS_FINDER_MATCH", "ADDING CANDIDATE: " + position.toString());
                buildingsPositions.add(position);
            }
        }

        return buildingsPositions;
    }

    private Deque<DistanceFrom> pathChooser(@NonNull DistanceFrom d,
                                            double remainingDistance,
                                            @NonNull Map<LatLng, Boolean> visitedTable)
            throws ExecutionException, InterruptedException {

        /* FIXME I need to hardcode all the distances between nodes!
        There is a problem tho: it's not easy to calculate all the distances without having the
        application crashing...
         */
        Deque<DistanceFrom> res = new ArrayDeque<>();

        if (remainingDistance >= minDistance) {

            List<FutureTask<DistanceFrom>> effectiveDistance = calculateDistanceFromStart(
                    d.end,
                    getInRange(d.end, MIN_DISTANCE_FIRST_HOP, MAX_DISTANCE_FIRST_HOP)
            );

            Collections.shuffle(effectiveDistance); // Randomizing the points...

            Log.d("PATH_CHOOSER", "effectiveDistance size: " + effectiveDistance.size());

            for (FutureTask<DistanceFrom> d2 : effectiveDistance) {

                DistanceFrom calculation = d2.get();

                if (calculation.start == d.end &&
                        calculation.start != d.start &&
                        calculation.distance >= 0 &&
                        (calculation.distance/1000) <= remainingDistance &&
                        visitedTable.get(calculation.end) == null) {

                    Log.d("PATH_CHOOSER", "adding new node into the list");

                    visitedTable.put(calculation.end, true);

                    res.addLast(calculation);
                    res.addAll(pathChooser(calculation,
                            remainingDistance - (calculation.distance/1000),
                            visitedTable));
                    return res;
                }
            }
        }

        return res;
    }
}
