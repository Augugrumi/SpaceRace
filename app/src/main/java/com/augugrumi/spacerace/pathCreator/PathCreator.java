package com.augugrumi.spacerace.pathCreator;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dpolonio on 15/11/17.
 */

public class PathCreator {

    /**
     * Max distance in meters
     */
    private int maxDistance = 5000;
    /**
     * Min distance in meters
     */
    private int minDistance = 500;
    private LatLng initialPosition = null;

    public PathCreator(@NonNull LatLng initialPosition, int maxDistance, int minDistance) {

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

    public LatLng[] generatePath() {

        /* TODO write something able to find a suitable path for the gamers
        I'll just try to briefly explain the idea:
        The idea is to find nodes inside a certain range, in a way that the sum of all the ranges
        is less than maxDistance but greater than minDistance. In order to achieve that we need to
        have the distance (the path between two nodes) with Google Maps API
        (https://developers.google.com/maps/documentation/directions/intro#Waypoints) of the
        possible candidates for the next "hop".
         */

        return null;
    }
}
