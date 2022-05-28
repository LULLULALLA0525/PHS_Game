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
import javafx.scene.paint.Color;
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
    private MapSubScene mapSubScene;

    private AnimationTimer gameTimer;

    private static int diceNum = 1;
    private static String pathInput;

    private int numOfPlayers;
    private ArrayList<Player> players;
    private static int currentPlayerIndex = 1;
    private static int finishedPlayers = 0;
    private BoardLabel boardLabel;
    private ArrayList<Label> playerLabel;
    private ImageView diceImage;
    private TextField pathInputField;

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

        createMap();

        this.numOfPlayers = numOfPlayers;
        initializePlayers();

        createScoreBoard();
        createDiceBoard();
        createGameLoop();

        gameStage.show();
    }

    private void initializePlayers() {
        players = new ArrayList<Player>();
        players.add(new Player(-1, 0, 0)); //dummy player
        for (int index = 1; index <= numOfPlayers; index++) players.add(new Player(index, 0, mapSubScene.getStartRow()));

        players.get(currentPlayerIndex).giveTurn(); //Player 1
    }

    private void createMap() {
        mapSubScene = new MapSubScene();
        gamePane.getChildren().add(mapSubScene);
    }

    private void createScoreBoard() {
        BoardSubScene scoreBoard = new BoardSubScene();
        gamePane.getChildren().add(scoreBoard);

        boardLabel = new BoardLabel("Player" + currentPlayerIndex + "'s Turn!");
        boardLabel.setLayoutX(10);
        boardLabel.setLayoutY(10);
        scoreBoard.getPane().getChildren().add(boardLabel);

        playerLabel = new ArrayList<Label>();
        playerLabel.add(new Label());
        for (int index = 1; index <= numOfPlayers; index++) {
            Label pLabel = new Label("Player " + index + "'s score : "
                    + String.format("%2d", players.get(index).getPlayerScore())
                    + "            X" + players.get(index).getBridgeCards());
            try {
                pLabel.setFont(Font.loadFont(new FileInputStream(FONT_PATH), 20));
            } catch (FileNotFoundException e) {
                pLabel.setFont(Font.font("Verdana", 20));
            }
            pLabel.setStyle("-fx-text-fill: BLACK;");
            pLabel.setLayoutX(20);
            pLabel.setLayoutY(40 + 24 * index);

            playerLabel.add(pLabel);
            scoreBoard.getPane().getChildren().add(pLabel);

            ImageView cards = new ImageView(new Image(new File("src/main/resources/PNG/cards.png").toURI().toString(), 20, 20, false, true));
            cards.setLayoutX(230);
            cards.setLayoutY(41 + 24 * index);
            scoreBoard.getPane().getChildren().add(cards);
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

                    if (diceNum - players.get(currentPlayerIndex).getBridgeCards() > 0) {
                        pathInputField.setPromptText("You can go " + (diceNum - players.get(currentPlayerIndex).getBridgeCards()) + " cells.");
                        players.get(currentPlayerIndex).finishRoll();
                    } else {
                        players.get(currentPlayerIndex).takeBridgeCard();
                        players.get(currentPlayerIndex).finishRoll();
                        players.get(currentPlayerIndex).finishGo();
                    }
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

                    if ((pathInput.length() == diceNum - players.get(currentPlayerIndex).getBridgeCards()) && isValidPath()) {
                        calculateScore();
                        players.get(currentPlayerIndex).finishGo();
                        pathInputField.setText("");
                        pathInputField.setPromptText("PATH");
                    } else {
                        pathInputField.setText("");
                        pathInputField.setPromptText("Invalid Path");
                    }
                }
            }
        });
        return goButton;
    }

    private boolean isValidPath() {
        int currentX = players.get(currentPlayerIndex).getX();
        int currentY = players.get(currentPlayerIndex).getY();

        for (int i = 0; i < pathInput.length(); i++) {
            if ((finishedPlayers != 0) && isGoingBack(currentX, currentY, pathInput.charAt(i))) return false;

            if ((pathInput.charAt(i) == 'U') || (pathInput.charAt(i) == 'u')) currentY--;
            else if ((pathInput.charAt(i) == 'D') || (pathInput.charAt(i) == 'd')) currentY++;
            else if ((pathInput.charAt(i) == 'L') || (pathInput.charAt(i) == 'l')) currentX--;
            else if ((pathInput.charAt(i) == 'R') || (pathInput.charAt(i) == 'r')) currentX++;
            else return false;

            if ((currentX < 0) || (currentX > mapSubScene.getMapWidth())) return false;
            else if ((currentY < 0) || (currentY > mapSubScene.getMapHeight())) return false;

            if (mapSubScene.map[currentY].get(currentX) == WALL) return false;
            else if (mapSubScene.map[currentY].get(currentX) == BRIDGE) gainedBridgeCards++;
        }
        return true;
    }

    private boolean isGoingBack(int currentX, int currentY, char pathInputChar) {
        int src = mapSubScene.mapWithDirection[currentY].get(currentX);
        int dest = 0;

        if ((pathInputChar == 'U') || (pathInputChar == 'u')) dest = mapSubScene.mapWithDirection[currentY - 1].get(currentX);
        else if ((pathInputChar == 'D') || (pathInputChar == 'd')) dest = mapSubScene.mapWithDirection[currentY + 1].get(currentX);
        else if ((pathInputChar == 'L') || (pathInputChar == 'l')) dest = mapSubScene.mapWithDirection[currentY - 1].get(currentX - 1);
        else if ((pathInputChar == 'R') || (pathInputChar == 'r')) dest = mapSubScene.mapWithDirection[currentY - 1].get(currentX + 1);

        return src > dest;
    }

    private int gainedScore = 0;
    private int gainedBridgeCards = 0;

    private void calculateScore() {
        int currentX = players.get(currentPlayerIndex).getX();
        int currentY = players.get(currentPlayerIndex).getY();

        gainedScore = 0;
        gainedBridgeCards = 0;

        for (int i = 0; i < pathInput.length(); i++) {
            if ((pathInput.charAt(i) == 'U') || (pathInput.charAt(i) == 'u')) currentY--;
            else if ((pathInput.charAt(i) == 'D') || (pathInput.charAt(i) == 'd')) currentY++;
            else if ((pathInput.charAt(i) == 'L') || (pathInput.charAt(i) == 'l')) currentX--;
            else if ((pathInput.charAt(i) == 'R') || (pathInput.charAt(i) == 'r')) currentX++;

            if (mapSubScene.map[currentY].get(currentX) == BRIDGE) gainedBridgeCards++;
            else if (mapSubScene.map[currentY].get(currentX) == PDRIVER) gainedScore += PDRIVER;
            else if (mapSubScene.map[currentY].get(currentX) == HAMMER) gainedScore += HAMMER;
            else if (mapSubScene.map[currentY].get(currentX) == SAW) gainedScore += SAW;
            else if (mapSubScene.map[currentY].get(currentX) == END) {
                if (finishedPlayers == 0) gainedScore += 7;
                else if (finishedPlayers == 1) gainedScore += 3;
                else if (finishedPlayers == 2) gainedScore += 1;
                quitThisPlayer();
            }
        }
        giveToPlayer(gainedScore, gainedBridgeCards);
        players.get(currentPlayerIndex).move(currentX, currentY);
    }

    private void giveToPlayer(int gainedScore, int gainedBridgeCards) {
        players.get(currentPlayerIndex).giveScore(gainedScore);
        players.get(currentPlayerIndex).giveBridgeCard(gainedBridgeCards);
        playerLabel.get(currentPlayerIndex).setText("Player " + currentPlayerIndex + "'s score : "
                + String.format("%2d", players.get(currentPlayerIndex).getPlayerScore())
                + "            X" + players.get(currentPlayerIndex).getBridgeCards());
    }

    private void quitThisPlayer() {
        players.get(currentPlayerIndex).quitPlay();
        playerLabel.get(currentPlayerIndex).setTextFill(Color.RED);
    }

    private static final int CELL = 0;
    private static final int PDRIVER = 1;
    private static final int HAMMER = 2;
    private static final int SAW = 3;
    private static final int BRIDGE_ENTRY = 4;
    private static final int BRIDGE = 5;
    private static final int BRIDGE_EXIT = 6;
    private static final int START = 7;
    private static final int END = 8;
    private static final int WALL = -1;

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
            do {
                currentPlayerIndex = currentPlayerIndex % numOfPlayers + 1;
            } while (!players.get(currentPlayerIndex).isPlayable());
            players.get(currentPlayerIndex).giveTurn();
            boardLabel.setText("Player" + currentPlayerIndex + "'s Turn!");
        }
    }

    private void checkGameOver() {
        int finishedPlayer = 0;
        for (int index = 1; index <= numOfPlayers; index++)
            if (!players.get(index).isPlayable()) finishedPlayer++;

        if (finishedPlayer == numOfPlayers) gameTimer.stop();
    }
}
