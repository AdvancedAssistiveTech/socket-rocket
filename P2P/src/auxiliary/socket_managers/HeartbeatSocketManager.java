package auxiliary.socket_managers;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartbeatSocketManager extends GenericSocketManager {
    private final AtomicBoolean connected; //atomic boolean should be used to keep values consistent across threads

    public HeartbeatSocketManager(Socket socket) {
        super(socket);

        //ping connected device to ensure connection is up
        connected = new AtomicBoolean(true);
        new Thread(() -> {
            while (true){
                try{
                    if(socket.isClosed()){ // check if socket has been closed since stream.read() isn't always reliable
                        throw new IOException();
                    }
                    inputStream.read();
                } catch (IOException e) {
                    connected.set(false);
                    break;
                }
            }
        }).start();
    }

    public boolean isConnected() {
        return connected.get();
    }
}
