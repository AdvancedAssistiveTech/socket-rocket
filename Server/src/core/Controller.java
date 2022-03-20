package core;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import util.DownloadableFile;
import util.enums.Tags;
import util.sockets.TransferSocketManager;
import util.uicomponents.progressbox.FileProgressBox;

import java.io.FileNotFoundException;
import java.util.Formatter;

public class Controller {
    @FXML
    private VBox holderPane;

    @FXML
    private VBox filesSideBox;

    @FXML
    private TabPane mainPane;

    @FXML
    private GridPane filesGrid;

    @FXML
    private TableView<DownloadableFile> tblFiles;

    @FXML
    private TableColumn<DownloadableFile, String> nameColumn;

    @FXML
    private TableColumn<DownloadableFile, Long> sizeColumn;

    @FXML
    private TextField txtMessage;

    @FXML
    private Button btnWrite;

    private Stage stage;

    public void newMessage(String text, boolean right) {
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

    public void setup(Stage stage) {
        this.stage = stage;

        txtMessage.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                clickSend();
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        tblFiles.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
                clickDownload();
            }
        });

        nameColumn.prefWidthProperty().bind(tblFiles.widthProperty().multiply(0.75));
        sizeColumn.prefWidthProperty().bind(tblFiles.widthProperty().multiply(0.25));
    }

    public Stage getStage() {
        return stage;
    }

    public void addTableEntry(DownloadableFile file) {
        tblFiles.getItems().add(file);
    }

    public void setTitle(String title){
        stage.setTitle(title);
    }

    public void addFPB(TransferSocketManager transferSocket){
        filesSideBox.getChildren().add(new FileProgressBox(transferSocket, filesSideBox).getMainBox());
        filesSideBox.setStyle("-fx-border-color: " + getFxBorder());
    }

    public void removeFPB(FileProgressBox toRemove){
        filesSideBox.getChildren().remove(toRemove.getMainBox());
        filesSideBox.setStyle("-fx-border-color: " + getFxBorder());
    }

    @FXML
    private void clickSend() {
        String message = txtMessage.getText();
        App.serverManager.getChatManager().send(Tags.TEXT, message);
        newMessage(message, true);
        txtMessage.setText("");
    }

    private String fxBorder = "#808080";
    public String getFxBorder(){
        return fxBorder = fxBorder.equals("#808080") ? "#808081" : "#808080";
    }

    @FXML
    private void clickDownload(){
        DownloadableFile target = tblFiles.getSelectionModel().getSelectedItem();
        if(target != null){
            App.serverManager.requestDownload(target);
        }
        else {
            System.out.println("please pick a file");
        }
    }

    @FXML
    private void clickWrite(){
        System.out.printf("written %s to file%n", App.serverManager.getPort());
        try (Formatter writer = new Formatter("server.connection"))
        {
            writer.format(App.serverManager.getPort() + "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        btnWrite.setStyle("-fx-background-color: #ccffdd");
        btnWrite.setDisable(true);
    }
}
