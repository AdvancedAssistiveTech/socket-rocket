package core.screens;

import auxiliary.gui_elements.GenericGUIElement;
import core.App;
import core.controllers.GenericController;
import javafx.application.Platform;

import java.net.URL;

public abstract class GenericScreen extends GenericGUIElement {
    protected GenericController controller;

    public void beforeLaunch(){}

    public GenericScreen(URL fxmlResource, String title){
        super(fxmlResource);
        controller = loader.getController();
        try{
            App.stage.setScene(scene);
        }
        catch (IllegalStateException stateException){
            Platform.runLater(() -> App.stage.setScene(scene));
        }
        controller.setup(this, title);
    }

    public GenericController getController() {
        return controller;
    }
}
