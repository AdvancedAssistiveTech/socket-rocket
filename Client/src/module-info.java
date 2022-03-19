module Client {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    requires Rocket;

    opens core to javafx.graphics, javafx.fxml;

    exports core;
}