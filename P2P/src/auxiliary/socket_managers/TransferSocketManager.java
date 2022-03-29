package auxiliary.socket_managers;

import auxiliary.data.DownloadableFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TransferSocketManager extends GenericSocketManager{
    private double progress;
    public TransferSocketManager(Socket socket) {
        super(socket);
    }

    public void sendFile(DownloadableFile toSend) throws IOException {
        FileInputStream FInputStream;
        try {
            FInputStream = new FileInputStream(toSend.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int count;
        byte[] dataBuffer = new byte[1024];

        progress = 0;

        while ((count = FInputStream.read(dataBuffer)) > 0){
            outputStream.write(dataBuffer, 0, count);
            progress += count;
            System.out.printf("read %s%% of %sb%n", Math.round((progress / toSend.getSize()) * 100), toSend.getSize());
        }

        FInputStream.close();
    }

    public void receiveFile(DownloadableFile toReceive) throws IOException {
        FileOutputStream FOutputStream;
        try {
            FOutputStream = new FileOutputStream(toReceive.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int count;
        byte[] dataBuffer = new byte[1024];

        progress = 0;

        while ((count = inputStream.read(dataBuffer)) > 0){
            FOutputStream.write(dataBuffer, 0, count);
            progress += count;
            System.out.printf("read %s%% of %sb%n", Math.round((progress / toReceive.getSize()) * 100), toReceive.getSize());
        }

        FOutputStream.close();
    }

    public double getProgress() {
        return progress;
    }
}
