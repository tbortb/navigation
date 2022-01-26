package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
public class AddPoi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddPoi.class);
    private static final int ZOOM_DEFAULT = 14;
    private static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);

    private Marker currentMarker;

    @FXML
    private HBox poiBox;
    @FXML
    private MapView map;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField operatorInput;
    @FXML
    private TextField latitudeInput;
    @FXML
    private TextField longitudeInput;

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private void initialize() {
        map.setZoom(ZOOM_DEFAULT);
        map.setCenter(LOCATION_DEFAULT);
        LOGGER.debug("Initializing MapJFX map...");
        setUpMapEventHandlers();
        // don't block the view when initializing map
        Platform.runLater(() -> map.initialize());
    }

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.poiBox, "Add Point of Interest");
    }

    private void setUpMapEventHandlers() {
        map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            if (this.currentMarker != null) {
                map.removeMarker(this.currentMarker);
            }
            final Coordinate newPosition = event.getCoordinate().normalize();

            this.currentMarker = new Marker(getClass().getResource("/images/poi.png"), -20, -70)
                    .setPosition(newPosition)
                    .setVisible(true);
            this.latitudeInput.setText(String.valueOf(newPosition.getLatitude()));
            this.longitudeInput.setText(String.valueOf(newPosition.getLongitude()));
            this.map.addMarker(this.currentMarker);
        });
    }

    @FXML
    private void savePoi() {
        throw new NotImplementedException();
    }

}
