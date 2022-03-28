package core.controllers;

import core.screens.GenericScreen;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class GenericController {
    protected Stage currentStage;
    protected GenericScreen controlledScreen;

    public static final Image ico_up = new Image(GenericController.class.getResourceAsStream("/ico_up.jpg"));

    public void setup(Stage currentStage, GenericScreen controlledScreen, String title){
        this.currentStage = currentStage;
        this.controlledScreen = controlledScreen;
        currentStage.getIcons().add(ico_up);

        currentStage.setTitle(title);

        currentStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            // handle window close with an exit() call to terminate the message listening thread as well
            System.exit(0);
        });
    }

    public void changeScreen(GenericScreen screen){
        currentStage.close();
        screen.getController().getCurrentStage().show();
    }

    public void setTitle(String title){
        currentStage.setTitle(title);
    }

    protected Stage getCurrentStage() { //limit stage object access to other controller objects
        return currentStage;
    }
    public GenericScreen getControlledScreen(){
        return controlledScreen;
    }
}
