package de.volkswagen.f73.evnavigator.util;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class DistanceCalculator {
    //Formula from http://www.movable-type.co.uk/scripts/latlong.html?from=49.243824,-121.887340&to=49.235347,-121.92532
    private static final int EARTH_RADIUS_KM = 6371;

    private DistanceCalculator(){}

    public static double getLinearDistanceKm(double startLat, double startLon,
                                             double endLat, double endLon) {

        double diffLat = Math.toRadians((endLat - startLat));
        double diffLong = Math.toRadians((endLon - startLon));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(diffLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(diffLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);

    }
}
