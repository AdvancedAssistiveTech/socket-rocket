package core.controllers;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class GenericController {
    protected Stage currentStage;

    public static final javafx.scene.image.Image ico_up = new Image(GenericController.class.getResourceAsStream("/ico_up.jpg"));

    public void setup(Stage currentStage){
        this.currentStage = currentStage;
        currentStage.getIcons().add(ico_up);

        currentStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            // handle window close with an exit() call to terminate the message listening thread as well
            System.exit(0);
        });
    }

    protected void changeStage(Stage incomingStage){
        currentStage.close();
        incomingStage.show();
    }

    public void setTitle(String title){
        currentStage.setTitle(title);
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}