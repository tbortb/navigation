package de.volkswagen.f73.evnavigator.application;

import de.volkswagen.f73.evnavigator.controller.MainWindow;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Starter class for the primary stage after initialization is finished.
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.stage;
        Scene scene = new Scene(this.fxWeaver.loadView(MainWindow.class));
        Font.loadFont(this.getClass().getResource("/fonts/Roboto-Bold.ttf").toExternalForm(), 10);
        Font.loadFont(this.getClass().getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 10);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        stage.setResizable(false);
        stage.setTitle("EV Navigator");
        stage.setScene(scene);
        stage.show();
    }
}
