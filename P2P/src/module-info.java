module P2P {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    requires Rocket;

    opens core to javafx.graphics, javafx.fxml;

    exports core;
    exports core.dashboard;
    opens core.dashboard to javafx.fxml, javafx.graphics;
    exports core.broker;
    opens core.broker to javafx.fxml, javafx.graphics;
}