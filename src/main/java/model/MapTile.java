package model;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class MapTile extends StackPane {
    private int row, column;
    private int cellInfo;

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

    public MapTile(int row, int column, int cellInfo, double CELL_SIZE, double STARTX, double STARTY) {
        this.row = row;
        this.column = column;
        this.cellInfo = cellInfo;

        Rectangle border = new Rectangle(CELL_SIZE - 2, CELL_SIZE - 2);;
        File file = null;
        if (this.cellInfo == START) file = new File("src/main/resources/TILES/tile_start.png");
        else if (this.cellInfo == CELL) file = new File("src/main/resources/TILES/tile_normal.png");
        else if (this.cellInfo == BRIDGE_ENTRY) file = new File("src/main/resources/TILES/tile_cross.png");
        else if (this.cellInfo == BRIDGE) file = new File("src/main/resources/TILES/tile_bridge.png");
        else if (this.cellInfo == BRIDGE_EXIT) file = new File("src/main/resources/TILES/tile_normal.png");
        else if (this.cellInfo == HAMMER) file = new File("src/main/resources/TILES/tile_hammer.png");
        else if (this.cellInfo == SAW) file = new File("src/main/resources/TILES/tile_saw.png");
        else if (this.cellInfo == PDRIVER) file = new File("src/main/resources/TILES/tile_driver.png");
        else if (this.cellInfo == END) file = new File("src/main/resources/TILES/tile_end.png");
        else file = new File("src/main/resources/TILES/tile_wall.png");
        border.setFill(new ImagePattern(new Image(file.toURI().toString())));
        getChildren().add(border);

        setTranslateX(this.column * CELL_SIZE + STARTX);
        setTranslateY(this.row * CELL_SIZE + STARTY);
    }
}
