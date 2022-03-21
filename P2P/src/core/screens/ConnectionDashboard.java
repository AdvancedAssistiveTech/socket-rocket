package core.screens;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class ConnectionDashboard extends GenericScreen {
    public ConnectionDashboard() {
        super(GenericScreen.class.getResource("/ConnectionDashboardXML.fxml"));
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            System.out.printf("Address: %s%nPort: %s%n", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            new Thread(() -> {
                try {
                    serverSocket.accept();
                    System.out.println("acc thru");
                } catch (IOException e) {
                    e.printStackTrace();
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
