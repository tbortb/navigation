package de.volkswagen.f73.evnavigator.service;


import com.sothawo.mapjfx.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class RouteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteService.class);
    private static final String OSRM_URL = "https://router.project-osrm.org/route/v1/car/%s?geometries=geojson&overview=full&alternatives=false&skip_waypoints=true";

    public JSONObject getRouteFromCoordinates(double originLat, double originLon, double destLat, double destLon) {
        String param = String.format(Locale.US, "%f,%f;%f,%f", originLon, originLat, destLon, destLat);
        String url = String.format(OSRM_URL, param);

        LOGGER.debug("Fetching route from {}", url);

        JSONObject result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            result = new JSONObject(response);
        } catch (Exception e){
            LOGGER.error("Error parsing OSRM API: {}", e.getMessage());
        }
        return result;
    }

    public List<Coordinate> getCoordinatesFromRoute(JSONObject json) {

        List<Coordinate> coordinateList = new ArrayList<>();

        LOGGER.debug("Parsing coordinates from JSON...");
        try {
            if (!json.getString("code").equals("Ok")) {
                LOGGER.error("OSRM API Error: {}", json.getString("code"));
                return Collections.emptyList();
            }

            JSONArray routes = json.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject geometry = route.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            for(int i = 0 ; i < coordinates.length(); i++) {
                JSONArray coordinatePair = coordinates.getJSONArray(i);

                double lon = coordinatePair.getDouble(0);
                double lat = coordinatePair.getDouble(1);
                coordinateList.add(new Coordinate(lat, lon));
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing route from JSON: {}", e.getMessage());
            return Collections.emptyList();
        }

        return coordinateList;
    }

    public String getDistanceFromRoute(JSONObject json) {

        LOGGER.debug("Parsing distance from JSON...");

        try {
            if (!json.getString("code").equals("Ok")) {
                LOGGER.error("OSRM API Error: {}", json.getString("code"));
                return "";
            }
            JSONArray routes = json.getJSONArray("routes");
            JSONObject routeObject = routes.getJSONObject(0);
            JSONArray legs = routeObject.getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            double distance = leg.getDouble("distance");
            DecimalFormat formatter = new DecimalFormat("##.00");
            if (distance < 1000) {
                return String.format("%s meters", formatter.format(distance));
            } else {
                return String.format("%s kilometers", formatter.format(distance / 1000));
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing distance from JSON: {}", e.getMessage());
            return "";
        }
    }
}