module Rocket {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens util to javafx.graphics, javafx.fxml;

    exports util;
    exports util.enums;
    exports util.uicomponents.progressbox;
    opens util.uicomponents.progressbox to javafx.fxml, javafx.graphics;
    exports util.sockets;
    opens util.sockets to javafx.fxml, javafx.graphics;
}