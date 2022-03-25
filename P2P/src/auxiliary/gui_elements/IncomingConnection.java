package auxiliary.gui_elements;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IncomingConnection extends GenericGUIElement{
    public IncomingConnection() {
        super(GenericGUIElement.class.getResource("/IncomingConnectionXML.fxml"));
        HBox root = loader.getRoot();
        Button rejectButton = (Button) root.getChildren().get(2);
        rejectButton.setOnAction(actionEvent -> ((VBox) root.getParent()).getChildren().remove(root));
    }
}
