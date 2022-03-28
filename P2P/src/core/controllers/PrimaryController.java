package core.controllers;

import auxiliary.data.DownloadableFile;
import core.screens.ConnectedPrimary;
import core.screens.GenericScreen;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PrimaryController extends GenericController {
    @FXML
    private VBox holderPane, filesSideBox;
    @FXML
    private Button btnSend, btnUpload, btnDownload;
    @FXML
    private TextField txtMessage;
    @FXML
    private TableView<DownloadableFile> tblFiles;
    @FXML
    TableColumn<DownloadableFile, String> nameColumn, sizeColumn;

    @Override
    public void setup(GenericScreen controlledScreen, String title){
        super.setup(controlledScreen, title);

        btnDownload.prefWidthProperty().bind(filesSideBox.widthProperty().divide(2));
        btnUpload.prefWidthProperty().bind(btnDownload.widthProperty());

        nameColumn.minWidthProperty().bind(tblFiles.widthProperty().divide(2));
        sizeColumn.minWidthProperty().bind(nameColumn.widthProperty());

        btnSend.setOnAction(actionEvent -> {
            clickSend();
        });

        txtMessage.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                clickSend();
            }
        });
    }

    public void addTableEntry(DownloadableFile file) {
        tblFiles.getItems().add(file);
    }

    public void addMessage(String text, boolean right) {
        //TODO: replace this label with a GUIElement
        Label label = new Label(text);

        label.setWrapText(true);
        label.setMinWidth(200);
        label.setPrefWidth(200);
        label.setMaxWidth(200);

        label.setPadding(new Insets(5));
        label.setTextAlignment(right ? TextAlignment.RIGHT : TextAlignment.LEFT);
        label.setAlignment(right ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        HBox container = new HBox(label);
        container.setAlignment(right ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        try {
            holderPane.getChildren().add(container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickSend(){
        String messageText = txtMessage.getText();
        ((ConnectedPrimary) controlledScreen).sendTextMessage(messageText);
        addMessage(messageText, true);
        txtMessage.clear();
    }
}
