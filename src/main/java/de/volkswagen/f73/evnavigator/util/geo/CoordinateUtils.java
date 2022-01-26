package de.volkswagen.f73.evnavigator.util.geo;

import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

import java.text.DecimalFormat;

public class CoordinateUtils {

    private CoordinateUtils() {}

    public static String calculateDistance(double originLat, double originLon, double destLat, double destLon) {
        com.grum.geocalc.Coordinate originCoordLat = com.grum.geocalc.Coordinate.fromDegrees(originLat);
        com.grum.geocalc.Coordinate originCoordLon = com.grum.geocalc.Coordinate.fromDegrees(originLon);
        com.grum.geocalc.Coordinate destCoordLat = com.grum.geocalc.Coordinate.fromDegrees(destLat);
        com.grum.geocalc.Coordinate destCoordLon = com.grum.geocalc.Coordinate.fromDegrees(destLon);
        Point origin = Point.at(originCoordLat, originCoordLon);
        Point dest = Point.at(destCoordLat, destCoordLon);

        double distance = EarthCalc.gcd.distance(origin, dest);
        DecimalFormat formatter = new DecimalFormat("##.00");

        if (distance < 1000) {
            return String.format("%s meters", formatter.format(distance));
        } else {
            return String.format("%s kilometers", formatter.format(distance / 1000));
        }
    }
}
