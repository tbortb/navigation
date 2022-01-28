package de.volkswagen.f73.evnavigator.util;

import com.sothawo.mapjfx.Coordinate;

import java.util.List;

/**
 * Utility class for several calculations around geolocal coordinates.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class GeoUtils {

    private static final int EARTH_RADIUS_KM = 6371;

    private GeoUtils() {
    }

    /**
     * Gets the linear distance in Km between two coordinates.
     * Calculation based on http://www.movable-type.co.uk/scripts/latlong.html?from=49.243824,-121.887340&to=49.235347,-121.92532
     *
     * @param startLat
     * @param startLon
     * @param endLat
     * @param endLon
     * @return
     */
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

    /**
     * Calculates the approximated cardinal direction of a given route via its origin and destination coordinates.
     *
     * @return array of exactly two directions, first latitudinal, second longitudinal
     */
    public static CardinalPoint[] calculateCardinalPoint(double startLat, double startLon,
                                                         double endLat, double endLon) {
        CardinalPoint[] directions = new CardinalPoint[2];

        directions[0] = startLat > endLat ? CardinalPoint.SOUTH : CardinalPoint.NORTH;
        directions[1] = startLon > endLon ? CardinalPoint.WEST : CardinalPoint.EAST;

        return directions;
    }

    public enum CardinalPoint {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
