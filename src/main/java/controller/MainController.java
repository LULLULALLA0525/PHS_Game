package controller;

import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import view.MainStage;
import view.MapCheckingStage;
import view.PHSSubScene;

import java.io.*;
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

    public void checkMap(){
        MapCheckingStage mapCheckingStage = new MapCheckingStage(this);
        mapCheckingStage.show();
        String errMsg = isValidMap();
        mapCheckingStage.setMsgLabel(errMsg);
        mapCheckingStage.createMenuButton();
    }

    @NotNull
    private String isValidMap() {
        boolean isStart = true;
        boolean isEnd = false;

        int x = 0;
        int y = 0;

        ArrayList<Integer> bridgeEndX = new ArrayList<>();
        ArrayList<Integer> bridgeEndY = new ArrayList<>();

        File file = new File("src/main/resources/MAPS/" + mapName + ".map");
        BufferedReader mapFile;
        try {
            mapFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            while ((line = mapFile.readLine()) != null) {
                if (isEnd) return "\"E\" is only placed at last.";

                if (isStart) {
                    if (!line.startsWith("S")) return "Map should starts with \"S\".";
                    else if (line.length() != 3) return "Map file format is invalid.";
                }

                isEnd = line.startsWith("E");
                if (isEnd){
                    if (line.length() != 1) return "Map file format is invalid.";
                }

                if (!isStart && !isEnd && line.length() != 5) return "Map file format is invalid.";

                if (line.startsWith("B")) {
                    if (line.endsWith("L") || line.endsWith("R")) return "Map file is invalid.";

                    bridgeEndX.add(x + 2);
                    bridgeEndY.add(y);
                }

                for (int i = 0; i < bridgeEndX.size(); i++) {
                    if (bridgeEndX.get(i) == x && bridgeEndY.get(i) == y) {
                        if (line.startsWith("b")) {
                            bridgeEndX.remove(i);
                            bridgeEndY.remove(i);
                        } else return "Map has endless bridge.";
                    }
                }

                if (line.endsWith("U")) y--;
                else if (line.endsWith("D")) y++;
                else if (line.endsWith("R")) x++;
                else if (line.endsWith("L")) return "Map has left path.";

                isStart = false;
            }
            if (!isEnd) return "Map should finishes with \"E\".";

            if (bridgeEndX.size() != 0) return "Map has endless bridge";

            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Map loading complete!";
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