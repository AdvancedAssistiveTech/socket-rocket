package core;

import core.screens.ConnectionDashboard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App extends Application {
    public static Stage stage;
    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        Application.launch(args);
    }
    @Override
    public void start(Stage stage) {
        App.stage = stage;
        new ConnectionDashboard();
        stage.show();
    }
}
