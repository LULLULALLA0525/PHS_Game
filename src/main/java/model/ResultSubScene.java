package model;

import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class ResultSubScene extends SubScene {
    private final static int RESULT_BOARD_WIDTH = 600;
    private final static int RESULT_BOARD_HEIGHT = 400;

    public ResultSubScene() {
        super(new AnchorPane(), RESULT_BOARD_WIDTH, RESULT_BOARD_HEIGHT);
        prefWidth(RESULT_BOARD_WIDTH);
        prefHeight(RESULT_BOARD_HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_panel.png").toURI().toString(), RESULT_BOARD_WIDTH, RESULT_BOARD_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(20);
        setLayoutY(20);
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
