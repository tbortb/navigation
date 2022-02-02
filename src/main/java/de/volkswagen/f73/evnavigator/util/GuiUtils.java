package de.volkswagen.f73.evnavigator.util;

import de.volkswagen.f73.evnavigator.controller.IController;
import de.volkswagen.f73.evnavigator.controller.MainWindow;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxWeaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.Duration;


/**
 * Utility class for commonly used user interface functions
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public class GuiUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GuiUtils.class);
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
     * @param clazz              controller Class (implementing IController) that should be shown after click
     * @param isIntermediateMenu whether the controller to show is a root view or an intermediate view
     */
    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IController> clazz,
                                               Boolean isIntermediateMenu) {
        fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(true);
        fxWeaver.getBean(MainWindow.class).getBackButton().setOnAction(e -> {
            fxWeaver.getBean(clazz).show();
            fxWeaver.getBean(MainWindow.class).getBackButton().setVisible(isIntermediateMenu);
        });
    }

    /**
     * Sets the back button functionality, defaulting its visibility after click to false
     *
     * @param fxWeaver FxWeaver bean to load controller with
     * @param clazz    controller Class (implementing IController) that should be shown after click
     */
    public static void setBackButtonNavigation(FxWeaver fxWeaver, Class<? extends IController> clazz) {
        setBackButtonNavigation(fxWeaver, clazz, false);
    }

    /**
     * Returns a human-readable String of a Duration value. Parses hours and minutes and only returns relevant
     * values.
     *
     * @param duration Duration object
     * @return String with minutes and, optionally, hours
     */
    public static String formatDurationToTimeString(Duration duration) {

        StringBuilder sb = new StringBuilder();

        if (duration.toHours() > 0) {
            sb.append(duration.toHours());
            if (duration.toHours() > 1) {
                sb.append(" hours");
            } else {
                sb.append(" hour");
            }
        }

        int minutesAfterHours = (int) duration.toMinutes() - ((int) duration.toHours() * 60);

        if (duration.toHours() > 0 && minutesAfterHours > 0) {
            sb.append(", ");
        }

        if (minutesAfterHours > 0) {
            if (minutesAfterHours > 1) {
                sb.append(minutesAfterHours);
                sb.append(" minutes");
            } else {
                sb.append(minutesAfterHours);
                sb.append(" minute");
            }
        }

        if (sb.length() == 0) {
            sb.append("under 1 minute");
        }

        return sb.toString();
    }

    /**
     * Returns a human readable String of a distance. Outputs meters for values under 1000 meters and
     * kilometers for values above.
     *
     * @param distance distance
     * @param isKm     if unit is in kilometers (meters if false)
     * @return formatted String
     */
    public static String distanceToString(double distance, boolean isKm) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (!isKm && distance < 1000) {
            return df.format(distance) + " meters";
        } else if (!isKm && distance >= 1000) {
            return df.format(distance / 1000) + " km";
        } else if (isKm && distance < 1) {
            return df.format(distance * 1000) + " meters";
        } else if (isKm && distance >= 1) {
            return df.format(distance) + " km";
        }

        return "";
    }

    public static Integer tfToIntegerOrNull(TextField intTf) {
        try {
            return Integer.parseInt(intTf.getText());
        } catch (NumberFormatException e) {
            LOGGER.info("Could not parse {} ", intTf.getText());
            return null;
        }
    }

    public static Double tfToDoubleOrNull(TextField doubleTf) {
        try {
            return Double.parseDouble(doubleTf.getText());
        } catch (NumberFormatException e) {
            LOGGER.info("Could not parse {} ", doubleTf.getText());
            return null;
        }
    }

    public static void numberOrEmptyToTextField(TextField tf, Number number) {
        tf.setText(number == null ? "" : String.valueOf(number));
    }

    public static Boolean nullToFalse(Boolean checkBool) {
        return checkBool != null && checkBool;
    }
}
