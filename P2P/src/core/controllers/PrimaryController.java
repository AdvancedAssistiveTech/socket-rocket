package core.controllers;

import auxiliary.data.DownloadableFile;
import core.screens.ConnectedPrimary;
import core.screens.GenericScreen;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PrimaryController extends GenericController {
    @FXML
    private VBox holderPane;
    @FXML
    private TableView<DownloadableFile> tblFiles;
    @FXML
    private Button btnSend;
    @FXML
    private TextField txtMessage;

    @Override
    public void setup(GenericScreen controlledScreen, String title){
        super.setup(controlledScreen, title);

        btnSend.setOnAction(actionEvent -> {
            String messageText = txtMessage.getText();
            ((ConnectedPrimary) controlledScreen).sendTextMessage(messageText);
            addMessage(messageText, true);
            txtMessage.clear();
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
}
