package core.screens;

import auxiliary.data.DownloadableFile;
import auxiliary.data.Message;
import auxiliary.data.enums.Tags;
import auxiliary.socket_managers.ChatSocketManager;
import auxiliary.socket_managers.HeartbeatSocketManager;
import auxiliary.socket_managers.TransferSocketManager;
import core.controllers.PrimaryController;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectedPrimary extends GenericScreen{
    private final ChatSocketManager chatManager;
    private final ServerSocket binder;
    private final String remoteAddress;

    private final List<TransferSocketManager> transfers = new ArrayList<>();

    private int remotePort;
    public ConnectedPrimary(HeartbeatSocketManager heartbeatManager, ChatSocketManager chatManager, ServerSocket binder, String remoteAddress) {
        super(GenericScreen.class.getResource("/ConnectedPrimaryXML.fxml"), "sRocket");
        PrimaryController controller = (PrimaryController) this.controller;

        this.chatManager = chatManager;
        this.binder = binder;
        this.remoteAddress = remoteAddress;

        new Thread(() -> { // track incoming messages
            chatManager.sendMessage(Tags.PORT_INFO, binder.getLocalPort() + "");
            while (heartbeatManager.isConnected()){ // message listening thread
                Message message;
                try {
                    message = chatManager.receiveMessage();
                } catch (IOException e) {
                    System.err.println("I/O error while receiving message");
                    continue; //bypass exception when receiving message since heartbeat socket will close the app if there's an ongoing issue
                }
                switch (message.getTag()){
                    case DOWNLOADABLE_FILE -> controller.addTableEntry(new DownloadableFile(message));
                    case DOWNLOAD_REQUEST -> {
                        new Thread(() -> {
                            try{
                                sendFile(new File(message.getContents()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    case PORT_INFO -> {
                        remotePort = Integer.parseInt(message.getContents());
                        System.out.printf("Connected to %s. Transfer connections are now to remote port %s%n", remoteAddress, remotePort);
                    }
                    case TEXT -> Platform.runLater(() -> controller.addMessage(message.getContents(), false));
                }
            }
            // handle lost connection
            System.err.println("Heartbeat reports lost connection");
            try{
                heartbeatManager.close();
                chatManager.close();
                for(TransferSocketManager index : transfers){
                    index.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(ConnectionDashboard::new);
        }).start();
    }

    public void sendDownloadMessage(DownloadableFile toRequest){
        chatManager.sendMessage(Tags.DOWNLOAD_REQUEST, toRequest.getPath());
        new Thread(() -> {
            try {
                receiveFile(toRequest, new Socket(remoteAddress, remotePort));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendUploadMessage(DownloadableFile toSend){
        chatManager.sendMessage(Tags.DOWNLOADABLE_FILE, toSend.getName(), toSend.getPath(), toSend.getSize() + "");
    }
    public void sendTextMessage(String messageText){
        chatManager.sendMessage(Tags.TEXT, messageText);
    }

    private void sendFile(File file) throws IOException {
        DownloadableFile toSend = new DownloadableFile(file);
        TransferSocketManager transferManager = new TransferSocketManager(binder.accept());

        transferManager.sendFile(toSend);
    }

    private void receiveFile(DownloadableFile toReceive, Socket transferSocket) throws IOException {
        TransferSocketManager transferManager = new TransferSocketManager(transferSocket);
        transfers.add(transferManager);
        transferManager.receiveFile(toReceive);
        transfers.remove(transferManager);
    }
}
