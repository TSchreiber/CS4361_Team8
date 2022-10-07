package t8;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
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
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 800, true);
        scene.setFill(Color.BLANCHEDALMOND);
        root.getChildren().add(new AmbientLight());

        root.setScaleX(50);
        root.setScaleY(50);
        root.setScaleZ(50);
        root.setTranslateX(600);
        root.setTranslateY(400);

        Node cube = new RubiksCube(); 
        root.getChildren().addAll(cube);

        var rotHandler = new MouseRotationHandler(cube);
        scene.setOnMousePressed(rotHandler);
        scene.setOnMouseDragged(rotHandler);

        stage.setTitle("Rubik's Cube");
        stage.setScene(scene);
        stage.show();
    }

    private class MouseRotationHandler implements EventHandler<MouseEvent> {

        private Point2D anchor;
        private Rotate xRotation, yRotation;
        private double initialAngleX, initialAngleY;
        private Node cube;

        public MouseRotationHandler(Node cube) {
            this.cube = cube;
            xRotation = new Rotate(0, Rotate.X_AXIS);
            yRotation = new Rotate(0, Rotate.Y_AXIS);
            cube.getTransforms().addAll(xRotation, yRotation);
        }

        public void handle(MouseEvent e) {
            if (e.getButton() != MouseButton.SECONDARY) return;
            if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
                anchor = new Point2D(e.getSceneX(), e.getSceneY());
                initialAngleX = xRotation.getAngle();
                initialAngleY = yRotation.getAngle();
            } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                xRotation.setAngle(initialAngleX - (anchor.getY() - e.getSceneY()));
                yRotation.setAngle(initialAngleY - (anchor.getX() - e.getSceneX()));
            }
        }

    }

}
