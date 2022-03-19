package core;

import auxiliary.ServerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class App extends Application {
    static ServerManager serverManager;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServerCoreXML.fxml"));
        Scene scene = new Scene(loader.load());

        Controller FXController = loader.getController();
        primaryStage.setScene(scene);
        FXController.setup(primaryStage);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/ico_up.jpg")));

        primaryStage.show();

        serverManager = null;
        try {
            serverManager = new ServerManager(0, FXController);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(serverManager == null){
            System.exit(-1);
        }
        else {
            scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> { // handle window close with an exit() call to terminate the message listening thread as well
                System.exit(0);
            });
        }
    }
}
