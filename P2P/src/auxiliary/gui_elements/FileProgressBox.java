package auxiliary.gui_elements;

import auxiliary.socket_managers.TransferSocketManager;
import core.screens.ConnectedPrimary;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class FileProgressBox extends GenericGUIElement{
    public FileProgressBox(TransferSocketManager transferManager, String fileName, ConnectedPrimary parentScreen){
        super(GenericGUIElement.class.getResource("/ProgressBoxXML.fxml"));

        VBox rootBox = (VBox) root;
        Label fileLabel = (Label) rootBox.getChildren().get(0);
        fileLabel.setText(String.format("Downloading %s", fileName));
        ProgressBar progressBar = (ProgressBar) rootBox.getChildren().get(1);
        Button btnCancel = (Button) rootBox.getChildren().get(2);

        progressBar.prefWidthProperty().bind(fileLabel.widthProperty());
        btnCancel.prefWidthProperty().bind(progressBar.widthProperty());

        btnCancel.setOnAction(actionEvent -> {
            transferManager.interruptTransfer();
        });

        //Link progress bar to download progress
        new Thread(() -> {
            while (transferManager.getProgress() < 1){
                if(progressBar.getProgress() != transferManager.getProgress()){
                    progressBar.setProgress(transferManager.getProgress());
                }
            }
            Platform.runLater(() -> ((VBox) rootBox.getParent()).getChildren().remove(rootBox));
        }).start();
    }

     /* TODO: implement pop-out
    public void show(){
        Platform.runLater(() -> window.setTitle(String.format("Downloading %s", transferSocket.getDownloadInfo(DownloadInfo.FILENAME))));
        window.show();
    }
     */
}
