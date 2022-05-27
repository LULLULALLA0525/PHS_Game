package view;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class GameViewManager {
    public final static String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";

    private final AnchorPane gamePane;
    private final Scene gameScene;
    private final Stage gameStage;

    private static final int GAME_WIDTH = 768;
    private static final int GAME_HEIGHT = 968;

    private Stage menuStage;

    private AnimationTimer gameTimer;

    private static int diceNum = 1;
    private static String pathInput;

    private int numOfPlayers;
    private ArrayList<Player> players;
    private ArrayList<Label> playerLabel;
    private ImageView diceImage;
    private TextField pathInputField;
    private static int currentPlayerIndex = 1;

    public GameViewManager() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);

        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        gamePane.setBackground(new Background((background)));
    }

    public void createNewGame (Stage menuStage, int numOfPlayers) {
        this.menuStage = menuStage;
        this.menuStage.hide();

        this.numOfPlayers = numOfPlayers;
        initializePlayers();

        createMap();
        createScoreBoard();
        createDiceBoard();
        createGameLoop();

        gameStage.show();
    }

    private void initializePlayers() {
        players = new ArrayList<Player>();
        players.add(new Player(-1)); //dummy player
        for (int index = 1; index <= numOfPlayers; index++) players.add(new Player(index));

        players.get(currentPlayerIndex).giveTurn(); //Player 1
    }

    private void createMap() {
        MapSubScene map = new MapSubScene();
        gamePane.getChildren().add(map);
    }

    private void createScoreBoard() {
        BoardSubScene scoreBoard = new BoardSubScene();
        gamePane.getChildren().add(scoreBoard);

        BoardLabel boardLabel = new BoardLabel(numOfPlayers + " Players Mode");
        boardLabel.setLayoutX(10);
        boardLabel.setLayoutY(10);
        scoreBoard.getPane().getChildren().add(boardLabel);

        playerLabel = new ArrayList<Label>();
        playerLabel.add(new Label());
        for (int index = 1; index <= numOfPlayers; index++) {
            Label pLabel = new Label("Player " + index + "'s score : " + players.get(index).getPlayerScore());
            try {
                pLabel.setFont(Font.loadFont(new FileInputStream(FONT_PATH), 20));
            } catch (FileNotFoundException e) {
                pLabel.setFont(Font.font("Verdana", 20));
            }
            pLabel.setStyle("-fx-text-fill: BLACK;");
            playerLabel.add(pLabel);

            pLabel.setLayoutX(20);
            pLabel.setLayoutY(40 + 24 * index);

            scoreBoard.getPane().getChildren().add(pLabel);
        }
    }

    private void createDiceBoard() {
        DiceSubScene diceBoard = new DiceSubScene();
        gamePane.getChildren().add(diceBoard);

        diceImage = new ImageView(new Image(new File("src/main/resources/PNG/dice" + diceNum + ".png").toURI().toString(), 126, 126, false, true));
        diceImage.prefWidth(126);
        diceImage.prefHeight(126);
        diceImage.setLayoutX(20);
        diceImage.setLayoutY(20);
        diceBoard.getPane().getChildren().add(diceImage);

        diceBoard.getPane().getChildren().add(createRollButton());

        pathInputField = new TextField();
        pathInputField.setPromptText("PATH");
        pathInputField.setPrefWidth(130);
        pathInputField.setPrefHeight(30);
        pathInputField.setLayoutX(166);
        pathInputField.setLayoutY(20);
        diceBoard.getPane().getChildren().add(pathInputField);

        diceBoard.getPane().getChildren().add(createGoButton());
    }

    private PHSBigButton createRollButton() {
        PHSBigButton rollButton = new PHSBigButton("ROLL");
        rollButton.setLayoutX(161);
        rollButton.setLayoutY(106);

        rollButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (players.get(currentPlayerIndex).isChanceToRoll()) {
                    Random random = new Random();

                    diceNum = random.nextInt(6) + 1;
                    diceImage.setImage(new Image(new File("src/main/resources/PNG/dice" + diceNum + ".png").toURI().toString(), 126, 126, false, true));
                    pathInputField.setPromptText("You can go " + diceNum + " cells.");

                    players.get(currentPlayerIndex).finishRoll();
                }
            }
        });
        return rollButton;
    }

    private PHSSmallButton createGoButton() {
        PHSSmallButton goButton = new PHSSmallButton("GO");
        goButton.setLayoutX(306);
        goButton.setLayoutY(20);

        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (players.get(currentPlayerIndex).isChanceToGo()) {
                    pathInput = pathInputField.getText();

                    if (pathInput.length() == diceNum) {
                        giveScoreToPlayer(diceNum);
                        players.get(currentPlayerIndex).finishGo();
                        pathInputField.setText("");
                        pathInputField.setPromptText("PATH");
                    } else {
                        pathInputField.setPromptText("Invalid Text");
                    }
                }
            }
        });
        return goButton;
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkPlayerIsDone();
                checkGameOver();
            }
        };
        gameTimer.start();
    }

    private void checkPlayerIsDone() {
        if (!players.get(currentPlayerIndex).isTurnOver()) {
            currentPlayerIndex = currentPlayerIndex % numOfPlayers + 1;
            players.get(currentPlayerIndex).giveTurn();
        }
    }

    private void checkGameOver() {
        int finishedPlayer = 0;
        for (int index = 1; index <= numOfPlayers; index++)
            if (!players.get(index).isPlayable()) finishedPlayer++;

        if (finishedPlayer == numOfPlayers) gameTimer.stop();
    }

    private void giveScoreToPlayer(int score) {
        players.get(currentPlayerIndex).giveScore(score);
        playerLabel.get(currentPlayerIndex).setText("Player " + currentPlayerIndex + "'s score : " + players.get(currentPlayerIndex).getPlayerScore());
    }
}
