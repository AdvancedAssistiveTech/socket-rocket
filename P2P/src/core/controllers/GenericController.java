package core.controllers;

import core.App;
import core.screens.GenericScreen;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class GenericController {
    protected GenericScreen controlledScreen;

    public void setup(GenericScreen controlledScreen, String title) {
        this.controlledScreen = controlledScreen;
        controlledScreen.beforeLaunch(title);
    }

    public GenericScreen getControlledScreen(){
        return controlledScreen;
    }
}
