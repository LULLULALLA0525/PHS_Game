package model;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.*;
import java.util.ArrayList;

public class MapSubScene extends SubScene {

    private static final int MAP_WIDTH = 700;
    private static final int MAP_HEIGHT = 700;

    public static ArrayList<Integer>[] map;
    private ImageView[] playerPieces;

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

    private static int mapHeight = 0;
    private static int mapWidth = 1;
    private static int startRow = 0;

    public MapSubScene() {
        super(new AnchorPane(), MAP_WIDTH, MAP_HEIGHT);
        prefWidth(MAP_WIDTH);
        prefHeight(MAP_HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/map_panel.png").toURI().toString(), MAP_WIDTH, MAP_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(34);
        setLayoutY(34);

        loadMap();
    }

    private void loadMap(){
        readMap();
        fillMap();
        drawMap();
    }

    private void readMap(){
        File file = new File("src/main/resources/MAPS/map.txt");
        BufferedReader mapFile;
        try {
            mapFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            int goDown = 0;
            int maxHeight = 0;
            int minHeight = 0;

            while ((line = mapFile.readLine()) != null) {
                if (line.endsWith("D")) goDown++;
                else if (line.endsWith("U")) goDown--;
                else if (line.endsWith("R")) mapWidth++;

                if (goDown > minHeight) minHeight = goDown;
                if (goDown < maxHeight) maxHeight = goDown;
            }

            mapHeight = minHeight - maxHeight + 1;
            startRow -= maxHeight;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillMap() {
        map = new ArrayList[mapHeight];
        for (int i = 0; i < mapHeight; i++) {
            map[i] = new ArrayList<Integer>();
            for (int j = 0; j < mapWidth; j++) {
                map[i].add(WALL);
            }
        }

        File file = new File("src/main/resources/MAPS/map.txt");
        BufferedReader mapFile;
        try {
            mapFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line;
            boolean isStart = true;

            int currentRow = startRow;
            int currentColumn = 0;
            while((line = mapFile.readLine()) != null) {
                if (line.startsWith("S") && isStart) map[currentRow].set(currentColumn, START);
                else if (line.startsWith("C")) map[currentRow].set(currentColumn, CELL);
                else if (line.startsWith("B")) map[currentRow].set(currentColumn, BRIDGE_ENTRY);
                else if (line.startsWith("b")) map[currentRow].set(currentColumn, BRIDGE_EXIT);
                else if (line.startsWith("H")) map[currentRow].set(currentColumn, HAMMER);
                else if (line.startsWith("S")) map[currentRow].set(currentColumn, SAW);
                else if (line.startsWith("P")) map[currentRow].set(currentColumn, PDRIVER);
                else if (line.startsWith("E")) {
                    map[currentRow].set(currentColumn, END);
                    break;
                }

                if (line.startsWith("B")) map[currentRow].set(currentColumn + 1, BRIDGE);

                if (isStart){
                    if (line.charAt(2) == 'U') currentRow--;
                    else if (line.charAt(2) == 'D') currentRow++;
                    else if (line.charAt(2) == 'R') currentColumn++;
                    isStart = false;
                    continue;
                }

                if (line.endsWith("U")) currentRow--;
                else if (line.endsWith("D")) currentRow++;
                else if (line.endsWith("R")) currentColumn++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double CELL_SIZE;
    private static double STARTX;
    private static double STARTY;
    private void drawMap() {
        AnchorPane root = (AnchorPane) this.getRoot();

        if (mapHeight >= mapWidth) {
            CELL_SIZE = (double)(MAP_HEIGHT - 20) / mapHeight;
            STARTY = 10.0;
            STARTX = (MAP_WIDTH - CELL_SIZE*mapWidth) / 2;
        }
        else {
            CELL_SIZE = (double)(MAP_WIDTH - 20) / mapWidth;
            STARTX = 10.0;
            STARTY = (MAP_HEIGHT - CELL_SIZE*mapHeight) / 2;
        }

        for (int row = 0; row < mapHeight; row++) {
            for (int column = 0; column < mapWidth; column++) {
                MapTile tile = new MapTile(row, column, map[row].get(column), CELL_SIZE, STARTX, STARTY);
                root.getChildren().add(tile);
            }
        }
    }

    public void drawPlayerPiece(ArrayList<Player> players, int numOfPlayers) {
        ImageView[] playerPieces = new ImageView[numOfPlayers];
        for (int index = 1; index < numOfPlayers; index++) {
            playerPieces[index] = new ImageView(new Image(new File("src/main/resources/PNG/pawn" + index + ".png").toURI().toString(), 20, 20, false, true));
            playerPieces[index].setLayoutX(players.get(index).getX());
        }
    }
}
