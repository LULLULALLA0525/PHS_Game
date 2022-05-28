package model;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class BoardSubScene extends SubScene {
    private final static int BOARD_WIDTH = 300;
    private final static int BOARD_HEIGHT = 166;

    public BoardSubScene() {
        super(new AnchorPane(), BOARD_WIDTH, BOARD_HEIGHT);
        prefWidth(BOARD_WIDTH);
        prefHeight(BOARD_HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_panel.png").toURI().toString(), BOARD_WIDTH, BOARD_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(34);
        setLayoutY(768);
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
