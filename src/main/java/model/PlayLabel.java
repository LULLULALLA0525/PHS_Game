package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayLabel extends Label {

    public final static String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";
    public final static int INFO_WIDTH = 400;
    public final static int INFO_HEIGHT = 50;

    public PlayLabel(String text) {
        setPrefWidth(INFO_WIDTH);
        setPrefHeight(INFO_HEIGHT);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_info_panel.png").toURI().toString(), INFO_WIDTH, INFO_HEIGHT, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
        setBackground(new Background(image));

    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
        setStyle("-fx-text-fill: BLACK;");
    }
}
