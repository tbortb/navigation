package de.volkswagen.f73.evnavigator.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class SettingsMenu {

    @FXML
    private HBox menuBox;

    @Autowired
    private FxWeaver fxWeaver;

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.menuBox, "Settings");
    }

    @FXML
    private void openAddStationView() {
        this.fxWeaver.load(AddStation.class).getController().show();
    }

    @FXML
    private void openAddPoiView() {
        this.fxWeaver.load(AddPoi.class).getController().show();
    }

}
