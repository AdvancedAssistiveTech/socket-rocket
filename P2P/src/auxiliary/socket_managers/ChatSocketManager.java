package auxiliary.socket_managers;

import auxiliary.data.Message;
import auxiliary.data.enums.Tags;

import java.io.IOException;
import java.net.Socket;

public class ChatSocketManager extends GenericSocketManager {
    public ChatSocketManager(Socket socket) {
        super(socket);
    }

    public Message receiveMessage() throws IOException {
        String utf = inputStream.readUTF();
        //process tags etc. from message
        if(!utf.startsWith("\\")){
            System.err.printf("Unknown string: %s (Not prefaced with \\)%n", utf);
            return null;
        }
        Message toReturn = new Message(utf);
        System.out.printf(
                "Received %s:%n%s%n",
                toReturn.getTag().getConsoleID(), utf
        );
        return toReturn;
    }

    public void sendMessage(Tags tag, String... args){
        Message toSend;
        if(args.length < tag.getRequiredArgsCount()){
            System.err.printf("Problem sending %s, required %s args, given %s. Send aborted", tag, tag.getRequiredArgsCount(), args.length);
        }
        else {
            if (args.length > tag.getRequiredArgsCount()){
                System.err.printf("Warning while sending %s. Required %s args, given %s.", tag, tag.getRequiredArgsCount(), args.length);
            }
            toSend = new Message(tag, args);
            try {
                outputStream.writeUTF(toSend.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("Written %s:%n", tag.getConsoleID());
            for (Object index : args){
                System.out.println(index.toString());
            }
            System.out.printf("Final string: %s%n", toSend);
        }
    }
}
