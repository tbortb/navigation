package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.service.RouteService;
import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
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
    private Marker destinationMarker;
    private Marker originMarker;
    private Coordinate currentCoordinate;
    private CoordinateLine currentPath;

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
    private ListView poiList;
    @FXML
    private ListView routeList;

    @Autowired
    private FxWeaver fxWeaver;
    @Autowired
    private StationService stationService;
    @Autowired
    private RouteService routeService;

    @FXML
    private void initialize() {
        map.setZoom(ZOOM_DEFAULT);
        map.setCenter(LOCATION_DEFAULT);
        map.setAnimationDuration(500);
        setUpMapEvents();
        // don't block the view when initializing map
        Platform.runLater(() -> {
            LOGGER.debug("Initializing MapJFX map...");
            map.initialize();
        });
    }

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.navigationPane, "Navigation");
    }

    private void setUpMapEvents() {
        map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            if (this.currentMarker != null) {
                map.removeMarker(this.currentMarker);
            }
            this.currentCoordinate = event.getCoordinate().normalize();

            this.currentMarker = new Marker(getClass().getResource("/images/markers/poi.png"), -20, -70)
                    .setPosition(this.currentCoordinate)
                    .setVisible(true);

            this.map.addMarker(this.currentMarker);
        });

        this.zoomSlider.valueProperty().bindBidirectional(this.map.zoomProperty());
    }

    @FXML
    private void loadOriginFromMap() {
        setInputFromCoordinate(this.originLatInput, this.originLonInput);
    }

    @FXML
    private void loadDestinationFromMap() {
        setInputFromCoordinate(this.destLatInput, this.destLonInput);
    }

    @FXML
    private void loadOriginFromPoi() {
        throw new NotImplementedException();
    }

    @FXML
    private void loadDestinationFromPoi() {
        throw new NotImplementedException();
    }

    @FXML
    private void calculateRoute() {
        if (this.currentPath != null) {
            this.map.removeCoordinateLine(this.currentPath);
        }

        double originLat = Double.parseDouble(this.originLatInput.getText());
        double originLon = Double.parseDouble(this.originLonInput.getText());
        double destLat = Double.parseDouble(this.destLatInput.getText());
        double destLon = Double.parseDouble(this.destLonInput.getText());

        Coordinate origin = new Coordinate(originLat, originLon);
        Coordinate dest = new Coordinate(destLat, destLon);


        JSONObject json = routeService.getRouteFromCoordinates(originLat, originLon, destLat, destLon);

        List<Coordinate> waypoints = routeService.getCoordinatesFromRoute(json);
        LOGGER.info("Calculated distance is: {}", routeService.getDistanceFromRoute(json));

        this.currentPath = new CoordinateLine(waypoints);

        this.currentPath.setVisible(true);
        this.currentPath.setColor(Color.BLUE);
        this.currentPath.setWidth(10);

        if (this.destinationMarker != null) {
            this.map.removeMarker(this.destinationMarker);
            this.map.removeMarker(this.originMarker);
        }

        this.destinationMarker = new Marker(getClass().getResource("/images/markers/destination.png"), -20, -70)
                .setPosition(dest)
                .setVisible(true);

        this.originMarker = new Marker(getClass().getResource("/images/markers/home.png"), -20, -70)
                .setPosition(origin)
                .setVisible(true);

        if (this.currentMarker != null) {
            this.map.removeMarker(this.currentMarker);
            this.currentMarker = null;
        }

        this.map.addMarker(this.originMarker);
        this.map.addMarker(this.destinationMarker);

        this.map.addCoordinateLine(this.currentPath);

    }

    private void setInputFromCoordinate(TextField lat, TextField lon) {
        LOGGER.debug("Setting input from map click event...");
        if (this.currentCoordinate != null) {
            lat.setText(String.valueOf(this.currentCoordinate.getLatitude()));
            lon.setText(String.valueOf(this.currentCoordinate.getLongitude()));
        }
    }



}
