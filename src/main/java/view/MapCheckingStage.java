package view;

import controller.MainController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class MapCheckingStage extends Stage {
    public static final int CHECKING_WIDTH = 500;
    public static final int CHECKING_HEIGHT = 250;

    private final MainController mainController;
    private final AnchorPane checkingPane;

    private final PHSLabel msgLabel;
    private String errMsg;

    public MapCheckingStage(MainController mainController) {
        this.mainController = mainController;
        this.errMsg = "Map Checking...";

        this.checkingPane = new AnchorPane();
        Scene checkingScene = new Scene(checkingPane, CHECKING_WIDTH, CHECKING_HEIGHT);
        setScene(checkingScene);
        setTitle("Map checking");

        createBackground();

        this.msgLabel = new PHSLabel(errMsg, 400, 50);
        this.msgLabel.setLayoutX(50);
        this.msgLabel.setLayoutY(50);
        this.checkingPane.getChildren().add(msgLabel);
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        this.checkingPane.setBackground(new Background((background)));
    }

    public void setMsgLabel(String errMsg) {
        this.errMsg = errMsg;
        this.msgLabel.setText(errMsg);
    }

    public void createMenuButton() {
        PHSButton menuButton = new PHSButton("CONTINUE", "big");
        menuButton.setLayoutX(150);
        menuButton.setLayoutY(150);

        menuButton.setOnAction(actionEvent -> {
            if (errMsg.equals("Map loading complete!")) {
                this.close();
                mainController.newGame();
            }
            else this.close();
        });

        checkingPane.getChildren().add(menuButton);
    }
}
