package util;

import util.enums.Tags;

import java.io.DataOutputStream;
import java.io.IOException;

public class PrefaceHandler {
    DataOutputStream outputStream;

    public PrefaceHandler(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void send(Tags tag, Object... args){
        StringBuilder toSend;
        if(args.length < tag.getRequiredArgsCount()){
            System.err.printf("Problem sending %s, required %s args, given %s. Send aborted", tag, tag.getRequiredArgsCount(), args.length);
        }
        else {
            if (args.length > tag.getRequiredArgsCount()){
                System.err.printf("Warning while sending %s. Required %s args, given %s.", tag, tag.getRequiredArgsCount(), args.length);
            }
            toSend = new StringBuilder("\\" + tag.getValue() + args[0] + ",");
            for(int index = 1; index < args.length; index++){
                toSend.append(args[index]).append(",");
            }
            toSend = new StringBuilder(toSend.substring(0, toSend.length() - 1)); // truncate last comma
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

    /*
    static Message processReceived(String utf){
        if(!utf.startsWith("\\")){
            System.err.printf("Unknown string: %s (Not prefaced with \\)%n", utf);
            return null;
        }
        Message toReturn = new Message(utf.substring(2), Tags.fromChar(utf.charAt(1)));
        System.out.printf(
                "Received %s:%n%s%n",
                toReturn.getTag().getConsoleID(), utf
        );
        return toReturn;
    }

     */
}
