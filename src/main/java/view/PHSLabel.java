package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PHSLabel extends Label {
    public final static String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";
    public final static String FONT_COLOR = "#381E0D";

    public PHSLabel(String text, int width, int height) {
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        BackgroundImage image = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_info_panel.png").toURI().toString(), width, height, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
        setBackground(new Background(image));

        setPrefWidth(width);
        setPrefHeight(height);
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
        setTextFill(Color.web("#381E0D"));
    }
}
