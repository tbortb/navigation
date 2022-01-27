package de.volkswagen.f73.evnavigator.util;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import javafx.scene.control.TextField;

public class MapUtils {

    private MapUtils() {}

    public static Marker buildMarker(double lat, double lon, MarkerImage image) {
        return new Marker(MapUtils.class.getResource(String.format("/images/markers/%s", image.getFilename())), -20, -70)
                .setPosition(new Coordinate(lat, lon))
                .setVisible(true);
    }

    public static void setInputFromCoordinate(TextField lat, TextField lon, Coordinate coord) {
        if (coord != null) {
            lat.setText(String.valueOf(coord.getLatitude()));
            lon.setText(String.valueOf(coord.getLongitude()));
        }
    }

    public enum MarkerImage {
        DESTINATION("destination.png"),
        ORIGIN("home.png"),
        PLACE("place.png"),
        STATION("station.png");

        private String filename;

        MarkerImage(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return this.filename;
        }
    }
}
