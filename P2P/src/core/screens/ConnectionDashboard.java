package core.screens;

import auxiliary.socket_managers.GenericSocketManager;
import core.controllers.DashboardController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionDashboard extends GenericScreen {
    private final AtomicBoolean acceptingCandidates;
    private final ServerSocket serverSocket;
    public ConnectionDashboard() {
        super(GenericScreen.class.getResource("/ConnectionDashboardXML.fxml"), "sRocket Connection Dashboard");

        acceptingCandidates = new AtomicBoolean(true);
        DashboardController controller = ((DashboardController) super.controller);

        serverSocket = GenericSocketManager.createServerSocket(2000);
        controller.setLocalInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());
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
            System.out.println("end of candidates");
            //closeServerSocket();
        }).start();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void stopAcceptingCandidates(){
        acceptingCandidates.set(false);
        try {
            new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort()); //create throwaway socket to free up last accept() call from candidates loop
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeLaunch(String title) {
        super.beforeLaunch(title);
        setResizable(false);
    }
}
