package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.IPlace;
import de.volkswagen.f73.evnavigator.service.IService;
import de.volkswagen.f73.evnavigator.util.MapUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static de.volkswagen.f73.evnavigator.util.GeoUtils.isValidCoordinate;
import static de.volkswagen.f73.evnavigator.util.GuiUtils.*;
import static de.volkswagen.f73.evnavigator.util.MapUtils.buildMarker;
import static de.volkswagen.f73.evnavigator.util.MapUtils.setInputFromCoordinate;

/**
 * Parent controller for the ManagePlaces and ManageStations views.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public abstract class ManagePlacesBase<T extends IPlace, S extends IService> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ManagePlacesBase.class);
    protected static final int ZOOM_DEFAULT = 14;
    protected static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);

    protected Marker currentMarker;
    protected T selectedPlace;

    @FXML
    protected AnchorPane root;
    @FXML
    protected MapView map;
    @FXML
    protected TextField nameInput;
    @FXML
    protected TextField latitudeInput;
    @FXML
    protected TextField longitudeInput;
    @FXML
    protected ListView<T> placesList;

    @Autowired
    protected S placeService;

    @Autowired
    protected FxWeaver fxWeaver;

    /**
     * Sets up the MapView.
     */
    @FXML
    public void initialize() {
        this.map.setCenter(LOCATION_DEFAULT);
        this.map.setZoom(ZOOM_DEFAULT);
        this.map.setAnimationDuration(200);
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
                this.updateMapWithSelectedItem();
            }
        });
    }

    protected void updateMapWithSelectedItem() {
        if (this.selectedPlace != null) {
            this.map.setCenter(new Coordinate(this.selectedPlace.getLat(), this.selectedPlace.getLon()));
            Coordinate selectedCoord = new Coordinate(this.selectedPlace.getLat(), this.selectedPlace.getLon());
            this.nameInput.setText(this.selectedPlace.getName());
            this.currentMarker.setPosition(selectedCoord);
            this.currentMarker.setVisible(true);
            setInputFromCoordinate(this.latitudeInput, this.longitudeInput, selectedCoord);
        }
    }

    /**
     * Sets this view as the center of the MainWindow stage.
     */
    public void show() {
        this.fetchPlaces();
        this.fxWeaver.getBean(MainWindow.class).setView(this.root, "Manage Places");
        setBackButtonNavigation(this.fxWeaver, SettingsMenu.class, true);
    }

    /**
     * Sets the Place ListView to a newly fetched list from DB
     */
    public void fetchPlaces() {
        this.placesList.setItems(FXCollections.observableArrayList(this.placeService.getAll()));
        this.placesList.refresh();
    }

    /**
     * Initialization of MapView event Handlers.
     */
    protected void setUpMapEventHandlers() {
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
    protected void savePlace() {

        if (this.nameInput.getText().isEmpty()) {
            showError("No name provided", "Please enter a name for your place.");
            return;
        }

        if (!isValidCoordinate(this.latitudeInput.getText(), this.longitudeInput.getText())) {
            showError(COORD_ERROR_TITLE, COORD_ERROR_BODY);
            return;
        }

        if (this.selectedPlace == null) {
            this.placeService.save(this.createNewPlace());
        } else {
            this.updatePlace();
            this.placeService.save(this.updatePlace());
        }

        this.fetchPlaces();

    }

    /**
     * Wired to Button, deletes a selected place from DB.
     */
    @FXML
    protected void deletePlace() {

        if (this.selectedPlace == null) {
            showError("No place selected", "Please select a place to delete from the list.");
        } else {
            this.placeService.delete(this.selectedPlace);
        }

        this.fetchPlaces();

    }

    /**
     * Method that creates a new place from the given input field
     *
     * @return a newly created place
     */
    protected abstract T createNewPlace();

    /**
     * Method that updates the selected place from the given input field
     *
     * @return the updated selected place
     */
    protected abstract T updatePlace();

}

