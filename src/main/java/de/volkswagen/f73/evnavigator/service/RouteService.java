package de.volkswagen.f73.evnavigator.service;


import com.sothawo.mapjfx.Coordinate;
import de.volkswagen.f73.evnavigator.model.Route;
import de.volkswagen.f73.evnavigator.repository.RouteRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static de.volkswagen.f73.evnavigator.util.GuiUtils.showError;

/**
 * Service class for the handling of route info
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class RouteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteService.class);
    private static final String OSRM_URL = "https://router.project-osrm.org/route/v1/car/%s?geometries=geojson&overview=full&alternatives=false&skip_waypoints=true";

    @Autowired
    private RouteRepository routeRepo;

    /**
     * Fetches the OSRM API to get a car route from one coordinate to another.
     *
     * @param originLat latitude of the origin
     * @param originLon longitude of the origin
     * @param destLat   latitude of the destination
     * @param destLon   longitude of the destination
     * @return JSONObject containing the response from OSRM API
     */
    public JSONObject getRouteFromCoordinates(double originLat, double originLon, double destLat, double destLon) {
        String param = String.format(Locale.US, "%f,%f;%f,%f", originLon, originLat, destLon, destLat);
        String url = String.format(OSRM_URL, param);

        LOGGER.debug("Fetching route from {}", url);

        JSONObject result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            result = new JSONObject(response);
        } catch (HttpClientErrorException.BadRequest br) {
            showError("Bad request", "The navigation API reported a bad request. " +
                    "Please check your requested route.");
            LOGGER.error("RestTemplate error: {}", br.getMessage());
        } catch (ResourceAccessException | HttpClientErrorException ra) {
            showError("Connection error", "API could not be reached. " +
                    "Are you offline or behind firewall?");
            LOGGER.error("RestTemplate error: {}", ra.getMessage());
        }

        return result;
    }

    /**
     * Parses a list of coordinates from an OSRM JSON response object containing a route.
     *
     * @param json the complete JSON response from OSRM API
     * @return a List of Coordinates to be used with mapjfx
     */
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

            for (int i = 0; i < coordinates.length(); i++) {
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

    /**
     * Parses the distance of a route from an OSRM JSON response object and translates it into a
     * human-readable format.
     *
     * @param json the complete JSON response from OSRM API
     * @return a List of Coordinates to be used with mapjfx
     */
    public double getDistanceFromRoute(JSONObject json) {

        LOGGER.debug("Parsing distance from JSON...");

        try {
            if (!json.getString("code").equals("Ok")) {
                LOGGER.error("OSRM API Error: {}", json.getString("code"));
                return -1;
            }
            JSONArray routes = json.getJSONArray("routes");
            JSONObject routeObject = routes.getJSONObject(0);
            JSONArray legs = routeObject.getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            return leg.getDouble("distance");
        } catch (Exception e) {
            LOGGER.error("Error parsing distance from JSON: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * Returns all user saved Routes.
     *
     * @return a list of all Route objects in database
     */
    public List<Route> getSavedRoutes() {
        return this.routeRepo.findAll();
    }

    /**
     * Saves a route to Database
     *
     * @param route Route object to save
     * @return the saved route object
     */
    public Route saveRoute(Route route) {
        return this.routeRepo.save(route);
    }
}
