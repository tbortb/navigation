package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.application.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot starter class, main entry point of the application
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
