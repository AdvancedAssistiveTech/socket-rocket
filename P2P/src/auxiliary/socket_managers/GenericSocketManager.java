package auxiliary.socket_managers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class GenericSocketManager {
    private final Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    public GenericSocketManager(Socket socket){
        this.socket = socket;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error while setting up socketManager");
            e.printStackTrace();
        }
    }
}
