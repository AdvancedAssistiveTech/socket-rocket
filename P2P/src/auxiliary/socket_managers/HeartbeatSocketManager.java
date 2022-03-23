package auxiliary.socket_managers;

import java.io.IOException;
import java.net.Socket;

public class HeartbeatSocketManager extends GenericSocketManager {
    public HeartbeatSocketManager(Socket socket) {
        super(socket);

        //ping connected device to ensure connection is up
        new Thread(() -> {
            while (true){
                try{
                    inputStream.read();
                } catch (IOException e) {
                    System.err.println("Socket disconnect");
                    System.exit(1);
                }
            }
        }).start();
    }
}
