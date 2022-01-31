package de.volkswagen.f73.evnavigator.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
class GeoUtilTests {

    /**
     * Test if the earth surface distance calculation gives results close to the ones drawn from google
     */
    @Test
    void distanceCalculationTestShouldReturnApproximateResult(){
        //Test values from http://www.movable-type.co.uk/scripts/latlong.html?from=49.243824,-121.887340&to=49.235347,-121.92532
        double lon1 = -121.887340;
        double lat1 = 49.243824;
        double lon2 = -121.92532;
        double lat2 = 49.235347;
        double expectedDistance = 2.914;
        double errorMarginPct = 0.01;

        double actualDistance = GeoUtils.getLinearDistanceKm(lat1, lon1, lat2, lon2);

        Assertions.assertTrue(actualDistance < expectedDistance * (1 + errorMarginPct));
        Assertions.assertTrue(actualDistance > expectedDistance * (1 - errorMarginPct));
    }

    /**
     * Tests if either an invalid decimal separator or impossible coordinates fail the check method
     */
    @Test
    void coordinateValidationShouldDetectWrongInput() {
        String wrongLat = "51,2382";
        String wrongLon = "9.82147";
        double wrongLat2 = 210.282853;
        double wrongLon2 = -192.2582719;

        Assertions.assertFalse(GeoUtils.isValidCoordinate(wrongLat, wrongLon));
        Assertions.assertFalse(GeoUtils.isValidCoordinate(wrongLat2, wrongLon2));
    }

    @Test
    void coordinateValidationShouldPassValidInput() {
        String lat = "52.421150";
        String lon = "10.744060";
        double lat2 = 52.421150;
        double lon2 = 10.744060;


        Assertions.assertTrue(GeoUtils.isValidCoordinate(lat, lon));
        Assertions.assertTrue(GeoUtils.isValidCoordinate(lat2, lon2));
    }
}
