package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager manager = new ViewManager();
        stage = manager.getMainStage();
        stage.setTitle("PHS GAME");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}