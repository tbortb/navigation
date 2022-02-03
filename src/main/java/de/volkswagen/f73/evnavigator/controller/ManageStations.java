package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static de.volkswagen.f73.evnavigator.util.GuiUtils.*;

/**
 * Controller for the AddStation view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class ManageStations extends ManagePlacesBase<Station, StationService> {

    @FXML
    private TextField voltageInput;
    @FXML
    private TextField amperageInput;
    @FXML
    private TextField capacityInput;
    @FXML
    private TextField openingHoursInput;
    @FXML
    private TextField operatorInput;
    @FXML
    private TextField socketSchukoInput;
    @FXML
    private TextField socketType2Input;
    @FXML
    private TextField socketType2OutputInput;
    @FXML
    private TextArea notesInput;
    @FXML
    private CheckBox membershipNeededCb;
    @FXML
    private CheckBox hasFeeCb;

    @Override
    protected Station createNewPlace() {
        return new Station(this.nameInput.getText(),
                tfToIntegerOrNull(this.voltageInput),
                tfToIntegerOrNull(this.amperageInput),
                this.membershipNeededCb.isIndeterminate() ? null : this.membershipNeededCb.isSelected(),
                this.capacityInput.getText(),
                this.hasFeeCb.isIndeterminate() ? null : this.hasFeeCb.isSelected(),
                this.notesInput.getText(),
                this.openingHoursInput.getText(),
                this.operatorInput.getText(),
                tfToIntegerOrNull(this.socketSchukoInput),
                tfToIntegerOrNull(this.socketType2Input),
                tfToIntegerOrNull(this.socketType2OutputInput),
                tfToDoubleOrNull(this.latitudeInput),
                tfToDoubleOrNull(this.longitudeInput));
    }

    @Override
    protected Station updatePlace() {
        this.selectedPlace.setName(this.nameInput.getText());
        this.selectedPlace.setMaxVoltage(tfToIntegerOrNull(this.voltageInput));
        this.selectedPlace.setMaxAmperage(tfToIntegerOrNull(this.amperageInput));
        this.selectedPlace.setHasMembership(this.membershipNeededCb.isSelected());
        this.selectedPlace.setCapacity(this.capacityInput.getText());
        this.selectedPlace.setHasFee(this.hasFeeCb.isSelected());
        this.selectedPlace.setNote(this.notesInput.getText());
        this.selectedPlace.setOpeningHours(this.openingHoursInput.getText());
        this.selectedPlace.setOperator(this.operatorInput.getText());
        this.selectedPlace.setSocketSchukoAmount(tfToIntegerOrNull(this.socketSchukoInput));
        this.selectedPlace.setSocketType2Amount(tfToIntegerOrNull(this.socketType2Input));
        this.selectedPlace.setSocketType2Output(tfToIntegerOrNull(this.socketType2OutputInput));
        this.selectedPlace.setLat(tfToDoubleOrNull(this.latitudeInput));
        this.selectedPlace.setLon(tfToDoubleOrNull(this.longitudeInput));
        return this.selectedPlace;
    }

    /**
     * Sets this view as the center of the MainWindow stage and selects a defined place
     */
    public void show(Class<? extends IController> backClass, Station selectedPlace) {
        this.map.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.fetchPlaces();
                this.fxWeaver.getBean(MainWindow.class).setView(this.root, "Manage Stations");
                setBackButtonNavigation(this.fxWeaver, backClass, true);
                Optional<Station> optStation = this.placesList.getItems().stream().filter(s -> s.getId().equals(selectedPlace.getId())).findAny();
                if (optStation.isPresent()) {
                    this.placesList.getSelectionModel().select(selectedPlace);
                    this.selectedPlace = selectedPlace;
                    this.updateMapWithSelectedItem();
                } else {
                    LOGGER.info("Place could not be found in database: {}", selectedPlace);
                }
            }
        });
    }

    @Override
    protected void clearFields() {
        super.clearFields();

        this.root.getChildren().forEach(child -> {
            if (child instanceof TextField) {
                ((TextField) child).clear();
            }
        });

        this.membershipNeededCb.setSelected(false);
        this.membershipNeededCb.setIndeterminate(true);
        this.hasFeeCb.setSelected(false);
        this.hasFeeCb.setIndeterminate(true);

    }

    @Override
    protected void updateMapWithSelectedItem() {
        super.updateMapWithSelectedItem();
        numberOrEmptyToTextField(this.voltageInput, this.selectedPlace.getMaxVoltage());
        numberOrEmptyToTextField(this.amperageInput, this.selectedPlace.getMaxAmperage());
        this.capacityInput.setText(this.selectedPlace.getCapacity());

        if (this.selectedPlace.getHasFee() == null) {
            this.hasFeeCb.setIndeterminate(true);
        } else {
            this.hasFeeCb.setIndeterminate(false);
            this.hasFeeCb.setSelected(this.selectedPlace.getHasFee());
        }

        if (this.selectedPlace.getHasMembership() == null) {
            this.membershipNeededCb.setIndeterminate(true);
        } else {
            this.membershipNeededCb.setIndeterminate(false);
            this.membershipNeededCb.setSelected(this.selectedPlace.getHasMembership());
        }

        this.notesInput.setText(this.selectedPlace.getNote());
        this.openingHoursInput.setText(this.selectedPlace.getOpeningHours());
        this.operatorInput.setText(this.selectedPlace.getOperator());

        numberOrEmptyToTextField(this.socketSchukoInput, this.selectedPlace.getSocketSchukoAmount());
        numberOrEmptyToTextField(this.socketType2Input, this.selectedPlace.getSocketType2Amount());
        numberOrEmptyToTextField(this.socketType2OutputInput, this.selectedPlace.getSocketType2Output());

    }

    public boolean isLoaded() {
        return this.isLoaded;
    }
}
