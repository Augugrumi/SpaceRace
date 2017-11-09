package com.augugrumi.spacerace.utility;

import android.location.Location;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 08/11/17
 */

public class CoordinatesUtility {

    private static final int EARTH_RADIUS = 6371;

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * @param l1 Location obj
     * @param l2 Location obj
     * @return Distance in Meters
     */
    public static double distance(Location l1, Location l2) {

        double latDistance = Math.toRadians(Math.abs(l1.getLatitude() - l2.getLatitude()));
        double lonDistance = Math.toRadians(Math.abs(l1.getLongitude() - l2.getLongitude()));
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(l1.getLatitude())) * Math.cos(Math.toRadians(l2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c * 1000; // convert to meters

        double height = Math.abs(l1.getAltitude() - l2.getAltitude());

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
