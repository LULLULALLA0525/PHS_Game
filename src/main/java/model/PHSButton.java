package model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PHSButton extends Button {

    private final String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";
    private final static int BUTTON_WIDTH = 200;
    private final static int BUTTON_HEIGHT = 50;

    private final BackgroundImage freeImage = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_button.png").toURI().toString()),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    private final Background freeBackground = new Background(freeImage);
    private final BackgroundImage pressedImage = new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_button_pressed.png").toURI().toString()),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    private final Background pressedBackground = new Background(pressedImage);
    public PHSButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(BUTTON_WIDTH);
        setPrefHeight(BUTTON_HEIGHT);
        setBackground(freeBackground);
        initializeButtonListeners();
    }

    private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
        setStyle("-fx-text-fill: #381E0D;");
    }

    private void setButtonPressedStyle() {
        setBackground(pressedBackground);
        setPrefHeight(BUTTON_HEIGHT - 4);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle() {
        setBackground(freeBackground);
        setPrefHeight(BUTTON_HEIGHT);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });

    }

}
