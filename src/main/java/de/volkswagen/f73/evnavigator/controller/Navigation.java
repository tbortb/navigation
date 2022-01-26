package de.volkswagen.f73.evnavigator.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class Navigation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Navigation.class);
    private static final int ZOOM_DEFAULT = 14;
    private static final Coordinate LOCATION_DEFAULT = new Coordinate(52.421150, 10.744060);

    @FXML
    private AnchorPane navigationPane;
    @FXML
    private MapView map;

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private void initialize() {
        map.setZoom(ZOOM_DEFAULT);
        map.setCenter(LOCATION_DEFAULT);
        LOGGER.info("Initializing MapJFX map...");
        // don't block the view when initializing map
        Platform.runLater(() -> map.initialize());

    }

    public void show() {
        this.fxWeaver.getBean(MainWindow.class).setView(this.navigationPane, "Navigation");
    }

}
