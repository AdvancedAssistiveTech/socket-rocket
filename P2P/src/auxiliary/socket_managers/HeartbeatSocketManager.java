package auxiliary.socket_managers;

import core.controllers.BrokerController;
import core.controllers.GenericController;
import core.screens.ConnectionBroker;
import core.screens.ConnectionDashboard;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class HeartbeatSocketManager extends GenericSocketManager {
    private GenericController currentController;

    public HeartbeatSocketManager(Socket socket) {
        super(socket);

        //ping connected device to ensure connection is up
        new Thread(() -> {
            while (true){
                try{
                    if(socket.isClosed()){ // check if socket has been closed since stream.read() isn't always reliable
                        throw new IOException();
                    }
                    inputStream.read();
                } catch (IOException e) {
                    System.err.println("Socket disconnect");
                    if (currentController instanceof BrokerController){
                        try {
                            ((ConnectionBroker) currentController.getControlledScreen()).close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    Platform.runLater(() -> currentController.changeScreen(new ConnectionDashboard()));
                    break;
                }
            }
        }).start();
    }

    public void setCurrentController(GenericController currentController) {
        this.currentController = currentController;
    }
}
