module Server {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    requires Rocket;
    requires org.jetbrains.annotations;

    opens core to javafx.graphics, javafx.fxml;
    opens auxiliary to javafx.base;

    exports core;
}