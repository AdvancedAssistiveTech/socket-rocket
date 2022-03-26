package core.screens;

import core.controllers.DashboardController;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDashboard extends GenericScreen {
    public ConnectionDashboard() {
        super(GenericScreen.class.getResource("/ConnectionDashboardXML.fxml"));

        DashboardController controller = ((DashboardController) super.controller);

        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            System.out.printf("Address: %s%nPort: %s%n", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            new Thread(() -> {
                List<Socket> socketCandidates = new ArrayList<>();
                // update stage to broker object to reflect incoming connection TODO: make this only happen after connection is accepted by user in menu
                while (true){
                    Socket newCandidate;
                    try{
                        newCandidate = serverSocket.accept();
                        socketCandidates.add(newCandidate); //add all incoming connections to candidate list
                        controller.addIncoming(newCandidate, serverSocket);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            if(e instanceof BindException){
                System.err.println("Error while trying to open socket 2000 on local address. Is the application open somewhere else?");
                //System.exit(1);
                controller.setTitle("bindError");
            }
        }
    }
}
