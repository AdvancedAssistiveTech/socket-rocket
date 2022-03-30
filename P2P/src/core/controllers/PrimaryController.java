package core.controllers;

import auxiliary.data.DownloadableFile;
import auxiliary.gui_elements.FileProgressBox;
import core.screens.ConnectedPrimary;
import core.screens.GenericScreen;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

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

        ConnectedPrimary connectedPrimary = ((ConnectedPrimary) getControlledScreen());

        btnDownload.prefWidthProperty().bind(filesSideBox.widthProperty().divide(2));
        btnUpload.prefWidthProperty().bind(btnDownload.widthProperty());

        nameColumn.minWidthProperty().bind(tblFiles.widthProperty().divide(2));
        sizeColumn.minWidthProperty().bind(nameColumn.widthProperty());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        btnUpload.setOnAction(actionEvent -> {
            try {
                DownloadableFile uploadTarget = selectDownloadableFile();
                connectedPrimary.sendUploadMessage(uploadTarget);
            }
            catch (NullPointerException pointerException){
                System.out.println("file selection cancelled");
            }
        });

        btnDownload.setOnAction(actionEvent -> connectedPrimary.sendDownloadMessage(tblFiles.getSelectionModel().getSelectedItem()));

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

    public void addFileProgressBox(FileProgressBox box){
        filesSideBox.getChildren().add(box.getRoot());
    }

    public void refreshFileBox(){
        filesSideBox.setStyle("-fx-border-color: " + (fxBorder = fxBorder.equals("#808080") ? "#808081" : "#808080"));
    }
    private String fxBorder = "#808080";

    private DownloadableFile selectDownloadableFile() throws NullPointerException{
        return new DownloadableFile(new FileChooser().showOpenDialog(null));
    }

    private void clickSend(){
        String messageText = txtMessage.getText();
        ((ConnectedPrimary) controlledScreen).sendTextMessage(messageText);
        addMessage(messageText, true);
        txtMessage.clear();
    }
}
