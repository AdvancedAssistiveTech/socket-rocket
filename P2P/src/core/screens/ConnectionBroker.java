package core.screens;

import core.controllers.BrokerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionBroker extends GenericScreen {
    public ConnectionBroker(String address) {
        super(GenericScreen.class.getResource("/ConnectionBrokerXML.fxml"));

        BrokerController controller = ((BrokerController) this.controller);
        try {
            controller.addToFlow("Creating socket object", true);
            Socket socket = new Socket(address, 2000);
            controller.addToFlow("Socket connect complete", true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
