package core.screens;

import core.controllers.DashboardController;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionDashboard extends GenericScreen {
    public AtomicBoolean acceptingCandidates;
    private ServerSocket serverSocket;
    public ConnectionDashboard() {
        super(GenericScreen.class.getResource("/ConnectionDashboardXML.fxml"), "sRocket Connection Dashboard");

        acceptingCandidates = new AtomicBoolean(true);
        DashboardController controller = ((DashboardController) super.controller);

        try {
            serverSocket = new ServerSocket(2000);
            System.out.printf("Address: %s%nPort: %s%n", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            new Thread(() -> {
                /*
                TODO: add multiple incoming connections
                this list should be used to track all the incoming connections and provide the appropriate socket object
                when the user selects a given connection
                 */
                List<Socket> socketCandidates = new ArrayList<>();
                // update stage to broker object to reflect incoming connection TODO: make this only happen after connection is accepted by user in menu
                while (acceptingCandidates.get()){
                    Socket newCandidate;
                    try{
                        newCandidate = serverSocket.accept();
                        socketCandidates.add(newCandidate); //add all incoming connections to candidate list
                        controller.addIncoming(newCandidate);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                closeServerSocket();
            }).start();
        } catch (IOException e) {
            if(e instanceof BindException){
                System.err.println("Error while trying to open socket 2000 on local address. Is the application open somewhere else?");
                //System.exit(1);
                controller.setTitle("bindError");
            }
        }
    }

    public void closeServerSocket(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
