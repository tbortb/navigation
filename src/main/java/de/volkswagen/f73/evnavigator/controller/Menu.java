package de.volkswagen.f73.evnavigator.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller for the menu view.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class Menu implements IController {

    @FXML
    private HBox menuBox;

    @Autowired
    private FxWeaver fxWeaver;

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.menuBox, "Main Menu");
    }

    @FXML
    private void openNavigationView() {
        if (!this.fxWeaver.getBean(Navigation.class).isLoaded) {
            this.fxWeaver.load(Navigation.class);
        }
        this.fxWeaver.getBean(Navigation.class).show();

    }

    @FXML
    private void openSettingsView() {
        this.fxWeaver.load(SettingsMenu.class).getController().show();
    }

}
