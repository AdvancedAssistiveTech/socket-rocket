package util.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class SocketManager {
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public SocketManager(Socket socket){
        /*
        downloadInfo = new HashMap<>();
        downloadInfo.put(DownloadInfo.PROGRESS, "0");
         */
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
