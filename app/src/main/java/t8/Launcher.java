package t8;

import javafx.application.Application;
import javafx.beans.*;
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
    private MouseHandler mouseHandler;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox();

        Text moves = new Text("MOVES");
        moves.setFont(Font.font("Helvetica", FontWeight.BOLD, 70));
        Label moveCount = new Label(i.toString());
        Timer timer = new Timer();
        timer.startTimer();
        stage.setOnCloseRequest(e -> timer.stopTimer());
        Text time = new Text("TIME");
        time.setFont(Font.font("Helvetica", FontWeight.BOLD, 70));
        Label timeCount = new Label();
        timeCount.textProperty().bind(timer.getSspTime());
       
        moveCount.setAlignment(Pos.CENTER);
        moveCount.setMaxWidth(200);
        timeCount.setAlignment(Pos.CENTER);
        timeCount.setMaxWidth(200);
        moveCount.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
        timeCount.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        buttonRestart.setPrefSize(200, 50);
        buttonClose.setPrefSize(200, 50);
        buttonRestart.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        buttonClose.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        buttonPause.setPrefSize(200, 50);
        buttonPause.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));

        VBox sidebar = new VBox(moves, moveCount,time,timeCount, buttonRestart, buttonPause, buttonClose);
        sidebar.setSpacing(25);
        sidebar.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(root, Color.BLANCHEDALMOND);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        sidebar.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, null, null)));
        sidebar.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        root.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        sidebar.setPadding(new Insets(25));

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, 1200, 800, true, SceneAntialiasing.BALANCED);
        scene3d.setFill(Color.BLANCHEDALMOND);
        root3d.getChildren().add(new AmbientLight());

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
					moveCount.setText(i.toString());
                    timer.resetTimer();
					root3d.getChildren().clear();
					var cube = new RubiksCube(); 
                    // scramble before adding the move count system so scramble moves don't 
                    // get counted towards the total
                    cube.scramble();
					root3d.getChildren().addAll(cube);
					root3d.getChildren().add(new AmbientLight());
                    mouseHandler = new MouseHandler(cube);
                    scene3d.setOnMousePressed(mouseHandler);
                    scene3d.setOnMouseDragged(mouseHandler);
                    scene3d.setOnMouseReleased(mouseHandler);
                    if(buttonPause.getText() == "Resume") {
						buttonPause.setText("Pause");
					}
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
                        timer.pause();
                        mouseHandler.disable();
						buttonPause.setText("Resume");
					}
					else {
                        timer.unpause();
                        mouseHandler.enable();
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
                    stage.getOnCloseRequest().handle(null);
				} catch (Exception el) {
					el.printStackTrace();
				}
			}
		});

        var cube = new RubiksCube();
        // scramble before adding the move count system so scramble moves don't 
        // get counted towards the total
        cube.scramble();
        cube.addRotationEventHandler(e -> moveCount.setText((++i).toString()));
        root3d.getChildren().addAll(cube);

        mouseHandler = new MouseHandler(cube);
        scene3d.setOnMousePressed(mouseHandler);
        scene3d.setOnMouseDragged(mouseHandler);
        scene3d.setOnMouseReleased(mouseHandler);

        root.getChildren().addAll(scene3d, sidebar);
        

        stage.setTitle("Rubik's Cube");
        stage.setScene(scene);
        stage.show();
    }

}
