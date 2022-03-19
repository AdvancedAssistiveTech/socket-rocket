package core;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.DownloadableFile;
import util.Message;
import util.enums.Tags;
import util.sockets.ChatSocketManager;
import util.sockets.TransferSocketManager;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class App extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter address: ");
        String address = input.next();
        int port;
        if(address.equals("\\f")){
            address = "localhost";
            port = Integer.parseInt(new Scanner(new File("server.connection")).next());
        }
        else {
            System.out.print("Enter port: ");
            port = input.nextInt();
        }

        Socket chatSocket = new Socket(address, port);

        ChatSocketManager chatManager = new ChatSocketManager(chatSocket);

        String finalAddress = address;
        new Thread(() -> {
            while (true) {
                try {
                    Message message = chatManager.receiveMessage();
                    switch (message.getTag()){ //get tag
                        case TEXT -> {
                            System.out.println(message.getContents());
                            System.out.println("default message");
                        }
                        case DOWNLOAD_REQUEST -> {
                            new Thread(() -> {
                                try  {
                                    TransferSocketManager fileManager = new TransferSocketManager(new Socket(finalAddress, Integer.parseInt(message.getContents(0))));
                                    fileManager.sendFile(new File(message.getContents(1)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                        case INSTRUCTION -> {
                        }
                    }
                } catch (IOException disconnect) {
                    System.out.println("Disconnected");
                    disconnect.printStackTrace();
                    System.exit(0);
                }
            }
        }).start();

        input = new Scanner(System.in);
        while (true) {
            System.out.println("Message: ");
            String message = input.nextLine();
            if (message.startsWith("\\")) {
                switch (message.substring(1)) {
                    case "i" -> {
                        DownloadableFile selected = new DownloadableFile(new FileChooser().showOpenDialog(null));
                        chatManager.send(Tags.DOWNLOADABLE_FILE, selected.getName(), selected.getPath(), selected.getSize());
                    }
                }
            } else {
                chatManager.send(Tags.TEXT, message);
                System.out.printf("SENT: %s%n", message);
            }
        }
    }
}
