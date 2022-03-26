package auxiliary.socket_managers;

import util.Message;
import util.enums.Tags;

import java.io.IOException;
import java.net.Socket;

public class BrokerSocketManager extends GenericSocketManager{
    public BrokerSocketManager(Socket socket) {
        super(socket);
    }

    public boolean awaitAccept(){
        String utf;
        while (true){
            try {
                utf = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            Message incoming = new Message(utf);
            if(incoming.getTag().equals(Tags.BROKER)){
                return true;
            }
            else {
                System.out.println(incoming);
            }
        }
    }

    public void sendAccept(){
        Message outgoing = new Message(Tags.BROKER, "");
        try {
            outputStream.writeUTF(outgoing.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
