package auxiliary;

import core.Controller;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import util.DownloadableFile;
import util.enums.Tags;
import util.sockets.ChatSocketManager;
import util.sockets.TransferSocketManager;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerManager {
    private ChatSocketManager chatManager;
    private final Controller FXController;

    private final int port;

    public ServerManager(int port, @NotNull Controller FXController) throws IOException {
        chatManager = null;

        final ServerSocket init = new ServerSocket(port);
        this.port = init.getLocalPort();

        FXController.setTitle(String.format("Open on p:%s add:%s", getPort(), init.getInetAddress()));

        new Thread(() -> {
            try {
                chatManager = new ChatSocketManager(init.accept()); // first socket connection is for text transfer
            } catch (IOException e) {
                System.err.println(e);
                System.out.println("disconnected");
                System.exit(0);
            }

            Platform.runLater(() -> FXController.getStage().setTitle("Connected"));
            /*
            while (true){ // message listening thread
                Message message = null;
                try {
                    message = chatManager.receiveMessage();
                } catch (IOException e) { //disconnected
                    e.printStackTrace();
                    System.out.println("disconnected");
                    System.exit(0);
                }
                switch (message.getTag()){
                    case DOWNLOADABLE_FILE -> FXController.addTableEntry(new DownloadableFile(
                            message.getContents(0),
                            message.getContents(1),
                            Long.parseLong(message.getContents(2))
                    ));
                    case INSTRUCTION -> {
                        // handle ack messages
                    }
                    case TEXT -> {
                        Message finalMessage = message;
                        Platform.runLater(() -> FXController.newMessage(finalMessage.getContents(), false));
                    }
                }
            }

             */
        }).start();

        this.FXController = FXController;
    }

    public void requestDownload(DownloadableFile target){
        new Thread(() -> {
            TransferSocketManager fileManager;
            try (ServerSocket init = new ServerSocket(0)) { //connect separate socket for file transfer
                chatManager.send(Tags.DOWNLOAD_REQUEST, init.getLocalPort(), target.getPath());
                fileManager = new TransferSocketManager(init.accept());
                Platform.runLater(() -> {
                    FXController.setTitle(String.format("Beginning download of %s", target.getName()));
                    FXController.addFPB(fileManager);
                });
                fileManager.receiveFile(target);
                Platform.runLater(() -> {
                    FXController.setTitle(String.format("Finished download of %s", target.getName()));
                });
            } catch (IOException e) {
                System.out.println("Failure when establishing file connection");
            }
        }).start();
    }

    public int getPort(){
        return port;
    }

    public ChatSocketManager getChatManager() {
        return chatManager;
    }
}
