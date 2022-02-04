package de.volkswagen.f73.evnavigator.service;

import com.sothawo.mapjfx.Coordinate;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RouteServiceTests {

    private static final double ORIGIN_LON = 13.542373;
    private static final double ORIGIN_LAT = 52.380071;
    private static final double DESTINATION_LON = 13.556786;
    private static final double DESTINATION_LAT = 52.341003;
    private static final double DESTINATION_LON_FAR = 8.788047;
    private static final double DESTINATION_LAT_FAR = 53.051855;

    @Autowired
    RouteService routeService;

    /**
     * Tests whether calling OSRM API returns a response that signals a correct request.
     */
    @Test
    void getRouteFromApiReturnsStatusCodeOkTest() {
        JSONObject result = this.routeService.getRouteFromCoordinates(ORIGIN_LAT, ORIGIN_LON,
                DESTINATION_LAT, DESTINATION_LON);
        String responseCode = Assertions.assertDoesNotThrow(() -> result.getString("code"));
        Assertions.assertEquals("Ok", responseCode);
    }

    /**
     * Tests whether calling OSRM API with two coordinates far from each other returns a plausible response with many
     * waypoints.
     */
    @Test
    void longRouteJsonToCoordinatesReturnsManyWaypointsTest() {
        List<Coordinate> waypoints = this.routeService.getCoordinatesFromRoute(
                this.routeService.getRouteFromCoordinates(ORIGIN_LAT, ORIGIN_LON,
                        DESTINATION_LAT_FAR, DESTINATION_LON_FAR));

        Assertions.assertTrue(waypoints.size() > 1000);
    }

    /**
     * Tests whether calling OSRM API with two coordinates far from each other returns a response with a plausible
     * distance.
     */
    @Test
    void longRouteJsonToDistanceReturnsCorrectDistanceTest() {
        double distance = this.routeService.getDistanceFromRoute(this.routeService.getRouteFromCoordinates(
                ORIGIN_LAT,
                ORIGIN_LON,
                DESTINATION_LAT_FAR,
                DESTINATION_LON_FAR)
        ) / 1000;
        Assertions.assertTrue(distance > 250 && distance < 500);
    }
}
