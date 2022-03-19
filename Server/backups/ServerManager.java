package auxiliary;

import core.Controller;
import javafx.application.Platform;
import util.DownloadableFile;
import util.SocketManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {
    private Socket socket;
    private final Controller controller;
    private SocketManager socketManager;

    private final int port;

    private volatile boolean listenIncoming = true;

    public ServerManager(int port, Controller FXController) throws IOException {
        socketManager = null;
        controller = FXController;

        ServerSocket init = new ServerSocket(port);
        this.port = init.getLocalPort();
        new Thread(() -> {
            try {
                socket = init.accept();
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("disconnected");
                System.exit(0);
            }

            socketManager = new SocketManager(socket);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.getStage().setTitle("Connected");
                }
            });

            while (true){ // message listening thread
                if(listenIncoming){
                    String message = null;
                    try {
                        message = socketManager.receiveString();
                    } catch (IOException e) { //disconnected
                        e.printStackTrace();
                        System.out.println("disconnected");
                        System.exit(0);
                    }
                    String finalMessage = message;
                    if(finalMessage.startsWith("\\")){
                        switch (finalMessage.charAt(1)){
                            case 'd' -> {
                                String[] fileInfo = finalMessage.substring(2).split(",");
                                controller.addTableEntry(new DownloadableFile(
                                        fileInfo[0],
                                        fileInfo[1],
                                        Long.parseLong(fileInfo[2])
                                ));
                            }
                            case 'a' -> {
                                switch (finalMessage.substring(2)){
                                    case "tfBegin" -> {
                                        listenIncoming = false;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.newMessage(finalMessage, false);
                            }
                        });
                    }
                }
                else {
                    System.out.println("listen paused");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        controller.getStage().setTitle(String.format("Open on p:%s add:%s", getPort(), InetAddress.getLocalHost().getHostAddress()));
    }

    public void requestDownload(DownloadableFile target){
        socketManager.sendString(String.format("\\l%s", target.getPath())); // token to request file
        while (listenIncoming){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socketManager.sendString(String.format("\\a%s", "tfAccept"));
        socketManager.receiveFile(target);
        listenIncoming = true;
    }

    public int getPort(){
        return port;
    }

    public SocketManager getSocketManager() {
        return socketManager;
    }
}
