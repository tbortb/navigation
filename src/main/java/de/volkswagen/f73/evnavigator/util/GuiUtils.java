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

    /**
     * Creates an error modal and shows it to the user
     *
     * @param title the title of the error window
     * @param body  the error message shown in the body of the window
     */
    public static void showError(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(body);

        alert.showAndWait();
    }

    /**
     * Sets the back button functionality and its visibility after being clicked
     *
     * @param fxWeaver           FxWeaver bean to load controller with
     * @param clazz              controller Class (implementing IMenuController) that should be shown after click
     * @param isIntermediateMenu whether the controller to show is a root view or an intermediate view
     */
    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IMenuController> clazz,
                                               Boolean isIntermediateMenu) {
        fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(true);
        fxWeaver.getBean(MainWindow.class).getBackButton().setOnAction(e -> {
            fxWeaver.loadController(clazz).show();
            fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(isIntermediateMenu);
        });
    }

    /**
     * Sets the back button functionality, defaulting its visibility after click to false
     *
     * @param fxWeaver FxWeaver bean to load controller with
     * @param clazz    controller Class (implementing IMenuController) that should be shown after click
     */
    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IMenuController> clazz) {
        setBackButtonNavigation(fxWeaver, clazz, false);
    }
}
