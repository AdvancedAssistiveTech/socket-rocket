package core.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class BrokerController extends GenericController {
    @FXML
    TextFlow logFlow;

    public void addToFlow(String text, boolean newLine){
        text += newLine ? "\n" : "";
        Text candidateText = new Text(text);
        logFlow.getChildren().add(candidateText);
    }
}
