package core;

import core.dashboard.GenericController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class GenericScreen {
    protected URL fxmlResource;
    protected FXMLLoader loader;
    protected Stage stage;
    protected GenericController controller;

    public void beforeLaunch(){}

    public GenericScreen(URL fxmlResource){
        this.fxmlResource = fxmlResource;
        this.stage = new Stage();
        loader = new FXMLLoader(fxmlResource);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
