package auxiliary.socket_managers;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class GenericSocketManager implements Closeable {
    protected final Socket socket;
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

    public static ServerSocket createServerSocket(int candidatePort){ //convenience method to try to create a serverSocket on candidatePort and then fall back to system default if unsuccessful
        ServerSocket toReturn = null;
        try {
            toReturn = new ServerSocket(candidatePort);
        } catch (IOException e) {
            if(e instanceof BindException){
                try {
                    toReturn = new ServerSocket(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return toReturn;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
