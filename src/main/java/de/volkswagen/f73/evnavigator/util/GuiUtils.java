package de.volkswagen.f73.evnavigator.util;

import de.volkswagen.f73.evnavigator.controller.IMenuController;
import de.volkswagen.f73.evnavigator.controller.MainWindow;
import javafx.scene.control.Alert;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

/**
 * Utility class for commonly used JavaFX functions
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public class GuiUtils {

    public static final String COORD_ERROR_TITLE = "Invalid coordinates";
    public static final String COORD_ERROR_BODY = "Please enter valid coordinates in the \"Decimal Degrees\" format.";

    private GuiUtils() {
    }

    public static void showError(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(body);

        alert.showAndWait();
    }

    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IMenuController> clazz) {
        setBackButtonNavigation(fxWeaver, clazz, true);
    }

    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IMenuController> clazz, Boolean isRoot) {
        fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(true);
        fxWeaver.getBean(MainWindow.class).getBackButton().setOnAction(e -> {
            fxWeaver.loadController(clazz).show();
            fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(!isRoot);
        });
    }
}
