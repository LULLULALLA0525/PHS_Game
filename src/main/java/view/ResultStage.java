package view;

import controller.GameController;
import controller.MainController;
import controller.ResultController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ResultStage extends Stage {
    private static final int RESULT_WIDTH = 640;
    private static final int RESULT_HEIGHT = 440;

    private final MainController mainController;
    private final GameController gameController;
    private final ResultController resultController;

    private final AnchorPane resultPane;

    public ResultStage(MainController mainController, GameController gameController, ResultController resultController) {
        this.mainController = mainController;
        this.gameController = gameController;
        this.resultController = resultController;
        this.resultPane = new AnchorPane();
        Scene resultScene = new Scene(resultPane, RESULT_WIDTH, RESULT_HEIGHT);
        setScene(resultScene);
        setTitle("PHS Game");

        createBackground();
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        this.resultPane.setBackground(new Background((background)));
    }

    public void createResultBoard(int highScore) {
        PHSSubScene resultSubScene = new PHSSubScene(600, 400, 20, 20, "yellow");
        resultPane.getChildren().add(resultSubScene);

        Label gameOver = new Label("Game Over!");
        try {
            gameOver.setFont(Font.loadFont(new FileInputStream(PHSLabel.FONT_PATH), 50));
        } catch (FileNotFoundException e) {
            gameOver.setFont(Font.font("Verdana", 50));
        }
        gameOver.setTextFill(Color.web("#381E0D"));
        gameOver.setLayoutX(160);
        gameOver.setLayoutY(20);

        resultSubScene.getPane().getChildren().add(gameOver);

        for (int index = 1; index <= mainController.getNumOfPlayers(); index++) {
            String winner = "";
            if (gameController.getPlayerScore(index) == highScore) winner = "   Winner!";
            Label rLabel = new Label("Player " + index + "'s score : "
                    + String.format("%2d", gameController.getPlayerScore(index))
                    + winner);
            try {
                rLabel.setFont(Font.loadFont(new FileInputStream(PHSLabel.FONT_PATH), 40));
            } catch (FileNotFoundException e) {
                rLabel.setFont(Font.font("Verdana", 40));
            }
            rLabel.setTextFill(Color.web(PHSLabel.FONT_COLOR));
            rLabel.setLayoutX(20);
            rLabel.setLayoutY(60 + 45 * index);

            resultSubScene.getPane().getChildren().add(rLabel);
        }

        PHSButton MenuButton = new PHSButton("GO TO MENU", "big");
        MenuButton.setLayoutX(200);
        MenuButton.setLayoutY(330);

        MenuButton.setOnAction(actionEvent -> resultController.goToMainMenu());

        resultSubScene.getPane().getChildren().add(MenuButton);
    }
}
