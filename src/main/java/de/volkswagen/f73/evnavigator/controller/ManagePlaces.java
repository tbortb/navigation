package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.service.PlaceService;
import de.volkswagen.f73.evnavigator.util.MapUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.volkswagen.f73.evnavigator.util.GeoUtils.isValidCoordinate;
import static de.volkswagen.f73.evnavigator.util.GuiUtils.*;
import static de.volkswagen.f73.evnavigator.util.MapUtils.buildMarker;
import static de.volkswagen.f73.evnavigator.util.MapUtils.setInputFromCoordinate;

/**
 * Controller for the ManagePlaces view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class ManagePlaces {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagePlaces.class);
    private static final int ZOOM_DEFAULT = 14;
    private static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);

    private Marker currentMarker;
    private Place selectedPlace;

    @FXML
    private AnchorPane root;
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
    @FXML
    private ListView<Place> placesList;

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
        this.map.setAnimationDuration(500);
        LOGGER.debug("Initializing MapJFX map...");
        this.setUpMapEventHandlers();
        // don't block the view when initializing map
        Platform.runLater(() -> this.map.initialize());

        this.map.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.currentMarker = buildMarker(LOCATION_DEFAULT.getLatitude(), LOCATION_DEFAULT.getLongitude(), MapUtils.MarkerImage.PLACE, false);
                this.map.addMarker(this.currentMarker);
            }
        });

        this.placesList.setOnMouseClicked(e -> {
            if (this.placesList.getSelectionModel().getSelectedItem() != null) {
                this.selectedPlace = this.placesList.getSelectionModel().getSelectedItem();
                this.map.setCenter(new Coordinate(this.selectedPlace.getLat(), this.selectedPlace.getLon()));
                Coordinate selectedCoord = new Coordinate(this.selectedPlace.getLat(), this.selectedPlace.getLon());
                this.nameInput.setText(this.selectedPlace.getName());
                this.currentMarker.setPosition(selectedCoord);
                this.currentMarker.setVisible(true);
                setInputFromCoordinate(this.latitudeInput, this.longitudeInput, selectedCoord);
            }
        });
    }

    /**
     * Sets this view as the center of the MainWindow stage.
     */
    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.root, "Add Favorite Place");
        setBackButtonNavigation(this.fxWeaver, SettingsMenu.class, false);
        this.fetchPlaces();
    }

    /**
     * Sets the Place ListView to a newly fetched list from DB
     */
    public void fetchPlaces() {
        this.placesList.setItems(FXCollections.observableArrayList(this.placeService.getAllPlaces()));
    }

    /**
     * Initialization of MapView event Handlers.
     */
    private void setUpMapEventHandlers() {
        this.map.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            Coordinate newPosition = event.getCoordinate().normalize();

            this.currentMarker.setPosition(newPosition);
            this.currentMarker.setVisible(true);
            this.placesList.getSelectionModel().clearSelection();
            setInputFromCoordinate(this.latitudeInput, this.longitudeInput, newPosition);
            this.selectedPlace = null;

        });
    }

    /**
     * Wired to Button, saves user input to a Place in database or updates a selected Place.
     */
    @FXML
    private void savePlace() {

        if (this.nameInput.getText().isEmpty()) {
            showError("No name provided", "Please enter a name for your place.");
            return;
        }

        if (!isValidCoordinate(this.latitudeInput.getText(), this.longitudeInput.getText())) {
            showError(COORD_ERROR_TITLE, COORD_ERROR_BODY);
            return;
        }

        if (this.selectedPlace == null) {
            Place newPlace = new Place(this.nameInput.getText(),
                    Double.valueOf(this.latitudeInput.getText()),
                    Double.valueOf(this.longitudeInput.getText()));
            this.placeService.savePlace(newPlace);
        } else {
            this.selectedPlace.setLat(Double.valueOf(this.latitudeInput.getText()));
            this.selectedPlace.setLon(Double.valueOf(this.longitudeInput.getText()));
            this.selectedPlace.setName(this.nameInput.getText());
            this.placeService.savePlace(this.selectedPlace);
        }

        fetchPlaces();

    }

    /**
     * Wired to Button, deletes a selected place from DB.
     */
    @FXML
    private void deletePlace() {

        if (this.selectedPlace == null) {
            showError("No place selected", "Please select a place to delete from the list.");
        } else {
            this.placeService.deletePlace(this.selectedPlace);
        }

        fetchPlaces();

    }

}
