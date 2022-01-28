package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.application.Platform;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller for the MainWindow view - the root view of this application.
 *
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

    /**
     * Seeds the database with CSV values, takes the path from application.properties.
     */
    @FXML
    private void initialize() {
        LOGGER.debug("Parsing default CSV stations...");
        this.stationService.csvToEmptyDB(this.stationsCsvLocation);
        this.fxWeaver.load(Menu.class).getController().show();
        this.startClock();
    }

    private void startClock() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Runnable updateTime = () -> {
            while(true) {
                Date date = new Date();
                Platform.runLater(() -> this.clock.textProperty().set(dateFormat.format(date)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread clockThread = new Thread(updateTime);
        clockThread.setDaemon(true);
        clockThread.start();
    }

    /**
     * Sets a view and changes the menu title.
     *
     * @param node  the Node to be set as the center view
     * @param title the title to be displayed in the top menu
     */
    public void setView(Node node, String title) {
        LOGGER.debug("Setting view to {} titled {}", node.getClass(), title);
        this.rootPane.setCenter(node);
        this.menuTitle.setText(title);
    }

    /**
     * Toggles the standby view, wired to the standby button.
     */
    @FXML
    private void toggleStandby() {
        this.fxWeaver.load(Menu.class).getController().show();
    }

    public Label getTitle() {
        return this.menuTitle;
    }

    public BorderPane getRootPane() {
        return this.rootPane;
    }
}
