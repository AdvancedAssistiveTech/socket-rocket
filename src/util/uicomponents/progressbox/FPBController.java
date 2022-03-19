package util.uicomponents.progressbox;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import util.enums.DownloadInfo;
import util.sockets.TransferSocketManager;

public class FPBController {
    private TransferSocketManager tsm;

    @FXML
    private Button btnCancel;

    @FXML
    private VBox mainBox;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lblFile;

    @FXML
    private void clickCancel(){
        tsm.interruptTransfer();
    }

    void setProgress(double progress){
        progressBar.setProgress(progress);
    }

    public VBox getMainBox() {
        return mainBox;
    }

    public void init(TransferSocketManager tsm, VBox parent){
        this.tsm = tsm;
        lblFile.setText(String.format("Downloading %s", tsm.getDownloadInfo(DownloadInfo.FILENAME)));
        new Thread(() -> {
            while (tsm.getDownloadInfo(DownloadInfo.ACTIVE).equals("T")){
                setProgress(Double.parseDouble(tsm.getDownloadInfo(DownloadInfo.PROGRESS)));
            }
            Platform.runLater(() -> parent.getChildren().remove(mainBox));
        }).start();
    }
}
