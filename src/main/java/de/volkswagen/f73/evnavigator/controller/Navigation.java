package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.POI;
import de.volkswagen.f73.evnavigator.model.Route;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.service.POIService;
import de.volkswagen.f73.evnavigator.service.RouteService;
import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    private List<Marker> stationMarkers = new ArrayList<>();
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
    private ListView<POI> poiList;
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
    private POIService poiService;

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

        this.calcRouteBtn.disableProperty().bind(this.originLatInput.textProperty().isEmpty().or(
                this.originLonInput.textProperty().isEmpty().or(
                        this.destLatInput.textProperty().isEmpty().or(
                                this.destLonInput.textProperty().isEmpty()
                        )
                )
        ));

        this.saveRouteBtn.setDisable(true);

        this.poiList.setOnMouseClicked(e -> {
            if(this.poiList.getSelectionModel().getSelectedItem() != null){
                POI currentPoi = this.poiList.getSelectionModel().getSelectedItem();
                displayMarkerOnMap(currentPoi.getLat(), currentPoi.getLon());
                this.map.setCenter(new Coordinate(currentPoi.getLat(), currentPoi.getLon()));
            }
        });

        this.routeList.setOnMouseClicked(e -> {
            if(this.routeList.getSelectionModel().getSelectedItem() != null){
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

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.navigationPane, "Navigation");
        this.routeList.setItems(FXCollections.observableArrayList(routeService.getSavedRoutes()));
        this.poiList.setItems(FXCollections.observableArrayList(poiService.getAllPOIs()));
    }

    private void setUpMapEvents() {
        map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            Double lat = event.getCoordinate().normalize().getLatitude();
            Double lon = event.getCoordinate().normalize().getLongitude();
            this.displayMarkerOnMap(lat, lon);
        });


        this.zoomSlider.valueProperty().bindBidirectional(this.map.zoomProperty());
    }

    private void displayMarkerOnMap(Double lat, Double lon) {
        if (this.currentMarker != null) {
            map.removeMarker(this.currentMarker);
        }

        this.currentCoordinate = new Coordinate(lat, lon);

        this.currentMarker = new Marker(getClass().getResource("/images/markers/poi.png"), -20, -70)
                .setPosition(this.currentCoordinate)
                .setVisible(true);

        this.map.addMarker(this.currentMarker);
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

        this.saveRouteBtn.setDisable(false);

    }

    private void setInputFromCoordinate(TextField lat, TextField lon) {
        LOGGER.debug("Setting input from map click event...");
        if (this.currentCoordinate != null) {
            lat.setText(String.valueOf(this.currentCoordinate.getLatitude()));
            lon.setText(String.valueOf(this.currentCoordinate.getLongitude()));
        }
    }


    @FXML
    public void saveRoute() {
        TextInputDialog td =new TextInputDialog();
        td.setTitle("Geben sie einen Namen für die Route an");
        td.showAndWait();
        String name = td.getEditor().getText();
        Route route = new Route(name,
                this.originMarker.getPosition().getLatitude(),
                this.originMarker.getPosition().getLongitude(),
                this.destinationMarker.getPosition().getLatitude(),
                this.destinationMarker.getPosition().getLongitude());
        this.routeService.saveRoute(route);
        this.routeList.setItems(FXCollections.observableArrayList(routeService.getSavedRoutes()));
    }

    @FXML
    public void onShowCloseStations() {
        this.stationMarkers.forEach(this.map::removeMarker);
        this.stationMarkers.clear();
        List<Station> closeStations = this.stationService.getStationsCloseTo(this.currentMarker.getPosition().getLatitude(),
                this.currentMarker.getPosition().getLongitude(),
                this.distanceSlider.getValue());

        List<Marker> markers = closeStations.stream().map(s -> new Marker(getClass().getResource("/images/markers/station.png"),
                -20,-70).setPosition(new Coordinate(s.getLat(), s.getLon())).setVisible(true)
                ).collect(Collectors.toList());

        this.stationMarkers.addAll(markers);
        this.stationMarkers.forEach(this.map::addMarker);
    }
}
