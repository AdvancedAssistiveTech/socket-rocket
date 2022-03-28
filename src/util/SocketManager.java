package util;

import util.enums.DownloadInfo;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class SocketManager {
    private HashMap<DownloadInfo, String> downloadInfo;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private final PrefaceHandler textHandler;

    public SocketManager(Socket socket){
        downloadInfo = new HashMap<>();
        downloadInfo.put(DownloadInfo.PROGRESS, "0");
        System.out.printf("set %s%n", downloadInfo.get(DownloadInfo.PROGRESS));
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        textHandler = new PrefaceHandler(outputStream);
        downloadInfo = null;
    }

    /*
    public Message receiveMessage() throws IOException {
        return PrefaceHandler.processReceived(inputStream.readUTF());
    }

     */

    public TransferManager sendFile(File target){
        try {
            return new TransferManager(target, outputStream);
        } catch (IOException e) {
            System.err.printf("Problem beginning transfer of %s%n", target.getName());
            return null;
        }
    }

    public void receiveFile(DownloadableFile target){
        try (
            FileOutputStream fos = new FileOutputStream(target.getName())
        ) {
            int count, total = 0;
            byte[] buffer = new byte[(int) target.getSize()];
            downloadInfo = new HashMap<>();
            downloadInfo.put(DownloadInfo.FILENAME, target.getName());
            downloadInfo.put(DownloadInfo.PROGRESS, "0");
            while ((count = inputStream.read(buffer)) > 0){
                fos.write(buffer, 0, count);
                total += count;
                downloadInfo.put(DownloadInfo.PROGRESS, (total / (target.getSize() + 0d)) + "");
                System.out.printf("read %sb of %s%n", total, target.getSize());
                if(total == target.getSize()){
                    break;
                }
            }
            System.out.println("receive complete");
        }
        catch (IOException fileEx) {
            System.err.println("Error receiving file:");
            fileEx.printStackTrace();
        }
    }

    public PrefaceHandler getTextHandler() {
        return textHandler;
    }

    public String getDownloadInfo(DownloadInfo key){
        if(downloadInfo == null){
            return null;
        }
        return downloadInfo.get(key);
    }
}
