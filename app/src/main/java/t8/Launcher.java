package t8;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.*;
import javafx.fxml.*;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HelloWorld.fxml"));
        Scene scene = new Scene(root, 300, 275);
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
    }

}
