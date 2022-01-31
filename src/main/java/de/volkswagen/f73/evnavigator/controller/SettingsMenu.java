package de.volkswagen.f73.evnavigator.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.volkswagen.f73.evnavigator.util.GuiUtils.setBackButtonNavigation;

/**
 * Controller for the intermediate SettingsMenu view
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class SettingsMenu implements IMenuController {

    @FXML
    private HBox menuBox;

    @Autowired
    private FxWeaver fxWeaver;

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.menuBox, "Settings");
        setBackButtonNavigation(this.fxWeaver, Menu.class);
    }

    @FXML
    private void openAddStationView() {
        this.fxWeaver.load(ManageStations.class).getController().show();
    }

    @FXML
    private void openAddPlaceView() {
        this.fxWeaver.load(ManagePlaces.class).getController().show();
    }

}
