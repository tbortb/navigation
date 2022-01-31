package de.volkswagen.f73.evnavigator.util;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import javafx.scene.control.TextField;

/**
 * Utility class for generic actions within a controller that uses mapjfx.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class MapUtils {

    private MapUtils() {
    }

    /**
     * Builds a standardized mapjfx marker via argument settings
     *
     * @param lat       latitude of the marker position
     * @param lon       longitude of the marker position
     * @param image     MarkerImage enum of the desired marker icon
     * @param isVisible visibility upon creation
     * @return Marker object
     */
    public static Marker buildMarker(double lat, double lon, MarkerImage image, boolean isVisible) {
        return new Marker(MapUtils.class.getResource(String.format("/images/markers/%s", image.getFilename())), -20, -70)
                .setPosition(new Coordinate(lat, lon))
                .setVisible(isVisible);
    }

    /**
     * Builds a standardized mapjfx marker via argument settings with default visibility set to true
     *
     * @param lat   latitude of the marker position
     * @param lon   longitude of the marker position
     * @param image MarkerImage enum of the desired marker icon
     * @return Marker object
     */
    public static Marker buildMarker(double lat, double lon, MarkerImage image) {
        return buildMarker(lat, lon, image, true);
    }

    /**
     * Fills two textfields with coordinates
     *
     * @param lat   TextField of the latitude value
     * @param lon   TextField of the longitude value
     * @param coord Coordinate object to parse
     */
    public static void setInputFromCoordinate(TextField lat, TextField lon, Coordinate coord) {
        if (coord != null) {
            lat.setText(String.valueOf(coord.getLatitude()));
            lon.setText(String.valueOf(coord.getLongitude()));
        }
    }

    /**
     * Marker images with their respective filenames
     */
    public enum MarkerImage {
        DESTINATION("destination.png"),
        ORIGIN("home.png"),
        PLACE("place.png"),
        STATION("station.png");

        private final String filename;

        MarkerImage(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return this.filename;
        }
    }
}
