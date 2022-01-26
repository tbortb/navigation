package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.util.DistanceCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class UtilTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadCsvTests.class);

    /**
     * Test if the earth surface distance calculation gives results close to the ones drawn from google
     */
    @Test
    void distanceCalculationTest(){
        //Test values from http://www.movable-type.co.uk/scripts/latlong.html?from=49.243824,-121.887340&to=49.235347,-121.92532
        double lon1 = -121.887340;
        double lat1 = 49.243824;
        double lon2 = -121.92532;
        double lat2 = 49.235347;
        double expectedDistance = 2.914;
        double errorMarginPct = 0.01;

        double actualDistance = DistanceCalculator.getDistanceKM(lat1, lon1, lat2, lon2);

        Assertions.assertTrue(actualDistance < expectedDistance * (1 + errorMarginPct));
        Assertions.assertTrue(actualDistance > expectedDistance * (1 - errorMarginPct));

    }
}
