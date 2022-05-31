package controller;


import javafx.stage.Stage;
import view.ResultStage;

import java.io.File;

public class ResultController {

    private final MainController mainController;
    private final GameController gameController;
    private final ResultStage resultStage;

    public ResultController(MainController mainController, GameController gameController) {
        this.mainController = mainController;
        this.gameController = gameController;
        this.resultStage = new ResultStage(mainController, gameController, this);
    }

    public void showResult() {
        int highScore = 0;
        for (int index = 1; index <= mainController.getNumOfPlayers() - 1; index++) {
            if (gameController.getPlayerScore(index) > highScore) highScore = gameController.getPlayerScore(index);
        }
        resultStage.createResultBoard(highScore);
        resultStage.show();
    }

    public void goToMainMenu() {
        File file = new File("src/main/resources/log.txt");
        file.delete();
        resultStage.close();
        mainController.getMainStage().show();
    }
}
