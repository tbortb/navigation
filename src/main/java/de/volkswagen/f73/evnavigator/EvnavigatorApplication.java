package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.application.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Utility class for generic actions within a controller that uses mapjfx.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootApplication
public class EvnavigatorApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}

}
