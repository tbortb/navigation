package de.volkswagen.f73.evnavigator.application;

import de.volkswagen.f73.evnavigator.EvnavigatorApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main JavaFX application starter class
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        this.context = new SpringApplicationBuilder()
                .sources(EvnavigatorApplication.class)
                .run(this.getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        this.context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}
