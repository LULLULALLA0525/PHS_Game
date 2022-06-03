package view;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class MainStage extends Stage {
    public static final int MAIN_WIDTH = 1024;
    public static final int MAIN_HEIGHT = 768;

    private final MainController mainController;

    private final AnchorPane mainPane;

    private PHSSubScene playSubScene;
    private PHSSubScene howToPlaySubScene;

    public MainStage(@NotNull MainController mainController) {
        this.mainController = mainController;
        this.mainPane = new AnchorPane();
        Scene mainScene = new Scene(mainPane, MAIN_WIDTH, MAIN_HEIGHT);
        setScene(mainScene);
        setTitle("PHS Game");

        createBackground();
        createTitle();

        ArrayList<String> mapNames = mainController.checkMapFiles();
        createSubScenes(mapNames);
        createButtons();
    }

    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        this.mainPane.setBackground(new Background((background)));
    }

    private void createTitle() {
        ImageView title = new ImageView(new Image(new File("src/main/resources/PNG/title.png").toURI().toString(), 800, 200, false, true));
        title.setLayoutX(112);
        title.setLayoutY(25);
        this.mainPane.getChildren().add(title);
    }

    private void createSubScenes(ArrayList<String> mapNames) {
        this.playSubScene = new PHSSubScene(600, 450, 1024, 250, "yellow");
        this.mainPane.getChildren().add(this.playSubScene);

        PHSLabel playInfoLabel = new PHSLabel("Choose number of players and map.", 400, 50);
        playInfoLabel.setLayoutX(110);
        playInfoLabel.setLayoutY(100);
        this.playSubScene.getPane().getChildren().add(playInfoLabel);
        this.playSubScene.getPane().getChildren().add(createStartButton());
        this.playSubScene.getPane().getChildren().add(createHowManyPlayers());
        this.playSubScene.getPane().getChildren().add(createWhichMap(mapNames));

        this.howToPlaySubScene = new PHSSubScene(600, 450, 1024, 250, "yellow");
        mainPane.getChildren().add(howToPlaySubScene);

        ImageView howToPlay = new ImageView(new Image(new File("src/main/resources/PNG/how_to_play.png").toURI().toString(), 600, 450, false, true));
        this.howToPlaySubScene.getPane().getChildren().add(howToPlay);
    }

    @NotNull
    private ComboBox createHowManyPlayers() {
        ObservableList<Integer> data = FXCollections.observableArrayList(2, 3, 4);
        ComboBox<Integer> playersCombo = new ComboBox<>(data);
        playersCombo.setLayoutX(110);
        playersCombo.setLayoutY(180);
        playersCombo.setPrefWidth(195);
        playersCombo.setPrefHeight(40);
        playersCombo.setOnAction(actionEvent -> mainController.setNumOfPlayers(playersCombo.getValue()));
        return playersCombo;
    }

    @NotNull
    private ComboBox createWhichMap(ArrayList<String> mapNames) {
        ObservableList<String> data = FXCollections.observableArrayList(mapNames);
        ComboBox<String> mapCombo = new ComboBox<>(data);
        mapCombo.setLayoutX(315);
        mapCombo.setLayoutY(180);
        mapCombo.setPrefWidth(195);
        mapCombo.setPrefHeight(40);
        mapCombo.setOnAction(actionEvent -> mainController.setMapName(mapCombo.getValue()));
        return mapCombo;
    }

    @NotNull
    private PHSButton createStartButton() {
        PHSButton startButton = new PHSButton("START", "big");
        startButton.setLayoutX(310);
        startButton.setLayoutY(300);
        startButton.setOnAction(actionEvent -> {
            if (mainController.getNumOfPlayers() != 0 && !mainController.getMapName().equals("")) {
                mainController.showSubScene(playSubScene);
                mainController.checkMap();
            }
        });
        return startButton;
    }

    private void createButtons() {
        ArrayList<PHSButton> buttons = new ArrayList<>();
        buttons.add(createPlayButton());
        buttons.add(createLoadButton());
        buttons.add(createHowToPlayButton());
        buttons.add(createExitButton());

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setLayoutX(100);
            buttons.get(i).setLayoutY(250 + i * PHSButton.BUTTON_SIZE * 2);
            mainPane.getChildren().add(buttons.get(i));
        }
    }

    @NotNull
    private PHSButton createPlayButton() {
        PHSButton playButton = new PHSButton("NEW GAME", "big");
        playButton.setOnAction(actionEvent -> mainController.showSubScene(playSubScene));
        return playButton;
    }

    @NotNull
    private PHSButton createLoadButton() {
        PHSButton loadButton = new PHSButton("LOAD GAME", "big");
        loadButton.setOnAction(actionEvent -> {
            File file = new File("src/main/resources/log.txt");
            if (file.exists()) {
                mainController.loadGame();
            }
        });
        return loadButton;
    }

    @NotNull
    private PHSButton createHowToPlayButton() {
        PHSButton helpButton = new PHSButton("HOW TO PLAY", "big");
        helpButton.setOnAction(actionEvent -> mainController.showSubScene(howToPlaySubScene));
        return helpButton;
    }

    @NotNull
    private PHSButton createExitButton() {
        PHSButton exitButton = new PHSButton("EXIT", "big");
        exitButton.setOnAction(actionEvent -> this.close());
        return exitButton;
    }
}