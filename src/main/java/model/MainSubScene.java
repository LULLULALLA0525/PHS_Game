package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;

public class MainSubScene extends SubScene {
    private final static int SUBSCENE_WIDTH = 600;
    private final static int SUBSCENE_HEIGHT = 450;

    private boolean isHidden = true;

    public MainSubScene() {
        super(new AnchorPane(), SUBSCENE_WIDTH, SUBSCENE_HEIGHT);
        prefWidth(SUBSCENE_WIDTH);
        prefHeight(SUBSCENE_HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_panel.png").toURI().toString(), SUBSCENE_WIDTH, SUBSCENE_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(1024);
        setLayoutY(250);
    }

    public void moveSubScene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);

        if(isHidden) {
            transition.setToX(-676);
            isHidden = false;
        } else {
            transition.setToX(0);
            isHidden = true;
        }

        transition.play();
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }

}
