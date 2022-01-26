package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.application.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvnavigatorApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}

}
