package t8;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.fxml.*;
import java.util.*;
import java.net.*;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.*;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Scene scene = new Scene(root, Color.BLANCHEDALMOND);

        HBox menu = new HBox();
        menu.getChildren().addAll(new Button("Button1"), new Button("Button2"));

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, 1200, 800, true, SceneAntialiasing.BALANCED);
        scene3d.setFill(Color.BLANCHEDALMOND);
        root3d.getChildren().add(new AmbientLight());

        root3d.setScaleX(50);
        root3d.setScaleY(50);
        root3d.setScaleZ(50);
        root3d.setTranslateX(600);
        root3d.setTranslateY(400);

        var cube = new RubiksCube();
        root3d.getChildren().addAll(cube);

        MouseHandler mouseHandler = new MouseHandler(cube);
        scene3d.setOnMousePressed(mouseHandler);
        scene3d.setOnMouseDragged(mouseHandler);
        scene3d.setOnMouseReleased(mouseHandler);

        root.getChildren().addAll(menu, scene3d);

        stage.setTitle("Rubik's Cube");
        stage.setScene(scene);
        stage.show();
    }

    

}
