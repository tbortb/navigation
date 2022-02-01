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
                Integer.parseInt(this.voltageInput.getText()),
                Integer.parseInt(this.amperageInput.getText()),
                this.membershipNeededCb.isSelected(),
                this.capacityInput.getText(),
                this.hasFeeCb.isSelected(),
                this.notesInput.getText(),
                this.openingHoursInput.getText(),
                this.operatorInput.getText(),
                Integer.parseInt(this.socketSchukoInput.getText()),
                Integer.parseInt(this.socketType2Input.getText()),
                Integer.parseInt(this.socketType2OutputInput.getText()),
                Double.parseDouble(this.latitudeInput.getText()),
                Double.parseDouble(this.longitudeInput.getText()));
    }

    @Override
    protected Station updatePlace() {
        this.selectedPlace.setName(this.nameInput.getText());
        this.selectedPlace.setMaxVoltage(Integer.parseInt(this.voltageInput.getText()));
        this.selectedPlace.setMaxAmperage(Integer.parseInt(this.amperageInput.getText()));
        this.selectedPlace.setHasMembership(this.membershipNeededCb.isSelected());
        this.selectedPlace.setCapacity(this.capacityInput.getText());
        this.selectedPlace.setHasFee(this.hasFeeCb.isSelected());
        this.selectedPlace.setNote(this.notesInput.getText());
        this.selectedPlace.setOpeningHours(this.openingHoursInput.getText());
        this.selectedPlace.setOperator(this.operatorInput.getText());
        this.selectedPlace.setSocketSchukoAmount(Integer.parseInt(this.socketSchukoInput.getText()));
        this.selectedPlace.setSocketType2Amount(Integer.parseInt(this.socketType2Input.getText()));
        this.selectedPlace.setSocketType2Output(Integer.parseInt(this.socketType2OutputInput.getText()));
        this.selectedPlace.setLat(Double.parseDouble(this.latitudeInput.getText()));
        this.selectedPlace.setLon(Double.parseDouble(this.longitudeInput.getText()));
        return this.selectedPlace;
    }

}
