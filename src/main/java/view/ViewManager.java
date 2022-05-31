package view;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.PHSLabel;
import model.PHSButton;
import model.PHSSubScene;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ViewManager {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    private final AnchorPane mainPane;
    private final Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 250;

    private PHSSubScene playSubScene;
    private PHSSubScene howToPlaySubScene;

    private PHSSubScene currentScene;

    private final ArrayList<PHSButton> menuButtons;

    private int numOfPlayers = 0;
    private ArrayList<String> mapNames;
    private String mapName;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        Scene mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);

        checkMapFiles();

        createBackground();
        createTitle();

        createSubScenes();
        createButtons();
    }

    private void checkMapFiles() {
        File mapDirectory = new File("src/main/resources/MAPS/");
        File[] mapFiles = mapDirectory.listFiles();
        mapNames = new ArrayList<>();
        for(int i = 0; i < Objects.requireNonNull(mapFiles).length; i++)
            mapNames.add(mapFiles[i].getName().split("\\.")[0]);
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
        playSubScene = new PHSSubScene(600, 450, 1024, 250);
        mainPane.getChildren().add(playSubScene);

        PHSLabel playInfoLabel = new PHSLabel("Choose number of players and map.", 400, 50);
        playInfoLabel.setLayoutX(110);
        playInfoLabel.setLayoutY(100);
        playSubScene.getPane().getChildren().add(playInfoLabel);
        playSubScene.getPane().getChildren().add(createStartButton());
        playSubScene.getPane().getChildren().add(createHowManyPlayers());
        playSubScene.getPane().getChildren().add(createWhichMap());

        howToPlaySubScene = new PHSSubScene(600, 450, 1024, 250);
        mainPane.getChildren().add(howToPlaySubScene);

        ImageView howToPlay = new ImageView(new Image(new File("src/main/resources/PNG/how_to_play.png").toURI().toString(), 600, 450, false, true));
        howToPlaySubScene.getPane().getChildren().add(howToPlay);
    }

    @NotNull
    private ComboBox createHowManyPlayers() {
        ObservableList<Integer> data = FXCollections.observableArrayList(2, 3, 4);
        ComboBox<Integer> playersCombo = new ComboBox<>(data);
        playersCombo.setLayoutX(110);
        playersCombo.setLayoutY(180);
        playersCombo.setPrefWidth(195);
        playersCombo.setPrefHeight(40);
        playersCombo.setOnAction(actionEvent -> numOfPlayers = playersCombo.getValue());

        return playersCombo;
    }

    @NotNull
    private ComboBox createWhichMap() {
        ObservableList<String> data = FXCollections.observableArrayList(mapNames);
        ComboBox<String> mapCombo = new ComboBox<>(data);
        mapCombo.setLayoutX(315);
        mapCombo.setLayoutY(180);
        mapCombo.setPrefWidth(195);
        mapCombo.setPrefHeight(40);
        mapCombo.setOnAction(actionEvent -> mapName = mapCombo.getValue());

        return mapCombo;
    }

    @NotNull
    private PHSButton createStartButton() {
        PHSButton startButton = new PHSButton("START", "big");
        startButton.setLayoutX(310);
        startButton.setLayoutY(300);

        startButton.setOnAction(actionEvent -> {
            if(numOfPlayers != 0 && !mapName.equals("")) {
                GameViewManager gameManager = new GameViewManager();
                showSubScene(playSubScene);
                gameManager.createNewGame(mainStage, mapName, numOfPlayers);
            }
        });

        return startButton;
    }

    private void showSubScene(PHSSubScene subScene) {
        moveSubScene(subScene, currentScene == subScene);

        if (currentScene == subScene) currentScene = null;
        else {
            if (currentScene != null) moveSubScene(currentScene, true);
            currentScene = subScene;
        }
    }

    private void moveSubScene(PHSSubScene subScene, boolean isShown) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(subScene);

        if (isShown) transition.setToX(0);
        else transition.setToX(-676);

        transition.play();
    }

    private void createButtons() {
        createPlayButton();
        createLoadButton();
        createHowToPlayButton();
        createExitButton();
    }

    private void createPlayButton() {
        PHSButton playButton = new PHSButton("NEW GAME", "big");
        addButton(playButton);

        playButton.setOnAction(actionEvent -> showSubScene(playSubScene));
    }

    private void createLoadButton() {
        PHSButton loadButton = new PHSButton("LOAD GAME", "big");
        addButton(loadButton);

        loadButton.setOnAction(actionEvent -> {
            File file = new File("src/main/resources/log.txt");
            if (file.exists()) {
                GameViewManager gameManager = new GameViewManager();
                gameManager.loadGame(mainStage);
            }
        });
    }

    private void createHowToPlayButton() {
        PHSButton helpButton = new PHSButton("HOW TO PLAY", "big");
        addButton(helpButton);

        helpButton.setOnAction(actionEvent -> showSubScene(howToPlaySubScene));
    }

    private void createExitButton() {
        PHSButton exitButton = new PHSButton("EXIT", "big");
        addButton(exitButton);

        exitButton.setOnAction(actionEvent -> mainStage.close());
    }

    private void addButton(@NotNull PHSButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    public Stage getMainStage() { return mainStage; }
}