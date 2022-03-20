package core;

import core.gui_elements.GenericGUIElement;
import javafx.stage.Stage;

import java.net.URL;

public abstract class GenericScreen extends GenericGUIElement {
    protected Stage stage;
    protected GenericController controller;

    public void beforeLaunch(){}

    public GenericScreen(URL fxmlResource){
        super(fxmlResource);
        this.stage = new Stage();

        stage.setScene(scene);
        controller = loader.getController();
        controller.setup(stage);
        beforeLaunch();
        stage.show();
    }

    public GenericController getController() {
        return controller;
    }
}
