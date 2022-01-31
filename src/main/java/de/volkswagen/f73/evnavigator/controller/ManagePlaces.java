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
public class ManagePlaces extends ManagePlacesBase<Place, PlaceService>{


    @Override
    protected Place createNewPlace() {
        return new Place(this.nameInput.getText(),
                    Double.valueOf(this.latitudeInput.getText()),
                    Double.valueOf(this.longitudeInput.getText()));
    }

    @Override
    protected Place updatePlace() {
        this.selectedPlace.setLat(Double.valueOf(this.latitudeInput.getText()));
        this.selectedPlace.setLon(Double.valueOf(this.longitudeInput.getText()));
        this.selectedPlace.setName(this.nameInput.getText());
        return this.selectedPlace;
    }
}
