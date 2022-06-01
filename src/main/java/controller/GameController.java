package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.*;
import view.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GameController {

    private MainController mainController;
    private final GameStage gameStage;

    private AnimationTimer gameTimer;

    private ArrayList<Player> players;
    private int currentPlayerIndex = 1;
    private int finishedPlayers = 0;

    private int diceNum = 1;
    private String pathInput;

    public GameController() { gameStage = new GameStage(this); }

    public void buildNewGame(MainController mainController) {
        this.mainController = mainController;
        this.gameStage.buildGame(this.mainController);
        createGameLoop();
        gameStage.show();
    }

    public void initializePlayers() {
        players = new ArrayList<>();
        players.add(new Player(-1, GameStage.PLAYER_COLORS[0], false, 0, 0, 0, 0)); //dummy player

        for (int index = 1; index <= mainController.getNumOfPlayers(); index++)
            players.add(new Player(index, GameStage.PLAYER_COLORS[index], true, 0, 0, startColumn, startRow));

        players.get(currentPlayerIndex).giveTurn(); //Player 1
    }

    public void buildLoadedGame(MainController mainController) {
        this.mainController = mainController;

        players = new ArrayList<>();
        players.add(new Player(-1, GameStage.PLAYER_COLORS[0], false,0, 0, 0, 0)); //dummy player

        File file = new File("src/main/resources/log.txt");
        BufferedReader logFile;
        try {
            logFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            int lineNum = 1;

            while ((line = logFile.readLine()) != null) {
                if (lineNum == 1) mainController.setMapName(line);
                else if (lineNum == 2) mainController.setNumOfPlayers(Integer.parseInt(line));
                else if (lineNum == 3) currentPlayerIndex = Integer.parseInt(line);
                else if (lineNum == 4) finishedPlayers = Integer.parseInt(line);
                else {
                    String[] playerStatus = line.split("-");
                    int playerIndex = Integer.parseInt(playerStatus[0]);
                    boolean playable = Boolean.parseBoolean(playerStatus[1]);
                    int playerScore = Integer.parseInt(playerStatus[2]);
                    int bridgeCards = Integer.parseInt(playerStatus[3]);
                    int x = Integer.parseInt(playerStatus[4]);
                    int y = Integer.parseInt(playerStatus[5]);
                    players.add(new Player(playerIndex, GameStage.PLAYER_COLORS[playerIndex], playable, playerScore, bridgeCards, x, y));
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        players.get(currentPlayerIndex).giveTurn(); //Player 1

        this.gameStage.buildGame(this.mainController);
        createGameLoop();

        gameStage.show();
    }

    public ArrayList<ArrayList<Integer>> map;
    public ArrayList<ArrayList<Integer>> mapWithDirection;
    public ImageView[] playerPieces;

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

    private int mapHeight = 0;
    private int mapWidth = 1;
    private int startRow = 0;
    private int startColumn = 0;

    public void readMap(){
        File file = new File("src/main/resources/MAPS/" + mainController.getMapName() + ".map");
        BufferedReader mapFile;
        try {
            mapFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            int goDown = 0;
            int goRight = 0;
            int maxWidth = 0;
            int minWidth = 0;
            int maxHeight = 0;
            int minHeight = 0;

            while ((line = mapFile.readLine()) != null) {
                if (line.endsWith("D")) goDown++;
                else if (line.endsWith("U")) goDown--;
                else if (line.endsWith("R")) goRight++;
                else if (line.endsWith("L")) goRight--;

                if (goRight > minWidth) minWidth = goRight;
                if (goRight < maxWidth) maxWidth = goRight;

                if (goDown > minHeight) minHeight = goDown;
                if (goDown < maxHeight) maxHeight = goDown;
            }

            mapWidth = minWidth - maxWidth + 1;
            mapHeight = minHeight - maxHeight + 1;
            startRow -= maxHeight;
            startColumn -= maxWidth;

            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillMap() {
        map = new ArrayList<>();
        mapWithDirection = new ArrayList<>();
        for (int i = 0; i < mapHeight; i++) {
            map.add(new ArrayList<>());
            mapWithDirection.add(new ArrayList<>());
            for (int j = 0; j < mapWidth; j++) {
                map.get(i).add(WALL);
                mapWithDirection.get(i).add(1000000);
            }
        }

        File file = new File("src/main/resources/MAPS/" + mainController.getMapName() + ".map");
        BufferedReader mapFile;
        try {
            mapFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            boolean isStart = true;

            int currentRow = startRow;
            int currentColumn = startColumn;
            int cnt = 1;
            while((line = mapFile.readLine()) != null) {
                if (line.startsWith("S") && isStart) map.get(currentRow).set(currentColumn, START);
                else if (line.startsWith("C")) map.get(currentRow).set(currentColumn, CELL);
                else if (line.startsWith("B")) map.get(currentRow).set(currentColumn, BRIDGE_ENTRY);
                else if (line.startsWith("b")) map.get(currentRow).set(currentColumn, BRIDGE_EXIT);
                else if (line.startsWith("H")) map.get(currentRow).set(currentColumn, HAMMER);
                else if (line.startsWith("S")) map.get(currentRow).set(currentColumn, SAW);
                else if (line.startsWith("P")) map.get(currentRow).set(currentColumn, PDRIVER);
                else if (line.startsWith("E")) {
                    map.get(currentRow).set(currentColumn, END);
                    break;
                }

                mapWithDirection.get(currentRow).set(currentColumn, cnt++);
                if (line.startsWith("B")) {
                    map.get(currentRow).set(currentColumn + 1, BRIDGE);
                    mapWithDirection.get(currentRow).set(currentColumn + 1, cnt);
                }

                if (isStart){
                    if (line.charAt(2) == 'U') currentRow--;
                    else if (line.charAt(2) == 'D') currentRow++;
                    else if (line.charAt(2) == 'R') currentColumn++;
                    else if (line.charAt(2) == 'L') currentColumn--;
                    isStart = false;
                    continue;
                }

                if (line.endsWith("U")) currentRow--;
                else if (line.endsWith("D")) currentRow++;
                else if (line.endsWith("R")) currentColumn++;
                else if (line.endsWith("L")) currentColumn--;
            }

            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rollDice() {
        if (players.get(currentPlayerIndex).isChanceToRoll()) {
            Random random = new Random();
            this.diceNum = random.nextInt(6) + 1;
            gameStage.diceImage.setImage(new Image(new File("src/main/resources/PNG/dice" + diceNum + ".png").toURI().toString(), 126, 126, false, true));

            if (diceNum - players.get(currentPlayerIndex).getBridgeCards() > 0) {
                gameStage.pathInputField.setPromptText("You can go " + (diceNum - players.get(currentPlayerIndex).getBridgeCards()) + " cells.");
                players.get(currentPlayerIndex).finishRoll();
            } else {
                gameStage.pathInputField.setText("");
                gameStage.pathInputField.setPromptText("Player" + currentPlayerIndex + " can't go.");
                players.get(currentPlayerIndex).takeBridgeCard();
                players.get(currentPlayerIndex).finishRoll();
                players.get(currentPlayerIndex).finishGo();
            }
        }
    }

    public void pressedStay() {
        if (players.get(currentPlayerIndex).isChanceToRoll() && (players.get(currentPlayerIndex).getBridgeCards() > 0)) {
            isStay = true;
            players.get(currentPlayerIndex).takeBridgeCard();
            players.get(currentPlayerIndex).finishRoll();
            players.get(currentPlayerIndex).finishGo();
            gameStage.pathInputField.setText("");
            gameStage.pathInputField.setPromptText("PATH");
        } else if (players.get(currentPlayerIndex).isChanceToRoll()) {
            gameStage.pathInputField.setText("");
            gameStage.pathInputField.setPromptText("You don't have card.");
        }
    }

    public void pressedGo() {
        if (players.get(currentPlayerIndex).isChanceToGo()) {
            this.pathInput = gameStage.pathInputField.getText();
            String errMsg = isValidPath();
            if (errMsg.equals("Pass")) {
                players.get(currentPlayerIndex).finishGo();
                gameStage.pathInputField.setText("");
                gameStage.pathInputField.setPromptText("PATH");
            } else {
                gameStage.pathInputField.setText("");
                gameStage.pathInputField.setPromptText(errMsg);
            }
        }
    }

    private String isValidPath() {
        if (pathInput.length() != diceNum - players.get(currentPlayerIndex).getBridgeCards()) return "Should be " + (diceNum - players.get(currentPlayerIndex).getBridgeCards()) + "cells";

        int currentX = players.get(currentPlayerIndex).getX();
        int currentY = players.get(currentPlayerIndex).getY();

        for (int i = 0; i < pathInput.length(); i++) {
            boolean isBack = (finishedPlayers != 0) && isGoingBack(currentX, currentY, pathInput.charAt(i));

            if ((pathInput.charAt(i) == 'U') || (pathInput.charAt(i) == 'u')) currentY--;
            else if ((pathInput.charAt(i) == 'D') || (pathInput.charAt(i) == 'd')) currentY++;
            else if ((pathInput.charAt(i) == 'L') || (pathInput.charAt(i) == 'l')) currentX--;
            else if ((pathInput.charAt(i) == 'R') || (pathInput.charAt(i) == 'r')) currentX++;
            else return "";

            if ((currentX < 0) || (currentX > mapWidth)) return "Out of map";
            else if ((currentY < 0) || (currentY > mapHeight)) return "Out of map";

            if (map.get(currentY).get(currentX) == WALL) return "Can't go that way";
            else if (map.get(currentY).get(currentX) == END) return "Pass";
            else if (isBack) return "Can't go back";

            if (map.get(currentY).get(currentX) == BRIDGE) gainedBridgeCards++;
        }
        return "Pass";
    }

    private boolean isGoingBack(int currentX, int currentY, char pathInputChar) {
        int src = mapWithDirection.get(currentY).get(currentX);
        int dest = 0;

        if (src == 1) return false;

        if ((pathInputChar == 'U') || (pathInputChar == 'u')) dest = mapWithDirection.get(currentY - 1).get(currentX);
        else if ((pathInputChar == 'D') || (pathInputChar == 'd')) dest = mapWithDirection.get(currentY + 1).get(currentX);
        else if ((pathInputChar == 'L') || (pathInputChar == 'l')) dest = mapWithDirection.get(currentY).get(currentX - 1);
        else if ((pathInputChar == 'R') || (pathInputChar == 'r')) dest = mapWithDirection.get(currentY).get(currentX + 1);

        return src > dest;
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
            updatePlayerStatus();
            do {
                currentPlayerIndex = currentPlayerIndex % mainController.getNumOfPlayers() + 1;
                if(finishedPlayers == mainController.getNumOfPlayers()) break;
            } while (!players.get(currentPlayerIndex).isPlayable());
            players.get(currentPlayerIndex).giveTurn();
            gameStage.boardLabel.setText("Player" + currentPlayerIndex + "'s Turn!");
            gameStage.boardLabel.setTextFill(players.get(currentPlayerIndex).getPlayerColor());
            updateLog();
        }
    }

    private void updateLog() {
        File file = new File("src/main/resources/log.txt");
        BufferedWriter logFile;
        try {
            logFile = new BufferedWriter(new FileWriter(file));
            logFile.write(mainController.getMapName());
            logFile.newLine();
            logFile.write(Integer.toString(mainController.getNumOfPlayers()));
            logFile.newLine();
            logFile.write(Integer.toString(currentPlayerIndex));
            logFile.newLine();
            logFile.write(Integer.toString(finishedPlayers));
            logFile.newLine();
            for (int index = 1; index <= mainController.getNumOfPlayers(); index++)
                logFile.write(players.get(index).getPlayerStatus());

            logFile.flush();
            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayerStatus() {
        if (!isStay) calculateScore();
        else isStay = false;
        pathInput = "";
        diceNum = 1;
        gameStage.updatePlayerPiece();
    }

    private boolean isStay = false;
    private int gainedBridgeCards = 0;

    private void calculateScore() {
        int currentX = players.get(currentPlayerIndex).getX();
        int currentY = players.get(currentPlayerIndex).getY();

        int gainedScore = 0;
        gainedBridgeCards = 0;

        for (int i = 0; i < pathInput.length(); i++) {
            if ((pathInput.charAt(i) == 'U') || (pathInput.charAt(i) == 'u')) currentY--;
            else if ((pathInput.charAt(i) == 'D') || (pathInput.charAt(i) == 'd')) currentY++;
            else if ((pathInput.charAt(i) == 'L') || (pathInput.charAt(i) == 'l')) currentX--;
            else if ((pathInput.charAt(i) == 'R') || (pathInput.charAt(i) == 'r')) currentX++;

            if (map.get(currentY).get(currentX) == BRIDGE) gainedBridgeCards++;
            else if (map.get(currentY).get(currentX) == PDRIVER) gainedScore += PDRIVER;
            else if (map.get(currentY).get(currentX) == HAMMER) gainedScore += HAMMER;
            else if (map.get(currentY).get(currentX) == SAW) gainedScore += SAW;
            else if (map.get(currentY).get(currentX) == END) {
                if (finishedPlayers == 0) gainedScore += 7;
                else if (finishedPlayers == 1) gainedScore += 3;
                else if (finishedPlayers == 2) gainedScore += 1;
                quitThisPlayer();
                break;
            }
        }
        giveToPlayer(gainedScore, gainedBridgeCards);
        players.get(currentPlayerIndex).move(currentX, currentY);
    }

    private void giveToPlayer(int gainedScore, int gainedBridgeCards) {
        players.get(currentPlayerIndex).giveScore(gainedScore);
        players.get(currentPlayerIndex).giveBridgeCard(gainedBridgeCards);
        gameStage.playerLabel.get(currentPlayerIndex).setText("Player " + currentPlayerIndex + "'s score : "
                + String.format("%2d", players.get(currentPlayerIndex).getPlayerScore())
                + "            X" + players.get(currentPlayerIndex).getBridgeCards());
    }

    private void quitThisPlayer() {
        finishedPlayers++;
        players.get(currentPlayerIndex).quitPlay();
        gameStage.playerLabel.get(currentPlayerIndex).setTextFill(Color.RED);
    }

    private void checkGameOver() {
        if (finishedPlayers == mainController.getNumOfPlayers() - 1) {
            gameStage.close();
            gameTimer.stop();

            ResultController resultController = new ResultController(mainController, this);
            resultController.showResult();
        }
    }

    public Stage getGameStage() { return gameStage; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public Paint getPlayerColor(int index) { return players.get(index).getPlayerColor(); }
    public int getPlayerScore(int index) { return players.get(index).getPlayerScore(); }
    public int getPlayerBridgeCards(int index) { return players.get(index).getBridgeCards(); }
    public int getPlayerX(int index) { return players.get(index).getX(); }
    public int getPlayerY(int index) { return players.get(index).getY(); }

    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
}
