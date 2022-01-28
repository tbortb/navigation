package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.service.PlaceService;
import de.volkswagen.f73.evnavigator.util.MapUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller for the AddPlace view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class AddPlace {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddPlace.class);
    private static final int ZOOM_DEFAULT = 14;
    private static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);

    private Marker currentMarker;

    @FXML
    private HBox placeBox;
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
    private PlaceService placeService;

    @Autowired
    private FxWeaver fxWeaver;

    /**
     * Sets up the MapView.
     */
    @FXML
    private void initialize() {
        this.map.setZoom(ZOOM_DEFAULT);
        this.map.setCenter(LOCATION_DEFAULT);
        LOGGER.debug("Initializing MapJFX map...");
        this.setUpMapEventHandlers();
        // don't block the view when initializing map
        Platform.runLater(() -> this.map.initialize());
    }

    /**
     * Sets this view as the center of the MainWindow stage.
     */
    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.placeBox, "Add Favorite Place");
    }

    /**
     * Initialization of MapView event Handlers.
     */
    private void setUpMapEventHandlers() {
        this.map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            if (this.currentMarker != null) {
                this.map.removeMarker(this.currentMarker);
            }
            Coordinate newPosition = event.getCoordinate().normalize();

            this.currentMarker = MapUtils.buildMarker(newPosition.getLatitude(), newPosition.getLongitude(), MapUtils.MarkerImage.PLACE);
            this.latitudeInput.setText(String.valueOf(newPosition.getLatitude()));
            this.longitudeInput.setText(String.valueOf(newPosition.getLongitude()));
            this.map.addMarker(this.currentMarker);
        });
    }

    /**
     * Wired to Button, saves user input to a Place in database.
     */
    @FXML
    private void savePlace() {
        Place newPlace = new Place(this.nameInput.getText(),
                Double.valueOf(this.latitudeInput.getText()),
                Double.valueOf(this.longitudeInput.getText()));
        this.placeService.savePlace(newPlace);
    }

}
