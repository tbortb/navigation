package de.volkswagen.f73.evnavigator;

import com.sothawo.mapjfx.Coordinate;
import de.volkswagen.f73.evnavigator.service.RouteService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
class RouteServiceTests {

    private static final double ORIGIN_LON = 13.542373;
    private static final double ORIGIN_LAT = 52.380071;
    private static final double DESTINATION_LON = 13.556786;
    private static final double DESTINATION_LAT = 52.341003;
    private static final double DESTINATION_LON_FAR = 8.788047;
    private static final double DESTINATION_LAT_FAR = 53.051855;

    @Autowired
    RouteService routeService;

    @Test
    void getRouteFromApiReturnsStatusCodeOk() throws JSONException {
        JSONObject result = routeService.getRouteFromCoordinates(ORIGIN_LAT, ORIGIN_LON, DESTINATION_LAT, DESTINATION_LON);
        String responseCode = result.getString("code");
        Assertions.assertEquals("Ok", responseCode);
    }

    @Test
    void longRouteJsonToCoordinatesReturnsManyWaypoints() {
        List<Coordinate> waypoints = routeService.getCoordinatesFromRoute(
                routeService.getRouteFromCoordinates(ORIGIN_LAT, ORIGIN_LON, DESTINATION_LAT_FAR, DESTINATION_LON_FAR)
        );

        Assertions.assertTrue(waypoints.size() > 1000);
    }

    @Test
    void longRouteJsonToDistanceReturnsCorrectDistance() {
        String distanceFull = routeService.getDistanceFromRoute(routeService.getRouteFromCoordinates(
                ORIGIN_LAT,
                ORIGIN_LON,
                DESTINATION_LAT_FAR,
                DESTINATION_LON_FAR)
                );

        String distanceTrimmed = distanceFull.replaceAll("[^\\d.]", "").replace(",", ".");

        double distance = Assertions.assertDoesNotThrow(() -> Double.parseDouble(distanceTrimmed));
        Assertions.assertTrue(distance > 250 && distance < 1000);
    }
}
