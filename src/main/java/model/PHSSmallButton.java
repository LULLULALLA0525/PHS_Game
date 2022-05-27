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

public class PHSSmallButton extends Button {

    private final String FONT_PATH = "src/main/resources/FONTS/Cafe24Decobox.ttf";
    private final static int BUTTON_WIDTH = 50;
    private final static int BUTTON_HEIGHT = 50;

    private final Background freeBackground = new Background(new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_small_button.png").toURI().toString()),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
    private final Background pressedBackground = new Background(new BackgroundImage(new Image(new File("src/main/resources/PNG/yellow_small_button_pressed.png").toURI().toString()),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

    public PHSSmallButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(BUTTON_WIDTH);
        setPrefHeight(BUTTON_HEIGHT);
        setBackground(freeBackground);
        initializeButtonListeners();
    }

    private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 17));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 17));
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
