package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.PlayLabel;
import model.PHSBigButton;
import model.MainSubScene;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    private final AnchorPane mainPane;
    private final Scene mainScene;
    private final Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 250;

    private MainSubScene playSubScene;
    private MainSubScene howToPlaySubScene;

    private MainSubScene sceneToHide;

    List<PHSBigButton> menuButtons;

    private int numOfPlayers;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);

        createBackground();
        createTitle();

        createSubScenes();
        createButtons();
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background((background)));
    }

    private void createTitle() {
        ImageView title = new ImageView(new Image(new File("src/main/resources/PNG/title.png").toURI().toString(), 800, 200, false, true));
        title.setLayoutX(112);
        title.setLayoutY(25);
        mainPane.getChildren().add(title);
    }

    private void createSubScenes() {
        playSubScene = new MainSubScene();
        mainPane.getChildren().add(playSubScene);

        PlayLabel playInfoLabel = new PlayLabel("Choose number of players.");
        playInfoLabel.setLayoutX(110);
        playInfoLabel.setLayoutY(100);
        playSubScene.getPane().getChildren().add(playInfoLabel);
        playSubScene.getPane().getChildren().add(createStartButton());
        playSubScene.getPane().getChildren().add(createHowManyPlayers());

        howToPlaySubScene = new MainSubScene();
        mainPane.getChildren().add(howToPlaySubScene);

        ImageView howToPlay = new ImageView(new Image(new File("src/main/resources/PNG/how_to_play.png").toURI().toString(), 600, 450, false, true));
        howToPlaySubScene.getPane().getChildren().add(howToPlay);
    }

    @NotNull
    private PHSBigButton createStartButton() {
        PHSBigButton startButton = new PHSBigButton("START");
        startButton.setLayoutX(310);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameViewManager gameManager = new GameViewManager();
                showSubScene(playSubScene);
                gameManager.createNewGame(mainStage, numOfPlayers);
            }
        });

        return startButton;
    }

    @NotNull
    private ComboBox createHowManyPlayers() {
        ObservableList<Integer> data = FXCollections.observableArrayList(2, 3, 4);
        ComboBox<Integer> combo = new ComboBox<Integer>(data);
        combo.setLayoutX(110);
        combo.setLayoutY(180);
        combo.setPrefWidth(400);
        combo.setPrefHeight(40);
        combo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                numOfPlayers = combo.getValue();
            }
        });

        return combo;
    }

    private void showSubScene(@NotNull MainSubScene subScene) {
        subScene.moveSubScene();
        if(sceneToHide == subScene) {
            sceneToHide = null;
        }
        else if(sceneToHide != null) {
            sceneToHide.moveSubScene();
            sceneToHide = subScene;
        }
        else {
            sceneToHide = subScene;
        }
    }

    private void createButtons() {
        createPlayButton();
        createHowToPlayButton();
        createExitButton();
    }

    private void createPlayButton() {
        PHSBigButton playButton = new PHSBigButton("PLAY");
        addButton(playButton);

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(playSubScene);
            }
        });
    }

    private void createHowToPlayButton() {
        PHSBigButton helpButton = new PHSBigButton("HOW TO PLAY");
        addButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(howToPlaySubScene);
            }
        });
    }

    private void createExitButton() {
        PHSBigButton exitButton = new PHSBigButton("EXIT");
        addButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });
    }

    private void addButton(@NotNull PHSBigButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    public Stage getMainStage() { return mainStage; }
}