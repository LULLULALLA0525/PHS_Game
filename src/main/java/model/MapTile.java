package model;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.Objects;

public class MapTile extends StackPane {

    public static final int CELL = 0;
    public static final int PDRIVER = 1;
    public static final int HAMMER = 2;
    public static final int SAW = 3;
    public static final int BRIDGE_ENTRY = 4;
    public static final int BRIDGE = 5;
    public static final int BRIDGE_EXIT = 6;
    public static final int START = 7;
    public static final int END = 8;
    public static final int WALL = -1;

    public MapTile(int row, int column, int cellInfo, double CELL_SIZE, double STARTX, double STARTY) {
        Rectangle tile = new Rectangle(CELL_SIZE - 2, CELL_SIZE - 2);
        File file = null;
        if (cellInfo == START) file = new File("src/main/resources/TILES/tile_start.png");
        else if (cellInfo == CELL) file = new File("src/main/resources/TILES/tile_normal.png");
        else if (cellInfo == BRIDGE_ENTRY) file = new File("src/main/resources/TILES/tile_cross.png");
        else if (cellInfo == BRIDGE) file = new File("src/main/resources/TILES/tile_bridge.png");
        else if (cellInfo == BRIDGE_EXIT) file = new File("src/main/resources/TILES/tile_normal.png");
        else if (cellInfo == HAMMER) file = new File("src/main/resources/TILES/tile_hammer.png");
        else if (cellInfo == SAW) file = new File("src/main/resources/TILES/tile_saw.png");
        else if (cellInfo == PDRIVER) file = new File("src/main/resources/TILES/tile_driver.png");
        else if (cellInfo == END) file = new File("src/main/resources/TILES/tile_end.png");
        else if (cellInfo == WALL) file = new File("src/main/resources/TILES/tile_wall.png");
        tile.setFill(new ImagePattern(new Image(Objects.requireNonNull(file).toURI().toString())));
        getChildren().add(tile);

        setTranslateX(column * CELL_SIZE + STARTX);
        setTranslateY(row * CELL_SIZE + STARTY);
    }
}
