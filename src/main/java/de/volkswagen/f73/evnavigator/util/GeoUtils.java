package de.volkswagen.f73.evnavigator.util;

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
     * Calculation based on <a href="http://www.movable-type.co.uk/scripts/latlong.html?from=49.243824,-121.887340&to=49.235347,-121.92532">movable-type.co.uk</a>
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
}
