package model;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class PHSSubScene extends SubScene {

    public PHSSubScene(int width, int height, int x, int y) {
        super(new AnchorPane(), width, height);
        prefWidth(width);
        prefHeight(height);

        AnchorPane root = (AnchorPane) this.getRoot();

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_panel.png").toURI().toString(), width, height, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        root.setBackground(new Background(image));

        setLayoutX(x);
        setLayoutY(y);
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
