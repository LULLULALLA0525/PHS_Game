package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BoardSubScene;
import model.PHSMap;

import java.io.File;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 768;
    private static final int GAME_HEIGHT = 968;

    private Stage menuStage;

    public GameViewManager() {
        initializeStage();
        createBackground();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    public void createNewGame (Stage menuStage, int numOfPlayers) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createMap();
        createBoard(numOfPlayers);
        gameStage.show();
    }

    private void createMap() {
        PHSMap map = new PHSMap();
        gamePane.getChildren().add(map);
    }

    private void createBoard (int numOfPlayers) {
        BoardSubScene board = new BoardSubScene(numOfPlayers);
        gamePane.getChildren().add(board);
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        gamePane.setBackground(new Background((background)));
    }
}
