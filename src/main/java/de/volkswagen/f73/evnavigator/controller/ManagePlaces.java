package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.service.PlaceService;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

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
