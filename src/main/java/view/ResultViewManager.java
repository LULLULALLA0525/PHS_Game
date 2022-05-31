package view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.PHSButton;
import model.PHSSubScene;
import model.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ResultViewManager {
    public final static String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";

    private final AnchorPane resultPane;
    private final Stage resultStage;

    private static final int RESULT_WIDTH = 640;
    private static final int RESULT_HEIGHT = 440;

    private Stage menuStage;

    public ResultViewManager() {
        resultPane = new AnchorPane();
        Scene resultScene = new Scene(resultPane, RESULT_WIDTH, RESULT_HEIGHT);
        resultStage = new Stage();
        resultStage.setScene(resultScene);
        resultStage.setTitle("PHS Game");

        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        resultPane.setBackground(new Background((background)));
    }

    public void showResult(Stage menuStage, ArrayList<Player> players) {
        this.menuStage = menuStage;

        int highScore = 0;
        for (int index = 1; index <= players.size() - 1; index++) {
            if (players.get(index).getPlayerScore() > highScore) highScore = players.get(index).getPlayerScore();
        }
        createResultBoard(players, highScore);

        resultStage.show();
    }

    private void createResultBoard(ArrayList<Player> players, int highScore) {
        PHSSubScene resultSubScene = new PHSSubScene(600, 400, 20, 20);
        resultPane.getChildren().add(resultSubScene);

        Label gameOver = new Label("Game Over!");
        try {
            gameOver.setFont(Font.loadFont(new FileInputStream(FONT_PATH), 50));
        } catch (FileNotFoundException e) {
            gameOver.setFont(Font.font("Verdana", 50));
        }
        gameOver.setTextFill(Color.web("#381E0D"));
        gameOver.setLayoutX(160);
        gameOver.setLayoutY(20);

        resultSubScene.getPane().getChildren().add(gameOver);

        for (int index = 1; index <= players.size() - 1; index++) {
            String winner = "";
            if (players.get(index).getPlayerScore() == highScore) winner = "   Winner!";
            Label rLabel = new Label("Player " + index + "'s score : "
                    + String.format("%2d", players.get(index).getPlayerScore())
                    + winner);
            try {
                rLabel.setFont(Font.loadFont(new FileInputStream(FONT_PATH), 40));
            } catch (FileNotFoundException e) {
                rLabel.setFont(Font.font("Verdana", 40));
            }
            rLabel.setStyle("-fx-text-fill: #381E0D;");
            rLabel.setLayoutX(20);
            rLabel.setLayoutY(60 + 45 * index);

            resultSubScene.getPane().getChildren().add(rLabel);
        }

        PHSButton MenuButton = new PHSButton("GO TO MENU", "big");
        MenuButton.setLayoutX(200);
        MenuButton.setLayoutY(330);

        MenuButton.setOnAction(actionEvent -> {
            File file = new File("src/main/resources/log.txt");
            file.delete();
            resultStage.close();
            menuStage.show();
        });

        resultSubScene.getPane().getChildren().add(MenuButton);
    }
}
