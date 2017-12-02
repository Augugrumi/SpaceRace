package com.augugrumi.spacerace.utility;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

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

        double lat1 = l1.getLatitude(), lat2 = l2.getLatitude(),
               lng1 = l1.getLongitude(), lng2 = l2.getLongitude();

        return distance(lat1, lng1, lat2, lng2);
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return EARTH_RADIUS * c;
    }

    public static double get2DDistanceInKm(LatLng l1, LatLng l2) {

        // instantiate the calculator
        GeodeticCalculator geoCalc = new GeodeticCalculator();

        // select a reference elllipsoid
        Ellipsoid reference = Ellipsoid.WGS84;

        // set l1 coordinates
        GlobalCoordinates l1cord;
        l1cord = new GlobalCoordinates(l1.latitude, l1.longitude);

        // set l2 coordinates
        GlobalCoordinates l2cord;
        l2cord = new GlobalCoordinates(l2.latitude, l2.longitude);

        // calculate the geodetic curve
        GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(reference, l1cord, l2cord);
        double ellipseKilometers = geoCurve.getEllipsoidalDistance() / 1000.0;

        System.out.printf("Ellipsoidal Distance: %1.2f kilometers\n", ellipseKilometers);

        return ellipseKilometers;
    }
}
