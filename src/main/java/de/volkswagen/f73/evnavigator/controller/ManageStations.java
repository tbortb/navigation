package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.service.StationService;
import de.volkswagen.f73.evnavigator.util.MapUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static de.volkswagen.f73.evnavigator.util.GuiUtils.setBackButtonNavigation;

/**
 * Controller for the AddStation view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class ManageStations extends ManagePlacesBase<Station, StationService>{

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
                this.membershipNeededCb.isSelected(),
                this.capacityInput.getText(),
                this.hasFeeCb.isSelected(),
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
    public void show(Class<? extends IMenuController> backClass, Station selectedPlace) {
        this.map.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
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
    public void updateMapWithSelectedItem(){
        super.updateMapWithSelectedItem();
        this.voltageInput.setText(String.valueOf(this.selectedPlace.getMaxVoltage()));
        this.amperageInput.setText(String.valueOf(this.selectedPlace.getMaxAmperage()));
        this.membershipNeededCb.setSelected(nullToFalse(this.selectedPlace.getHasMembership()));
        this.capacityInput.setText(this.selectedPlace.getCapacity());
        this.hasFeeCb.setSelected(nullToFalse(this.selectedPlace.getHasFee()));
        this.notesInput.setText(this.selectedPlace.getNote());
        this.openingHoursInput.setText(this.selectedPlace.getOpeningHours());
        this.operatorInput.setText(this.selectedPlace.getOperator());
        this.socketSchukoInput.setText(String.valueOf(this.selectedPlace.getSocketSchukoAmount()));
        this.socketType2Input.setText(String.valueOf(this.selectedPlace.getSocketType2Amount()));
        this.socketType2OutputInput.setText(String.valueOf(this.selectedPlace.getSocketType2Output()));

    }

    private Integer tfToIntegerOrNull(TextField intTf){
        try {
            return Integer.parseInt(intTf.getText());
        } catch (NumberFormatException e){
            LOGGER.info("Could not parse {} ", intTf.getText());
            return null;
        }
    }

    private Double tfToDoubleOrNull(TextField doubleTf){
        try {
            return Double.parseDouble(doubleTf.getText());
        } catch (NumberFormatException e){
            LOGGER.info("Could not parse {} ", doubleTf.getText());
            return null;
        }
    }

    private Boolean nullToFalse(Boolean checkBool){
        return checkBool == null ? false : checkBool;
    }

}
