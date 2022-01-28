package de.volkswagen.f73.evnavigator.application;

import de.volkswagen.f73.evnavigator.controller.MainWindow;
import javafx.scene.Scene;
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
        stage.setScene(scene);
        stage.show();
    }
}
