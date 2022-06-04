package application;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.MainController;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();
        stage = controller.getMainStage();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}