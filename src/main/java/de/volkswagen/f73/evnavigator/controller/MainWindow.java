package de.volkswagen.f73.evnavigator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
public class MainWindow {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

    @FXML
    private Label label;

    @Autowired
    private FxWeaver fxWeaver;

}
