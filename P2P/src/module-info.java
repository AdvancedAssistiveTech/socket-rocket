module P2P {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    requires Rocket;

    opens core to javafx.graphics, javafx.fxml;

    exports core;
    exports core.controllers;
    opens core.controllers to javafx.fxml, javafx.graphics;
    exports core.screens;
    opens core.screens to javafx.fxml, javafx.graphics;
}