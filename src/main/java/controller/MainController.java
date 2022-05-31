package controller;

import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.MainStage;
import view.PHSSubScene;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {

    private final MainStage mainStage;

    private int numOfPlayers = 0;
    private String mapName;

    private PHSSubScene currentScene;

    public MainController() { mainStage = new MainStage(this); }

    public ArrayList<String> checkMapFiles() {
        File mapDirectory = new File("src/main/resources/MAPS/");
        File[] mapFiles = mapDirectory.listFiles();
        ArrayList<String> mapNames = new ArrayList<>();
        for(int i = 0; i < Objects.requireNonNull(mapFiles).length; i++)
            mapNames.add(mapFiles[i].getName().split("\\.")[0]);
        return mapNames;
    }

    public void showSubScene(PHSSubScene subScene) {
        moveSubScene(subScene, currentScene == subScene);

        if (currentScene == subScene) currentScene = null;
        else {
            if (currentScene != null) moveSubScene(currentScene, true);
            currentScene = subScene;
        }
    }

    private void moveSubScene(PHSSubScene subScene, boolean isShown) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(subScene);

        if (isShown) transition.setToX(0);
        else transition.setToX(-676);

        transition.play();
    }

    public void newGame() {
        GameController gameController = new GameController();
        gameController.buildNewGame(this);
    }

    public void loadGame() {
        GameController gameController = new GameController();
        gameController.buildLoadedGame(this);
    }

    public Stage getMainStage() { return mainStage; }
    public int getNumOfPlayers() { return numOfPlayers; }
    public String getMapName() { return mapName; }

    public void setNumOfPlayers(int numOfPlayers) { this.numOfPlayers = numOfPlayers; }
    public void setMapName(String mapName) { this.mapName = mapName; }
}