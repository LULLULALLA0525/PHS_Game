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
import java.util.Objects;

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

    private ArrayList<PHSBigButton> menuButtons;

    private int numOfPlayers = 0;
    private ArrayList<String> mapNames;
    private String mapName;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
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
        mapNames = new ArrayList<String>();
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
        playSubScene = new MainSubScene();
        mainPane.getChildren().add(playSubScene);

        PlayLabel playInfoLabel = new PlayLabel("Choose number of players and map.");
        playInfoLabel.setLayoutX(110);
        playInfoLabel.setLayoutY(100);
        playSubScene.getPane().getChildren().add(playInfoLabel);
        playSubScene.getPane().getChildren().add(createStartButton());
        playSubScene.getPane().getChildren().add(createHowManyPlayers());
        playSubScene.getPane().getChildren().add(createWhichMap());

        howToPlaySubScene = new MainSubScene();
        mainPane.getChildren().add(howToPlaySubScene);

        ImageView howToPlay = new ImageView(new Image(new File("src/main/resources/PNG/how_to_play.png").toURI().toString(), 600, 450, false, true));
        howToPlaySubScene.getPane().getChildren().add(howToPlay);
    }

    @NotNull
    private ComboBox createHowManyPlayers() {
        ObservableList<Integer> data = FXCollections.observableArrayList(2, 3, 4);
        ComboBox<Integer> playersCombo = new ComboBox<Integer>(data);
        playersCombo.setLayoutX(110);
        playersCombo.setLayoutY(180);
        playersCombo.setPrefWidth(195);
        playersCombo.setPrefHeight(40);
        playersCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                numOfPlayers = playersCombo.getValue();
            }
        });

        return playersCombo;
    }

    @NotNull
    private ComboBox createWhichMap() {
        ObservableList<String> data = FXCollections.observableArrayList(mapNames);
        ComboBox<String> mapCombo = new ComboBox<String>(data);
        mapCombo.setLayoutX(315);
        mapCombo.setLayoutY(180);
        mapCombo.setPrefWidth(195);
        mapCombo.setPrefHeight(40);
        mapCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mapName = mapCombo.getValue();
            }
        });

        return mapCombo;
    }

    @NotNull
    private PHSBigButton createStartButton() {
        PHSBigButton startButton = new PHSBigButton("START");
        startButton.setLayoutX(310);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(numOfPlayers != 0 && !mapName.equals("")) {
                    GameViewManager gameManager = new GameViewManager();
                    showSubScene(playSubScene);
                    gameManager.createNewGame(mainStage, mapName, numOfPlayers);
                }
            }
        });

        return startButton;
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
        createLoadButton();
        createHowToPlayButton();
        createExitButton();
    }

    private void createPlayButton() {
        PHSBigButton playButton = new PHSBigButton("NEW GAME");
        addButton(playButton);

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(playSubScene);
            }
        });
    }

    private void createLoadButton() {
        PHSBigButton loadButton = new PHSBigButton("LOAD GAME");
        addButton(loadButton);

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = new File("src/main/resources/log.txt");
                if (file.exists()) {
                    GameViewManager gameManager = new GameViewManager();
                    gameManager.loadGame(mainStage);
                }
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