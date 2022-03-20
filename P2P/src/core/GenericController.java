package core;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class GenericController {
    protected Stage currentStage;

    public static final javafx.scene.image.Image ico_up = new Image(GenericController.class.getResourceAsStream("/ico_up.jpg"));

    public void setup(Stage currentStage){
        this.currentStage = currentStage;
        currentStage.getIcons().add(ico_up);
    }

    public void changeStage(Stage incomingStage){
        currentStage.close();
        incomingStage.show();
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}
