package core.controllers;

import core.gui_elements.IncomingConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController extends GenericController {
    @FXML
    private ImageView logoView;
    @FXML
    private Label localIDLabel;
    @FXML
    private ComboBox<String> targetIDCombo;
    @FXML
    private Button connectBtn;
    @FXML
    private TabPane mainPanel;
    @FXML
    private VBox incomingBox;
    @FXML
    private VBox outgoingBox;

    private void updateConnectBtnText(String text){
        Platform.runLater(() -> connectBtn.setText(String.format("Connect to %s", text)));
    }

    public void setup(Stage stage){
        super.setup(stage);
        stage.setResizable(false);
        stage.show();

        int componentHeightTotal = 0;
        for(Node index : outgoingBox.getChildren()){
            if(!index.equals(logoView)){
                componentHeightTotal += index.getBoundsInParent().getHeight();
            }
        }
        componentHeightTotal += 50;

        mainPanel.setPrefSize(400, 365);
        outgoingBox.prefWidthProperty().bind(mainPanel.widthProperty());
        outgoingBox.prefHeightProperty().bind(mainPanel.heightProperty().subtract(30));

        logoView.setFitWidth(mainPanel.getPrefWidth() - 20);
        logoView.setFitHeight(mainPanel.getPrefHeight() - componentHeightTotal);

        targetIDCombo.prefWidthProperty().bind(outgoingBox.widthProperty().subtract(103));

        stage.hide();

        logoView.setImage(ico_up);
        localIDLabel.setText(String.format("This device's ID is %s", "127.0.0.1"));

        targetIDCombo.setOnKeyReleased(keyEvent -> {
            targetIDCombo.setValue(targetIDCombo.getEditor().getText());
            updateConnectBtnText(targetIDCombo.getValue());
        });
        targetIDCombo.setOnAction(actionEvent -> {
            targetIDCombo.setValue(targetIDCombo.getEditor().getText());
            updateConnectBtnText(targetIDCombo.getValue());
        });

        targetIDCombo.getItems().add("localhost");

        connectBtn.setOnAction(actionEvent -> {
            System.out.println("button interact");
            //changeStage(new ConnectionBroker().getController().getCurrentStage());
            incomingBox.getChildren().add(new IncomingConnection().getRoot());
        });

        stage.setTitle("Srocket Connection Dashboard");
    }
}
