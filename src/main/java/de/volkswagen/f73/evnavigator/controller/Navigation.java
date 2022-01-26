package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private Coordinate lastCoordinate;

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
    private ListView poiList;
    @FXML
    private ListView routeList;

    @Autowired
    private FxWeaver fxWeaver;
    @Autowired
    private StationService stationService;

    @FXML
    private void initialize() {
        map.setZoom(ZOOM_DEFAULT);
        map.setCenter(LOCATION_DEFAULT);
        setUpMapEventHandlers();
        // don't block the view when initializing map
        Platform.runLater(() -> {
            LOGGER.debug("Initializing MapJFX map...");
            map.initialize();
        });

    }

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.navigationPane, "Navigation");
    }

    private void setUpMapEventHandlers() {
        map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            if (this.currentMarker != null) {
                map.removeMarker(this.currentMarker);
            }
            this.lastCoordinate = event.getCoordinate().normalize();

            this.currentMarker = new Marker(getClass().getResource("/images/poi.png"), -20, -70)
                    .setPosition(this.lastCoordinate)
                    .setVisible(true);
            this.map.addMarker(this.currentMarker);
        });
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
        throw new NotImplementedException();
    }

    private void setInputFromCoordinate(TextField lat, TextField lon) {
        if (this.lastCoordinate != null) {
            lat.setText(String.valueOf(this.lastCoordinate.getLatitude()));
            lon.setText(String.valueOf(this.lastCoordinate.getLongitude()));
        }
    }

}
