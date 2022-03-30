package auxiliary.socket_managers;

import auxiliary.data.DownloadableFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransferSocketManager extends GenericSocketManager{
    private double progress;
    private final AtomicBoolean transferActive;
    private final DownloadableFile targetFile;
    public TransferSocketManager(Socket socket, DownloadableFile targetFile) {
        super(socket);
        this.targetFile = targetFile;
        transferActive = new AtomicBoolean();
    }

    public void sendFile() throws IOException {
        FileInputStream FInputStream;
        try {
            FInputStream = new FileInputStream(targetFile.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int count;
        byte[] dataBuffer = new byte[1024];
        transferActive.set(true);

        progress = 0;

        while ((count = FInputStream.read(dataBuffer)) > 0 && transferActive.get()){
            try{
                outputStream.write(dataBuffer, 0, count);
            }
            catch (SocketException exception){
                System.out.println("transfer interrupted");
                break;
            }
            progress += count;
            System.out.printf("read %s%% of %sb%n", Math.round((progress / targetFile.getSize()) * 100), targetFile.getSize());
        }

        FInputStream.close();
    }

    public void receiveFile() throws IOException {
        FileOutputStream FOutputStream;
        try {
            FOutputStream = new FileOutputStream(targetFile.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int count;
        byte[] dataBuffer = new byte[1024];
        transferActive.set(true);

        progress = 0;

        while ((count = inputStream.read(dataBuffer)) > 0){
            if(!transferActive.get()){
                System.out.println("transfer interrupt");
                progress = targetFile.getSize();
                break;
            }
            FOutputStream.write(dataBuffer, 0, count);
            progress += count;
            System.out.printf("read %s%% of %sb%n", Math.round((progress / targetFile.getSize()) * 100), targetFile.getSize());
        }

        FOutputStream.close();
    }

    public double getProgress() {
        return progress / targetFile.getSize();
    }

    public void interruptTransfer(){
        transferActive.set(false);
    }
}
