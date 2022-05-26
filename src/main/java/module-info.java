module com.example.phs_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;


    opens application to javafx.fxml;
    exports application;
    exports view;
    opens view to javafx.fxml;
}