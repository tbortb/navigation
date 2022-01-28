package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.model.Route;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.service.PlaceService;
import de.volkswagen.f73.evnavigator.service.RouteService;
import de.volkswagen.f73.evnavigator.service.StationService;
import de.volkswagen.f73.evnavigator.util.GeoUtils;
import de.volkswagen.f73.evnavigator.util.MapUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static de.volkswagen.f73.evnavigator.util.MapUtils.buildMarker;
import static de.volkswagen.f73.evnavigator.util.MapUtils.setInputFromCoordinate;


/**
 * Controller for the navigation view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class Navigation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Navigation.class);
    private static final int ZOOM_DEFAULT = 14;
    private static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);
    private Marker currentMarker;
    private final List<Marker> stationMarkers = new ArrayList<>();
    private Marker destinationMarker;
    private Marker originMarker;
    private Coordinate currentCoordinate;
    private CoordinateLine currentPath;
    private CoordinateLine linearPath;

    @FXML
    private AnchorPane navigationPane;
    @FXML
    private MapView map;
    @FXML
    private TextField originLatInput;
    @FXML
    private TextField originLonInput;
    @FXML
    private TextField destLatInput;
    @FXML
    private TextField destLonInput;
    @FXML
    private Slider zoomSlider;
    @FXML
    private ListView<Place> placeList;
    @FXML
    private ListView<Route> routeList;
    @FXML
    private Button saveRouteBtn;
    @FXML
    private Button calcRouteBtn;
    @FXML
    private Slider distanceSlider;

    @Autowired
    private FxWeaver fxWeaver;
    @Autowired
    private StationService stationService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private PlaceService placeService;

    /**
     * Sets up the MapView.
     */
    @FXML
    private void initialize() {
        this.map.setZoom(ZOOM_DEFAULT);
        this.map.setCenter(LOCATION_DEFAULT);
        this.map.setAnimationDuration(500);

        // don't block the view when initializing map
        Platform.runLater(() -> {
            LOGGER.debug("Initializing MapJFX map...");
            this.map.initialize();
        });

        this.map.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.setUpMapEvents();
            }
        });
    }


    /**
     * Sets this view as the center view in MainWindow, fetches places and routes from database.
     */
    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.navigationPane, "Navigation");
        this.routeList.setItems(FXCollections.observableArrayList(this.routeService.getSavedRoutes()));
        this.placeList.setItems(FXCollections.observableArrayList(this.placeService.getAllPlaces()));
    }

    /**
     * Sets up event handlers and bindings.
     */
    private void setUpMapEvents() {
        this.map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            Double lat = event.getCoordinate().normalize().getLatitude();
            Double lon = event.getCoordinate().normalize().getLongitude();
            this.displayMarkerOnMap(lat, lon);
        });


        this.zoomSlider.valueProperty().bindBidirectional(this.map.zoomProperty());

        this.calcRouteBtn.disableProperty().bind(this.originLatInput.textProperty().isEmpty().or(
                this.originLonInput.textProperty().isEmpty().or(
                        this.destLatInput.textProperty().isEmpty().or(
                                this.destLonInput.textProperty().isEmpty()
                        )
                )
        ));

        this.saveRouteBtn.setDisable(true);

        this.placeList.setOnMouseClicked(e -> {
            if (this.placeList.getSelectionModel().getSelectedItem() != null) {
                Place currentPlace = this.placeList.getSelectionModel().getSelectedItem();
                this.displayMarkerOnMap(currentPlace.getLat(), currentPlace.getLon());
                this.map.setCenter(new Coordinate(currentPlace.getLat(), currentPlace.getLon()));
            }
        });

        this.routeList.setOnMouseClicked(e -> {
            if (this.routeList.getSelectionModel().getSelectedItem() != null) {
                Route currentRoute = this.routeList.getSelectionModel().getSelectedItem();
                this.originLatInput.setText(currentRoute.getStartLat().toString());
                this.originLonInput.setText(currentRoute.getStartLon().toString());
                this.destLatInput.setText(currentRoute.getEndLat().toString());
                this.destLonInput.setText(currentRoute.getEndLon().toString());
                this.calculateRoute();
                //TODO: calculate zoom level and map position to display entire route
            }
        });

    }

    /**
     * Shows a marker of a given coordinate on the map.
     *
     * @param lat latitude
     * @param lon longitude
     */
    private void displayMarkerOnMap(Double lat, Double lon) {
        if (this.currentMarker != null) {
            this.map.removeMarker(this.currentMarker);
        }

        this.currentCoordinate = new Coordinate(lat, lon);

        this.currentMarker = new Marker(this.getClass().getResource("/images/markers/place.png"), -20, -70)
                .setPosition(this.currentCoordinate)
                .setVisible(true);

        this.map.addMarker(this.currentMarker);
    }

    /**
     * Wired to button for loading coordinates from a marker on the map.
     */
    @FXML
    private void loadOriginFromMap() {
        setInputFromCoordinate(this.originLatInput, this.originLonInput, this.currentCoordinate);
    }

    /**
     * Wired to button for loading coordinates from a marker on the map.
     */
    @FXML
    private void loadDestinationFromMap() {
        setInputFromCoordinate(this.destLatInput, this.destLonInput, this.currentCoordinate);
    }

    /**
     * Shows a route between origin/destination that the user has set before.
     * Draws the real route from API as well as the air-line onto the map.
     */
    @FXML
    private void calculateRoute() {
        if (this.currentPath != null) {
            this.map.removeCoordinateLine(this.currentPath);
        }
        if (this.linearPath != null) {
            this.map.removeCoordinateLine(this.linearPath);
        }

        double originLat = Double.parseDouble(this.originLatInput.getText());
        double originLon = Double.parseDouble(this.originLonInput.getText());
        double destLat = Double.parseDouble(this.destLatInput.getText());
        double destLon = Double.parseDouble(this.destLonInput.getText());
        Coordinate origin = new Coordinate(originLat, originLon);
        Coordinate dest = new Coordinate(destLat, destLon);

        JSONObject json = this.routeService.getRouteFromCoordinates(originLat, originLon, destLat, destLon);
        List<Coordinate> waypoints = this.routeService.getCoordinatesFromRoute(json);
        LOGGER.info("Route distance is: {}", this.routeService.getDistanceFromRoute(json));
        LOGGER.info("Linear distance is: {}", GeoUtils.getLinearDistanceKm(originLat, originLon, destLat, destLon));

        this.currentPath = new CoordinateLine(waypoints);
        this.currentPath.setVisible(true);
        this.currentPath.setColor(Color.BLUE);
        this.currentPath.setWidth(10);

        this.linearPath = new CoordinateLine(origin, dest);
        this.linearPath.setVisible(true);
        this.linearPath.setColor(Color.PURPLE);
        this.currentPath.setWidth(6);
        this.map.addCoordinateLine(this.linearPath);


        this.setRouteMarkers(origin, dest);

        this.saveRouteBtn.setDisable(false);
    }

    /**
     * Draws two different markers on the map for origin and destination coordinates.
     *
     * @param origin Coordinate object of the origin
     * @param dest   Coordinate object of the destination
     */
    private void setRouteMarkers(Coordinate origin, Coordinate dest) {
        this.map.addCoordinateLine(this.currentPath);

        if (this.destinationMarker != null) {
            this.map.removeMarker(this.destinationMarker);
            this.map.removeMarker(this.originMarker);
        }

        this.destinationMarker = buildMarker(dest.getLatitude(), dest.getLongitude(), MapUtils.MarkerImage.DESTINATION);
        this.originMarker = buildMarker(origin.getLatitude(), origin.getLongitude(), MapUtils.MarkerImage.ORIGIN);

        if (this.currentMarker != null) {
            this.map.removeMarker(this.currentMarker);
            this.currentMarker = null;
        }

        this.map.addMarker(this.originMarker);
        this.map.addMarker(this.destinationMarker);
    }


    /**
     * Opens a window asking the user for a name and saves a named route to the database.
     * Refreshes the route list in view.
     */
    @FXML
    public void saveRoute() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("New Route");
        td.setHeaderText("Please enter a name for your new route.");
        td.showAndWait();
        String name = td.getEditor().getText();
        Route route = new Route(name,
                this.originMarker.getPosition().getLatitude(),
                this.originMarker.getPosition().getLongitude(),
                this.destinationMarker.getPosition().getLatitude(),
                this.destinationMarker.getPosition().getLongitude());
        this.routeService.saveRoute(route);
        this.routeList.setItems(FXCollections.observableArrayList(this.routeService.getSavedRoutes()));
    }

    /**
     * Shows stations within a user defined radius close to a single marker.
     */
    @FXML
    public void onShowCloseStations() {
        this.stationMarkers.forEach(this.map::removeMarker);
        this.stationMarkers.clear();
        List<Station> closeStations = this.stationService.getStationsCloseTo(this.currentMarker.getPosition().getLatitude(),
                this.currentMarker.getPosition().getLongitude(),
                this.distanceSlider.getValue());

        List<Marker> markers = closeStations.stream().map(s -> buildMarker(s.getLat(), s.getLon(), MapUtils.MarkerImage.STATION)
        ).collect(Collectors.toList());

        this.stationMarkers.addAll(markers);
        this.stationMarkers.forEach(this.map::addMarker);
    }

    /**
     * Shows stations within a user defined radius along a calculated route.
     */
    @FXML
    public void showStationsAlongRoute() {

        this.stationMarkers.forEach(this.map::removeMarker);
        this.stationMarkers.clear();

        List<Station> closeStations = this.stationService.getStationsAlongPath(this.currentPath, this.distanceSlider.getValue());

        List<Marker> markers = closeStations.stream().map(s -> buildMarker(s.getLat(), s.getLon(), MapUtils.MarkerImage.STATION)
        ).collect(Collectors.toList());

        this.stationMarkers.addAll(markers);
        this.stationMarkers.forEach(this.map::addMarker);

    }
}
