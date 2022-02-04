package de.volkswagen.f73.evnavigator.controller;

import de.volkswagen.f73.evnavigator.service.StationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private Button backButton;

    private Node viewBeforeStandby;
    private boolean standByActive;
    private boolean backButtonVisibleBeforeStandby;

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

    /**
     * Starts a thread that updates the clock label on the top menu
     */
    private void startClock() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Runnable updateTime = () -> {
            while (true) {
                Date date = new Date();
                Platform.runLater(() -> this.clock.textProperty().set(dateFormat.format(date)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error("Clock thread sleep interrupted.");
                    Thread.currentThread().interrupt();
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
        if (!this.standByActive) {
            this.rootPane.setStyle("-fx-background-color: #000;");
            this.backButtonVisibleBeforeStandby = this.backButton.isVisible();
            this.backButton.setVisible(false);
            this.viewBeforeStandby = this.rootPane.getCenter();
            this.clock.setStyle("-fx-font-size: 24px;-fx-text-fill: white;-fx-pref-width: 250px");
            this.rootPane.setCenter(this.clock);
            this.menuTitle.setVisible(false);
            this.topMenu.setStyle("-fx-background-color: #000;");
            this.standByActive = true;
        } else {
            this.rootPane.setStyle("-fx-text-fill:  #94bdff;");
            this.menuTitle.setVisible(true);
            this.backButton.setVisible(this.backButtonVisibleBeforeStandby);
            this.rootPane.setCenter(this.viewBeforeStandby);
            this.clock.setStyle("");
            this.topMenu.getChildren().add(this.clock);
            this.topMenu.setStyle("-fx-background-color: #17223B;");
            this.standByActive = false;
        }

    }

    public Label getTitle() {
        return this.menuTitle;
    }

    public BorderPane getRootPane() {
        return this.rootPane;
    }

    public Button getBackButton() {
        return this.backButton;
    }
}
