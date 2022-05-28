package model;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.util.Random;

public class DiceSubScene extends SubScene {

    private final static int DICE_BOARD_WIDTH = 366;
    private final static int DICE_BOARD_HEIGHT = 166;

    public DiceSubScene() {
        super(new AnchorPane(), DICE_BOARD_WIDTH, DICE_BOARD_HEIGHT);
        prefWidth(DICE_BOARD_WIDTH);
        prefHeight(DICE_BOARD_HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_panel.png").toURI().toString(), DICE_BOARD_WIDTH, DICE_BOARD_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root = (AnchorPane) this.getRoot();
        root.setBackground(new Background(image));

        setLayoutX(368);
        setLayoutY(768);
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
