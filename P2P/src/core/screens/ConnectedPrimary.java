package core.screens;

import auxiliary.data.DownloadableFile;
import auxiliary.data.Message;
import auxiliary.data.enums.Tags;
import auxiliary.socket_managers.ChatSocketManager;
import auxiliary.socket_managers.HeartbeatSocketManager;
import core.controllers.PrimaryController;
import javafx.application.Platform;

import java.io.IOException;

public class ConnectedPrimary extends GenericScreen{
    private HeartbeatSocketManager heartbeatManager;
    private ChatSocketManager chatManager;
    public ConnectedPrimary(HeartbeatSocketManager heartbeatManager, ChatSocketManager chatManager) {
        super(GenericScreen.class.getResource("/ConnectedPrimaryXML.fxml"), "sRocket");
        PrimaryController controller = (PrimaryController) this.controller;

        this.heartbeatManager = heartbeatManager;
        this.chatManager = chatManager;

        new Thread(() -> { // track heartbeat socket status
            while (heartbeatManager.isConnected());
            System.err.println("Heartbeat reports lost connection");
            Platform.runLater(ConnectionDashboard::new);
        }).start();

        new Thread(() -> { // track incoming messages
            while (true){ // message listening thread
                Message message;
                try {
                    message = chatManager.receiveMessage();
                } catch (IOException e) {
                    //System.err.println("I/O error while receiving message");
                    continue;
                }
                switch (message.getTag()){
                    case DOWNLOADABLE_FILE -> controller.addTableEntry(new DownloadableFile(message));
                    case INSTRUCTION -> {
                        // handle ack messages
                    }
                    case TEXT -> {
                        Platform.runLater(() -> controller.addMessage(message.getContents(), false));
                    }
                }
            }
        }).start();
    }

    public void sendUploadMessage(DownloadableFile toSend){
        chatManager.sendMessage(Tags.DOWNLOADABLE_FILE, toSend.getName(), toSend.getPath(), toSend.getSize() + "");
    }
    public void sendTextMessage(String messageText){
        chatManager.sendMessage(Tags.TEXT, messageText);
    }
}
