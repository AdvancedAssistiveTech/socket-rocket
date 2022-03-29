package core.controllers;

import auxiliary.gui_elements.IncomingConnection;
import core.screens.ConnectionBroker;
import core.screens.ConnectionDashboard;
import core.screens.GenericScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.Socket;

public class DashboardController extends GenericController {
    @FXML
    private ImageView logoView;
    @FXML
    private Label localIDLabel;
    @FXML
    private ComboBox<String> targetIDCombo;
    @FXML
    private ComboBox<Integer> targetPortCombo;
    @FXML
    private Button connectBtn;
    @FXML
    private TabPane mainPanel;
    @FXML
    private VBox incomingBox;
    @FXML
    private VBox outgoingBox;
    @FXML
    private Label lblTargetID;

    private void updateDashboard(ComboBox<String> comboBox){
        comboBox.setValue(comboBox.getEditor().getText());
        updateConnectButton();
    }
    private void updateConnectButton(){
        connectBtn.setText(String.format("Connect to %s on %s", targetIDCombo.getValue(), targetPortCombo.getValue()));
    }

    public void addIncoming(Socket incomingConnection){
        Platform.runLater(() -> incomingBox.getChildren().add(new IncomingConnection(incomingConnection, (ConnectionDashboard) controlledScreen).getRoot()));
    }
    @Override
    public void setup(GenericScreen controlledScreen, String title){
        super.setup(controlledScreen, title);

        logoView.setImage(GenericScreen.ico_up);
        //localIDLabel.setText(String.format("This device's ID is %s on %s", "127.0.0.1", ""));

        targetIDCombo.setOnKeyReleased(keyEvent -> {
            /*
            synchroniseComboValueAndText(targetIDCombo);
            updateConnectBtnText(targetIDCombo.getValue());
             */
            updateDashboard(targetIDCombo);
        });
        targetIDCombo.setOnAction(actionEvent -> {
            updateDashboard(targetIDCombo);
        });
        targetPortCombo.setOnKeyReleased(keyEvent -> {
            targetPortCombo.setValue(Integer.parseInt(targetPortCombo.getEditor().getText()));
            updateConnectButton();
        });
        targetPortCombo.setOnAction(actionEvent -> {
            targetPortCombo.setValue(Integer.parseInt(targetPortCombo.getEditor().getText()));
            updateConnectButton();
        });

        targetIDCombo.getItems().add("localhost");
        targetPortCombo.getItems().add(2000);

        targetIDCombo.getSelectionModel().selectFirst();
        targetPortCombo.getSelectionModel().selectFirst();
        updateConnectButton();

        connectBtn.setOnAction(actionEvent -> new ConnectionBroker(targetIDCombo.getValue(), targetPortCombo.getValue()));
    }

    public void setLocalInfo(String ID, int port){
        localIDLabel.setText(String.format("This device's ID is %s on %s", ID, port));
    }
}
