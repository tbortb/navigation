package de.volkswagen.f73.evnavigator.application;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class StageReadyEvent extends ApplicationEvent {

    public final transient Stage stage;

    public StageReadyEvent(Stage stage) {
        super(stage);
        this.stage = stage;
    }
}
