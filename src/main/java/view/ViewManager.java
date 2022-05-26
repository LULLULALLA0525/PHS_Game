package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.PlayLabel;
import model.PHSButton;
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
    private MainSubScene scoreSubScene;
    private MainSubScene helpSubScene;
    private MainSubScene creditSubScene;

    private MainSubScene sceneToHide;

    List<PHSButton> menuButtons;

    private int numOfPlayers;
    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubScenes();
        createButtons();
        createBackground();
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

    private void createSubScenes() {
        playSubScene = new MainSubScene();
        mainPane.getChildren().add(playSubScene);

        PlayLabel playInfoLabel = new PlayLabel("Choose number of players.");
        playInfoLabel.setLayoutX(110);
        playInfoLabel.setLayoutY(40);
        playSubScene.getPane().getChildren().add(playInfoLabel);
        playSubScene.getPane().getChildren().add(createStartButton());
        playSubScene.getPane().getChildren().add(createHowManyPlayers());

        scoreSubScene = new MainSubScene();
        mainPane.getChildren().add(scoreSubScene);

        helpSubScene = new MainSubScene();
        mainPane.getChildren().add(helpSubScene);

        creditSubScene = new MainSubScene();
        mainPane.getChildren().add(creditSubScene);
    }

    private PHSButton createStartButton() {
        PHSButton startButton = new PHSButton("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameViewManager gameManager = new GameViewManager();
                gameManager.createNewGame(mainStage, numOfPlayers);
            }
        });

        return startButton;
    }

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

    public Stage getMainStage() {
        return mainStage;
    }

    private void addMenuButton(@NotNull PHSButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createButtons() {
        createPlayButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }

    private void createPlayButton() {
        PHSButton playButton = new PHSButton("PLAY");
        addMenuButton(playButton);

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(playSubScene);
            }
        });
    }

    private void createScoreButton() {
        PHSButton scoreButton = new PHSButton("SCORES");
        addMenuButton(scoreButton);

        scoreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private void createHelpButton() {
        PHSButton helpButton = new PHSButton("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(helpSubScene);
            }
        });
    }

    private void createCreditsButton() {
        PHSButton creditsButton = new PHSButton("CREDITS");
        addMenuButton(creditsButton);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(creditSubScene);
            }
        });
    }

    private void createExitButton() {
        PHSButton exitButton = new PHSButton("EXIT");
        addMenuButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });
    }


    private void createBackground() {
        BackgroundImage background = new BackgroundImage(new Image(new File("src/main/resources/PNG/main_background_green.png").toURI().toString(), 256, 256, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background((background)));
    }

}
