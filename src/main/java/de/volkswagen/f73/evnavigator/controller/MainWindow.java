package de.volkswagen.f73.evnavigator.controller;

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

    @FXML
    private void initialize() {
        fxWeaver.load(Menu.class).getController().show();
    }

    public void setView(Node node, String title) {
        LOGGER.info("Setting view to {} titled {}", node.getClass(), title);
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
