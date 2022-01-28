package de.volkswagen.f73.evnavigator.util;

import javafx.scene.control.Alert;

/**
 * Utility class for commonly used JavaFX functions
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
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
}
