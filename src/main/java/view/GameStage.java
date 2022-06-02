package view;

import controller.GameController;
import controller.MainController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.MapTile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class GameStage extends Stage {
    public static final int GAME_WIDTH = 768;
    public static final int GAME_HEIGHT = 968;

    private MainController mainController;
    private final GameController gameController;

    private final AnchorPane gamePane;

    private PHSSubScene mapSubScene;

    private ImageView[] playerPieces;
    private static double CELL_SIZE;
    private static double PIECE_SIZE;
    private static double STARTX;
    private static double STARTY;

    public static final Paint[] PLAYER_COLORS = {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.PURPLE};

    public PHSLabel boardLabel;
    public ArrayList<Label> playerLabel;
    public ImageView diceImage;
    public TextField pathInputField;

    public GameStage(GameController gameController) {
        this.gameController = gameController;
        this.gamePane = new AnchorPane();
        Scene gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        setScene(gameScene);
        setTitle("PHS Game");

        createBackground();
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        this.gamePane.setBackground(new Background((background)));
    }

    public void buildGame(MainController mainController) {
        this.mainController = mainController;
        this.mainController.getMainStage().hide();

        createMapSubScene();
        gameController.readMap();
        gameController.fillMap();
        drawMap();

        gameController.initializePlayers();
        drawPlayerPiece();

        createScoreBoard();
        createDiceBoard();
    }

    private void createMapSubScene() {
        this.mapSubScene = new PHSSubScene(700, 700, 34, 34, "white");
        this.gamePane.getChildren().add(mapSubScene);
    }

    public void drawMap() {
        int mapWidth = gameController.getMapWidth();
        int mapHeight = gameController.getMapHeight();

        if (mapHeight >= mapWidth) {
            CELL_SIZE = (mapSubScene.getHeight() - 20) / mapHeight;
            STARTY = 10.0;
            STARTX = (mapSubScene.getWidth() - CELL_SIZE * mapWidth) / 2;
        } else {
            CELL_SIZE = (mapSubScene.getWidth() - 20) / mapWidth;
            STARTX = 10.0;
            STARTY = (mapSubScene.getHeight() - CELL_SIZE * mapHeight) / 2;
        }

        for (int row = 0; row < mapHeight; row++) {
            for (int column = 0; column < mapWidth; column++) {
                MapTile tile = new MapTile(row, column, gameController.map.get(row).get(column), CELL_SIZE, STARTX, STARTY);
                mapSubScene.getPane().getChildren().add(tile);
            }
        }
    }

    private void createScoreBoard() {
        PHSSubScene scoreBoard = new PHSSubScene(300, 166, 34, 768, "yellow");
        gamePane.getChildren().add(scoreBoard);

        boardLabel = new PHSLabel("Player" + gameController.getCurrentPlayerIndex() + "'s Turn!", 280, 50);
        boardLabel.setTextFill(gameController.getPlayerColor(gameController.getCurrentPlayerIndex()));
        boardLabel.setLayoutX(10);
        boardLabel.setLayoutY(10);
        scoreBoard.getPane().getChildren().add(boardLabel);

        playerLabel = new ArrayList<>();
        playerLabel.add(new Label());
        for (int index = 1; index <= mainController.getNumOfPlayers(); index++) {
            Label pLabel = new Label("Player " + index + "'s score : "
                    + String.format("%2d", gameController.getPlayerScore(index))
                    + "            X" + gameController.getPlayerBridgeCards(index));
            try {
                pLabel.setFont(Font.loadFont(new FileInputStream(PHSLabel.FONT_PATH), 20));
            } catch (FileNotFoundException e) {
                pLabel.setFont(Font.font("Verdana", 20));
            }
            pLabel.setTextFill(Color.BLACK);
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
        PHSSubScene diceBoard = new PHSSubScene(366, 166, 368, 768, "yellow");
        gamePane.getChildren().add(diceBoard);

        diceImage = new ImageView(new Image(new File("src/main/resources/PNG/dice" + 1 + ".png").toURI().toString(), 126, 126, false, true));
        diceImage.prefWidth(126);
        diceImage.prefHeight(126);
        diceImage.setLayoutX(15);
        diceImage.setLayoutY(20);
        diceBoard.getPane().getChildren().add(diceImage);

        diceBoard.getPane().getChildren().add(createRollButton());
        diceBoard.getPane().getChildren().add(createStayButton());

        pathInputField = new TextField();
        pathInputField.setPromptText("PATH");
        pathInputField.setPrefWidth(145);
        pathInputField.setPrefHeight(30);
        pathInputField.setLayoutX(151);
        pathInputField.setLayoutY(20);
        diceBoard.getPane().getChildren().add(pathInputField);

        diceBoard.getPane().getChildren().add(createGoButton());
    }

    @NotNull
    private PHSButton createRollButton() {
        PHSButton rollButton = new PHSButton("ROLL", "half");
        rollButton.setLayoutX(151);
        rollButton.setLayoutY(106);
        rollButton.setOnAction(actionEvent -> gameController.rollDice());
        return rollButton;
    }

    @NotNull
    private PHSButton createStayButton() {
        PHSButton rollButton = new PHSButton("STAY", "half");
        rollButton.setLayoutX(256);
        rollButton.setLayoutY(106);
        rollButton.setOnAction(actionEvent -> gameController.pressedStay());
        return rollButton;
    }

    @NotNull
    private PHSButton createGoButton() {
        PHSButton goButton = new PHSButton("GO", "small");
        goButton.setLayoutX(306);
        goButton.setLayoutY(20);
        goButton.setOnAction(actionEvent -> gameController.pressedGo());
        return goButton;
    }

    public void drawPlayerPiece() {
        this.playerPieces = new ImageView[mainController.getNumOfPlayers() + 1];
        PIECE_SIZE = CELL_SIZE * 4 / 5;
        for (int index = 1; index <= mainController.getNumOfPlayers(); index++) {
            playerPieces[index] = new ImageView(new Image(new File("src/main/resources/PNG/pawn" + index + ".png").toURI().toString(), PIECE_SIZE, PIECE_SIZE, false, true));
            if ((index == 1) || (index == 2)) playerPieces[index].setLayoutY(gameController.getPlayerY(index) * CELL_SIZE + STARTY + (CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/4);
            else playerPieces[index].setLayoutY(gameController.getPlayerY(index) * CELL_SIZE + STARTY + (3*CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/3);
            if ((index == 1) || (index == 3)) playerPieces[index].setLayoutX(gameController.getPlayerX(index) * CELL_SIZE + STARTX + (CELL_SIZE/2 - PIECE_SIZE)/2 + PIECE_SIZE/40);
            else playerPieces[index].setLayoutX(gameController.getPlayerX(index) * CELL_SIZE + STARTX + (3*CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/10);
            mapSubScene.getPane().getChildren().add(playerPieces[index]);
        }
    }

    public void updatePlayerPiece() {
        for (int index = 1; index <= mainController.getNumOfPlayers(); index++) {
            if ((index == 1) || (index == 2)) playerPieces[index].setLayoutY(gameController.getPlayerY(index) * CELL_SIZE + STARTY + (CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/4);
            else playerPieces[index].setLayoutY(gameController.getPlayerY(index) * CELL_SIZE + STARTY + (3*CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/3);
            if ((index == 1) || (index == 3)) playerPieces[index].setLayoutX(gameController.getPlayerX(index) * CELL_SIZE + STARTX + (CELL_SIZE/2 - PIECE_SIZE)/2 + PIECE_SIZE/40);
            else playerPieces[index].setLayoutX(gameController.getPlayerX(index) * CELL_SIZE + STARTX + (3*CELL_SIZE/2 - PIECE_SIZE)/2 - PIECE_SIZE/10);
        }
    }
}