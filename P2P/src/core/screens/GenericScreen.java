package core.screens;

import auxiliary.gui_elements.GenericGUIElement;
import core.App;
import core.controllers.GenericController;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public abstract class GenericScreen extends GenericGUIElement {
    protected GenericController controller;
    private static Stage stage;

    public static final Image ico_up = new Image(GenericScreen.class.getResourceAsStream("/ico_up.jpg"));

    public void beforeLaunch(String title){
        stage.getIcons().add(ico_up);
        setResizable(true);

        stage.setTitle(title);

        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            // handle window close with an exit() call to terminate the message listening thread as well
            System.exit(0);
        });
    }

    public GenericScreen(URL fxmlResource, String title){
        super(fxmlResource);

        try{
            stage.setScene(scene);
        }
        catch (IllegalStateException stateException){
            Platform.runLater(() -> stage.setScene(scene));
        }

        controller = loader.getController();
        controller.setup(this, title);
    }

    public GenericController getController() {
        return controller;
    }

    public static void setStage(Stage stage) {
        GenericScreen.stage = stage;
    }

    protected void setResizable(boolean resizable){
        stage.setResizable(resizable);
    }

    protected void setTitle(String title){
        stage.setTitle(title);
    }
}
