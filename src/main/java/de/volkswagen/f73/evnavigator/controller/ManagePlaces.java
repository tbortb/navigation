package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.model.IPlace;
import de.volkswagen.f73.evnavigator.service.PlaceService;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import static de.volkswagen.f73.evnavigator.util.GeoUtils.isValidCoordinate;
import static de.volkswagen.f73.evnavigator.util.MapUtils.buildMarker;

/**
 * Controller for the ManagePlaces view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class ManagePlaces extends ManagePlacesBase<IPlace, PlaceService>{


    @Override
    protected IPlace createNewPlace() {
        return new IPlace(this.nameInput.getText(),
                    Double.valueOf(this.latitudeInput.getText()),
                    Double.valueOf(this.longitudeInput.getText()));
    }

    @Override
    protected IPlace updatePlace() {
        this.selectedPlace.setLat(Double.valueOf(this.latitudeInput.getText()));
        this.selectedPlace.setLon(Double.valueOf(this.longitudeInput.getText()));
        this.selectedPlace.setName(this.nameInput.getText());
        return this.selectedPlace;
    }
}
