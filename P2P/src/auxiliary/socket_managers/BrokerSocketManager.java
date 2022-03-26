package auxiliary.socket_managers;

import java.net.Socket;

public class BrokerSocketManager extends GenericSocketManager{
    public BrokerSocketManager(Socket socket) {
        super(socket);
    }

    public boolean awaitAccept(){
        //while (inputStream.re)
        return false;
    }
}
