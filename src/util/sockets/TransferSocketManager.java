package util.sockets;

import util.DownloadableFile;
import util.TransferManager;
import util.enums.DownloadInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class TransferSocketManager extends SocketManager{
    private HashMap<DownloadInfo, String> downloadInfo;
    private final Socket socket;

    public TransferSocketManager(Socket socket) {
        super(socket);
        this.socket = socket;
        downloadInfo = null;
    }

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
            downloadInfo.put(DownloadInfo.ACTIVE, "T");
            try {
                while ((count = inputStream.read(buffer)) > 0 && downloadInfo.get(DownloadInfo.ACTIVE).equals("T")){
                    fos.write(buffer, 0, count);
                    total += count;
                    downloadInfo.put(DownloadInfo.PROGRESS, (total / (target.getSize() + 0d)) + "");
                    System.out.printf("read %sb of %s%n", total, target.getSize());
                    if(total == target.getSize()){
                        break;
                    }
                }
            }
            catch (SocketException interrupt){
                if(downloadInfo.get(DownloadInfo.ACTIVE).equals("T")){
                    System.err.println("Error while downloading");
                    interrupt.printStackTrace();
                }
                else {
                    System.out.println("Transfer cancelled");
                }
            }

            downloadInfo.put(DownloadInfo.ACTIVE, "F");
            System.out.println("receive complete");
        }
        catch (IOException fileEx) {
            System.err.println("Error receiving file:");
            fileEx.printStackTrace();
        }
    }

    public String getDownloadInfo(DownloadInfo key){
        if(downloadInfo == null){
            return null;
        }
        return downloadInfo.get(key);
    }

    public void interruptTransfer(){
        downloadInfo.put(DownloadInfo.ACTIVE, "F");
        try {
            socket.close(); // more needed?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
