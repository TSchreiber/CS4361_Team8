package t8;

import javafx.application.Application;
import javafx.beans.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.fxml.*;
import java.util.*;
import java.util.Observable;
import java.net.*;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.*;

public class Launcher extends Application {
    
	public final static Button buttonRestart = new Button("Restart");
    final Button buttonClose = new Button("Close");
    final Button buttonPause = new Button("Pause");
    private Font font = new Font("Verdana", 36);
	public static Integer i = 0;

	private Timeline timeline;
    private Label timerLabel = new Label(), splitTimerLabel = new Label();
    private DoubleProperty timeSeconds = new SimpleDoubleProperty(),
            splitTimeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO, splitTime = Duration.ZERO;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
    	
        VBox root = new VBox();
        Text moves = new Text("MOVES");
        moves.setFont(Font.font("Helvetica", FontWeight.BOLD, 70));
        Label moveCount = new Label(i.toString());
        timerLabel.textProperty().bind(timeSeconds.asString());
        splitTimerLabel.textProperty().bind(splitTimeSeconds.asString());

        Text timetitle = new Text("TIME");
        timetitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 70));

        
       
        moveCount.setAlignment(Pos.CENTER);
        moveCount.setMaxWidth(200);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.setMaxWidth(200);
        moveCount.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
        timerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
        buttonRestart.setPrefSize(200, 50);
        buttonClose.setPrefSize(200, 50);
        buttonRestart.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        buttonClose.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        buttonPause.setPrefSize(200, 50);
        buttonPause.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));

        
        
        
        VBox moveCounter = new VBox(moves, moveCount,timetitle,timerLabel, buttonRestart, buttonPause, buttonClose);
        moveCounter.setSpacing(25);
        moveCounter.setAlignment(Pos.TOP_CENTER);
        HBox screen = new HBox(root,moveCounter);
        Scene scene = new Scene(screen, Color.BLANCHEDALMOND);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, 1200, 800, true, SceneAntialiasing.BALANCED);
        scene3d.setFill(Color.BLANCHEDALMOND);
        moveCounter.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, null, null)));
        moveCounter.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        root.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        root3d.getChildren().add(new AmbientLight());
        moveCounter.setPadding(new Insets(25));

        root3d.setScaleX(50);
        root3d.setScaleY(50);
        root3d.setScaleZ(50);
        root3d.setTranslateX(600);
        root3d.setTranslateY(400);
        
        buttonRestart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent a) {
				
				try {
					i = 0;
					time = Duration.ZERO;
					if(buttonPause.getText() == "Resume") {
						timeline.play();
						buttonPause.setText("Pause");
					}
					timeline.playFromStart();
					moveCount.setText(i.toString());
					root3d.getChildren().clear();
					var cube = new RubiksCube(); 
					root3d.getChildren().addAll(cube);
					root3d.getChildren().add(new AmbientLight());

			        var rotHandler = new MouseRotationHandler(cube);
			        var faceRotationHandler = new FaceRotationHandler(cube);
			        scene.setOnMousePressed((MouseEvent e) -> {
			            rotHandler.handle(e);
			            faceRotationHandler.handle(e);
			        });
			        scene.setOnMouseDragged((MouseEvent e) -> {
			            rotHandler.handle(e);
			            faceRotationHandler.handle(e);
			        });
			        scene.setOnMouseReleased((MouseEvent e) -> {
			            rotHandler.handle(e);
			            faceRotationHandler.handle(e);
			            moveCount.setText(i.toString());

			        });

				} catch (Exception el) {
					el.printStackTrace();
				}
			}
		});
        
        buttonPause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent a) {
				try {
					if(buttonPause.getText() == "Pause") {
						timeline.pause();
						buttonPause.setText("Resume");
					}
					else {
						timeline.play();
						buttonPause.setText("Pause");
					}
						
				} catch (Exception el) {
					el.printStackTrace();
				}
				
			}
        });
        
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent a) {
				try {
					stage.close();

				} catch (Exception el) {
					el.printStackTrace();
				}
			}
		});

        var cube = new RubiksCube();
        root3d.getChildren().addAll(cube);

        var rotHandler = new MouseRotationHandler(cube);
        var faceRotationHandler = new FaceRotationHandler(cube);
			scene3d.setOnMousePressed((MouseEvent e) -> {
				if (timeline != null) {
					splitTime = Duration.ZERO;
					splitTimeSeconds.set(splitTime.toSeconds());
				} else {
					timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent t) {
							Duration duration = ((KeyFrame) t.getSource()).getTime();
							time = time.add(duration);
							splitTime = splitTime.add(duration);
							timeSeconds.set(time.toSeconds());
							splitTimeSeconds.set(splitTime.toSeconds());
						}
					}));
					timeline.setCycleCount(Timeline.INDEFINITE);
					timeline.play();
				}
				if(buttonPause.getText() == "Pause") {
					rotHandler.handle(e);
					faceRotationHandler.handle(e);
				}
			});
			scene3d.setOnMouseDragged((MouseEvent e) -> {
				if(buttonPause.getText() == "Pause") {
					rotHandler.handle(e);
					faceRotationHandler.handle(e);
				}
			});
			scene3d.setOnMouseReleased((MouseEvent e) -> {
				if(buttonPause.getText() == "Pause") {
					rotHandler.handle(e);
					faceRotationHandler.handle(e);
					moveCount.setText(i.toString());
				}
				
			});
        root.getChildren().addAll(scene3d);
        

        stage.setTitle("Rubik's Cube");
        stage.setScene(scene);
        stage.show();
    }
    
    
    private class MouseRotationHandler implements EventHandler<MouseEvent> {

        public static double ROTATION_SPEED = 1/3f;

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
                xRotation.setAngle(initialAngleX - (anchor.getY() - e.getSceneY()) * ROTATION_SPEED);
                yRotation.setAngle(initialAngleY + (anchor.getX() - e.getSceneX()) * ROTATION_SPEED);
            }
        }

    }

}
