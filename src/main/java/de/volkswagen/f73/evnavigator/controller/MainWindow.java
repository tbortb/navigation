package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
@FxmlView
public class MainWindow {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);


    @Value("${stations.default.path}")
    private String stationsCsvLocation;

    @FXML
    private BorderPane rootPane;
    @FXML
    private HBox topMenu;
    @FXML
    private Label menuTitle;
    @FXML
    private Label clock;
    @FXML
    private Button standbyButton;
    @FXML
    private Pane contentPane;

    @Autowired
    private FxWeaver fxWeaver;
    @Autowired
    private StationService stationService;

    @FXML
    private void initialize() {
        LOGGER.debug("Parsing default CSV stations...");
        this.stationService.csvToDB(this.stationsCsvLocation);
        fxWeaver.load(Menu.class).getController().show();
    }

    public void setView(Node node, String title) {
        LOGGER.debug("Setting view to {} titled {}", node.getClass(), title);
        this.rootPane.setCenter(node);
        this.menuTitle.setText(title);
    }

    @FXML
    private void toggleStandby() {
        fxWeaver.load(Menu.class).getController().show();
    }

    public Label getTitle() {
        return this.menuTitle;
    }

}
