package auxiliary.gui_elements;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public abstract class GenericGUIElement {
    protected URL fxmlResource;
    protected FXMLLoader loader;
    protected Scene scene;

    public GenericGUIElement(URL fxmlResource){
        this.fxmlResource = fxmlResource;
        loader = new FXMLLoader(fxmlResource);
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node getRoot(){
        return loader.getRoot();
    }
}
