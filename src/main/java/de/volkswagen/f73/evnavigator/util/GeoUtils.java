package de.volkswagen.f73.evnavigator.util;

import com.sothawo.mapjfx.Coordinate;

import java.time.Duration;

/**
 * Utility class for several calculations around geolocal coordinates.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class GeoUtils {

    private static final double GERMANY_WEST_LONGITUDE = 5.866292;
    private static final double GERMANY_NORTH_LATITUDE = 55.095231;
    private static final double GERMANY_EAST_LONGITUDE = 15.041918;
    private static final double GERMANY_SOUTH_LATITUDE = 47.270113;
    private static final int EARTH_RADIUS_KM = 6371;

    private GeoUtils() {
    }

    /**
     * Gets the linear distance in Km between two coordinates.
     * Calculation based on <a href="http://www.movable-type.co.uk/scripts/latlong.html">movable-type.co.uk</a>
     *
     * @param startLat origin point latitude
     * @param startLon origin point longitude
     * @param endLat   destination point latitude
     * @param endLon   destination point longitude
     * @return distance in kilometers
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
     * Calculates the initial bearing in radians from an origin coordinate to a destination coordinate
     * Calculation based on <a href="http://www.movable-type.co.uk/scripts/latlong.html">movable-type.co.uk</a>
     *
     * @param origin origin coordinate
     * @param dest   destination coordinate
     * @return the initial bearing in radians
     */
    public static double bearingInRadians(Coordinate origin, Coordinate dest) {
        double srcLat = Math.toRadians(origin.getLatitude());
        double dstLat = Math.toRadians(dest.getLatitude());
        double dLng = Math.toRadians(dest.getLongitude() - origin.getLongitude());

        return Math.atan2(Math.sin(dLng) * Math.cos(dstLat),
                Math.cos(srcLat) * Math.sin(dstLat) -
                        Math.sin(srcLat) * Math.cos(dstLat) * Math.cos(dLng));
    }

    /**
     * Calculates the initial bearing in degrees from an origin coordinate to a destination coordinate
     * Calculation based on <a href="http://www.movable-type.co.uk/scripts/latlong.html">movable-type.co.uk</a>
     *
     * @param origin origin coordinate
     * @param dest   destination coordinate
     * @return the initial bearing in degrees
     */
    public static double bearingInDegrees(Coordinate origin, Coordinate dest) {
        return Math.toDegrees((bearingInRadians(origin, dest) + Math.PI) % Math.PI);
    }

    /**
     * Calculates the shortest linear distance of a point in relation to a line between two coordinates.
     * Calculation based on <a href="http://www.movable-type.co.uk/scripts/latlong.html">movable-type.co.uk</a>
     *
     * @param origin origin point of the line
     * @param dest   destination point of the line
     * @param point  coordinate to check the distance of
     * @return the distance from the point to the line in km
     */
    public static double crossTrackDistanceFromLinearPathInKm(Coordinate origin, Coordinate dest, Coordinate point) {
        double angularDistance = getLinearDistanceKm(origin.getLatitude(), origin.getLongitude(),
                point.getLatitude(), point.getLongitude()) / EARTH_RADIUS_KM;

        return Math.asin(Math.sin(angularDistance) * Math.sin(
                bearingInRadians(origin, point) - bearingInRadians(origin, dest))) * EARTH_RADIUS_KM;
    }

    /**
     * Checks whether two strings are valid coordinates when parsed as doubles
     *
     * @param lat latitude
     * @param lon longitude
     * @return validity
     */
    public static boolean isValidCoordinate(String lat, String lon) {

        double latitude;
        double longitude;
        try {
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        } catch (NumberFormatException ex) {
            return false;
        }

        return isValidCoordinate(latitude, longitude);
    }

    /**
     * Checks whether two doubles are geographically valid coordinates
     *
     * @param lat latitude
     * @param lon longitude
     * @return validity
     */
    public static boolean isValidCoordinate(double lat, double lon) {
        return (Math.abs(lat) <= 90) && (Math.abs(lon) <= 180);
    }

    /**
     * Checks whether a coordinate is approximately within the latitude and longitude boundaries of Germany
     *
     * @param lat latitude
     * @param lon longitude
     * @return validity
     */
    public static boolean isWithinGermany(double lat, double lon) {
        return !(lat > GERMANY_NORTH_LATITUDE || lat < GERMANY_SOUTH_LATITUDE
                || lon > GERMANY_EAST_LONGITUDE || lon < GERMANY_WEST_LONGITUDE);
    }

    /**
     * Returns three different durations in seconds for a distance in meters.
     * Baseline 111 kph, no motorways 88.8 kph, eco 61.05 kph
     *
     * @param meters distance in meters
     * @return three Duration values, first one fastest, second one without motorways and third one an eco route
     */
    public static Duration[] calculateRouteTimes(double meters) {
        Duration[] timeArr = new Duration[3];
        double kphToMpsFactor = 1000.0 / 3600.0;
        timeArr[0] = Duration.ofSeconds(Math.round(meters / (110.0 * kphToMpsFactor)));
        timeArr[1] = Duration.ofSeconds(Math.round(meters / (88.8 * kphToMpsFactor)));
        timeArr[2] = Duration.ofSeconds(Math.round(meters / (61.05 * kphToMpsFactor)));

        return timeArr;
    }
}
