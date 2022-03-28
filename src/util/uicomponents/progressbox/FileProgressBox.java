package util.uicomponents.progressbox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.sockets.TransferSocketManager;

import java.io.IOException;

public class FileProgressBox {
    private final FPBController controller;

    public FileProgressBox(TransferSocketManager transferSocket, VBox parent){
        //GUI setup:
        Stage window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgressBoxXML.fxml"));

        try {
            window.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Link progress bar to download progress
        controller = loader.getController();
        controller.init(transferSocket, parent);
    }

     /* TODO: implement pop-out
    public void show(){
        Platform.runLater(() -> window.setTitle(String.format("Downloading %s", transferSocket.getDownloadInfo(DownloadInfo.FILENAME))));
        window.show();
    }
     */

    public VBox getMainBox(){
        return controller.getMainBox();
    }
}
