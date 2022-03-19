package util;

import java.io.*;

public class TransferManager {
    private volatile int transferState; //0: inactive, 1: active, 2: complete, -1: interrupted

    public TransferManager(File target, DataOutputStream outputStream) throws IOException {
        FileInputStream fis = new FileInputStream(target.getAbsolutePath());

        int count;
        byte[] buffer = new byte[(int) target.length()];

        transferState = 1;

        while ((count = fis.read(buffer)) > 0 && transferState == 1){
            outputStream.write(buffer, 0, count);
            System.out.printf("read %sb of %s%n", count, target.length());
        }
        if(transferState != -1){
            transferState = 2;
        }
        else {
            System.err.println("Interrupted");
        }

        fis.close();
    }

    public void cancel(){
        transferState = -1;
    }
}
