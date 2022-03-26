package auxiliary.gui_elements;

import core.controllers.DashboardController;
import core.screens.ConnectionDashboard;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.Socket;

public class IncomingConnection extends GenericGUIElement{
    public IncomingConnection(Socket incomingSocket, DashboardController dashboardController) {
        super(GenericGUIElement.class.getResource("/IncomingConnectionXML.fxml"));
        HBox root = loader.getRoot();
        TextFlow infoFlow = (TextFlow) root.getChildren().get(0);
        Button connectButton = (Button) root.getChildren().get(1), rejectButton = (Button) root.getChildren().get(2);

        infoFlow.getChildren().add(new Text(String.format("Incoming connection request from %s", incomingSocket.getInetAddress().getHostName())));

        connectButton.setOnAction(actionEvent -> {
            ConnectionDashboard parentDashboard = (ConnectionDashboard) dashboardController.getControlledScreen();
            parentDashboard.acceptingCandidates.set(false);
            parentDashboard.closeServerSocket();
            dashboardController.brokerIncomingConnection(incomingSocket);
        });
        rejectButton.setOnAction(actionEvent -> ((VBox) root.getParent()).getChildren().remove(root));
    }
}
