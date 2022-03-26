package core.controllers;

import core.screens.ConnectionDashboard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class BrokerController extends GenericController {
    @FXML
    private TextFlow logFlow;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button btnCancel;

    private double progress = 0;
    private int steps;

    public void logMessage(String text, boolean newLine){
        text += newLine ? "\n" : "";
        Text candidateText = new Text(text);
        try{
            logFlow.getChildren().add(candidateText);
        }
        catch (IllegalStateException stateException){
            Platform.runLater(() -> logFlow.getChildren().add(candidateText));
        }
    }

    public void logMessage(Text text){
        logFlow.getChildren().add(text);
    }

    public void increaseProgress() {
        if (progress < 1){
            progress += (1d / steps);
        }
        else {
            System.err.println("Call to increaseProgress() in connectionBroker when progress already >= 1");
        }
        progressBar.setProgress(progress);
    }

    public void brokerError(){
        try{
            progressBar.setStyle("-fx-accent: red");
            btnCancel.setText("Return to dashboard");
        }
        catch (IllegalStateException stateException){
            System.out.println("caught");
            Platform.runLater(this::brokerError);
        }
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public void setup(Stage currentStage) {
        super.setup(currentStage);

        currentStage.setResizable(false);

        progressBar.prefWidthProperty().bind(logFlow.widthProperty());

        btnCancel.setOnAction(actionEvent -> {
            changeStage(new ConnectionDashboard().getController().getCurrentStage());
        });
    }
}