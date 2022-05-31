package model;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PHSButton extends Button {
    private final static String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";

    private int width;
    private int height;
    private int fontSize;
    private final Background freeBackground;
    private final Background pressedBackground;

    public PHSButton(String text, String type) {
        setText(text);

        switch (type) {
            case "small" -> {
                this.width = 50;
                this.height = 50;
                this.fontSize = 17;
            }
            case "half" -> {
                this.width = 100;
                this.height = 50;
                this.fontSize = 17;
            }
            case "big" -> {
                this.width = 200;
                this.height = 50;
                this.fontSize = 24;
            }
        }
        freeBackground = new Background(new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_" + type + "_button.png").toURI().toString(), width, height, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        pressedBackground = new Background(new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_" + type + "_button_pressed.png").toURI().toString(), width, height, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        setButtonFont();
        setPrefWidth(this.width);
        setPrefHeight(this.height);
        setBackground(freeBackground);

        initializeButtonListeners();
    }

    private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), fontSize));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", fontSize));
        }
        setTextFill(Color.web("#381E0D"));
    }

    private void setButtonPressedStyle() {
        setBackground(pressedBackground);
        setPrefHeight(height - 4);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle() {
        setBackground(freeBackground);
        setPrefHeight(height);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() {
        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                setButtonPressedStyle();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                setButtonReleasedStyle();
            }
        });

        setOnMouseEntered(mouseEvent -> setEffect(new DropShadow()));

        setOnMouseExited(mouseEvent -> setEffect(null));
    }
}
