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
    private Timer timer;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
            Launcher.class
            .getResource("styles.css")
            .toExternalForm());

        Text moves = new Text("MOVES");
        moves.getStyleClass().add("text-7xl");
        Label moveCount = new Label(i.toString());
        timer = new Timer();
        timer.startTimer();
        stage.setOnCloseRequest(e -> timer.stopTimer());
        Text time = new Text("TIME");
        time.getStyleClass().add("text-7xl");
        Label timeCount = new Label();
        timeCount.textProperty().bind(timer.getSspTime());

        moveCount.getStyleClass().addAll("text-4xl");
        timeCount.getStyleClass().addAll("text-3xl");

        buttonRestart.setPrefSize(200, 50);
        buttonClose.setPrefSize(200, 50);
        buttonPause.setPrefSize(200, 50);

        Leaderboard L = new Leaderboard();
        /**********************************************************************************
                NEED TO UPDATE MOVE, TIME, AND SCORE WHEN THE CUBE IS COMPLETED
        ***********************************************************************************/
        Score newPlayerScore = new Score("", 0, "00:00", 0);
        TextField playerTextField = new TextField("Enter Name!");

        Button leaderboardButton = new Button("Leaderboard");

        Button returnButton = new Button("Back to game");
        returnButton.setOnAction(e -> stage.setScene(scene));

        Button saveButton = new Button("Save Score");

        leaderboardButton.setOnAction(e -> {
            displayLeaderboard(stage, returnButton, saveButton, playerTextField, true, newPlayerScore);
        });

        saveButton.setOnAction(e ->{
            newPlayerScore.name = playerTextField.getText();
            L.addScoreToList(newPlayerScore);
            displayLeaderboard(stage, returnButton, saveButton, playerTextField, false, newPlayerScore);
        });

        VBox sidebar = new VBox(moves, moveCount,time,timeCount, buttonRestart, 
            buttonPause, leaderboardButton, buttonClose);
        sidebar.getStyleClass().addAll("align-top-center", "gap-sm", "p-6", "border-l-2", "border-slate-900");

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, 550, 550, true, SceneAntialiasing.BALANCED);

        root.setAlignment(scene3d, Pos.CENTER);

        root3d.getTransforms().addAll(
            new Translate(275, 275),
            new Scale(50, 50, 50)
            );
        
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
        cube.addRotationEventHandler(e -> {
            moveCount.setText((++i).toString());
            if (cube.isSolved()) {
                onSolved();
            }
        });
        root3d.getChildren().addAll(new AmbientLight(), cube);

        mouseHandler = new MouseHandler(cube);
        scene3d.setOnMousePressed(mouseHandler);
        scene3d.setOnMouseDragged(mouseHandler);
        scene3d.setOnMouseReleased(mouseHandler);

        root.setCenter(scene3d);
        root.setRight(sidebar);

        stage.setTitle("Rubik's Cube");
        stage.setScene(scene);
        stage.show();
    }

    private void onSolved() {
        timer.stopTimer();
        mouseHandler.disable();
        System.out.println(getScore());
    }

    private int getScore() {
        return (int)(timer.getTime() * i);
    }

    public void displayLeaderboard (Stage mainStage, Button returnButton, Button saveScore, TextField playerTextField, Boolean newScore, Score newPlayerScore){
        VBox leaderboardVBox = new VBox(30);
        Scene leaderboardScene = new Scene(leaderboardVBox, 1200, 800,Color.BLANCHEDALMOND);
        leaderboardVBox.setAlignment(Pos.CENTER);

        //clear leaderboard scene to display lastest scores
        leaderboardVBox.getChildren().clear();

        mainStage.setScene(leaderboardScene);

        //Leaderboard Label
        Label leaderboardLabel = new Label("LEADERBOARD!");
        leaderboardLabel.setFont(new Font("Arial", 72));

        //Feature elements
        HBox featureBox = new HBox(75);
        featureBox.setAlignment(Pos.CENTER);

        Label featureRankLabel = new Label("Rank");
        featureRankLabel.setFont(new Font("Arial", 32));
        featureRankLabel.setUnderline(true);

        Label featureNameLabel = new Label("Name");
        featureNameLabel.setFont(new Font("Arial", 32));
        featureNameLabel.setUnderline(true);

        Label featureMoveLabel = new Label("Moves");
        featureMoveLabel.setFont(new Font("Arial", 32));
        featureMoveLabel.setUnderline(true);

        Label featureTimeLabel = new Label("Time");
        featureTimeLabel.setFont(new Font("Arial", 32));
        featureTimeLabel.setUnderline(true);

        Label featureTotalScoreLabel = new Label("Score");
        featureTotalScoreLabel.setFont(new Font("Arial", 32));
        featureTotalScoreLabel.setUnderline(true);


        featureBox.setAlignment(Pos.CENTER);
        featureBox.getChildren().addAll(featureRankLabel,featureNameLabel,featureMoveLabel,featureTimeLabel,featureTotalScoreLabel);
        leaderboardVBox.getChildren().addAll(leaderboardLabel,featureBox);

        Leaderboard L = new Leaderboard();
        int rank = 1;
        Collections.sort(L.ScoreList);
        for (int i = 0; i < L.ScoreList.size() && i < 5; i++) {
            //User Elements
            HBox userHBox = new HBox(125);
            userHBox.setAlignment(Pos.CENTER);
            userHBox.setMaxWidth(1000);

            Label userRankLabel = new Label(String.valueOf(rank));
            userRankLabel.setFont(new Font("Arial", 28));

            Label userNameLabel = new Label(L.ScoreList.get(i).name);
            userNameLabel.setFont(new Font("Arial", 28));

            Label userMoveLabel = new Label(String.valueOf(L.ScoreList.get(i).moveCount));
            userMoveLabel.setFont(new Font("Arial", 28));

            Label userTimeLabel = new Label(L.ScoreList.get(i).timeCount);
            userTimeLabel.setFont(new Font("Arial", 28));

            Label userTotalScoreLabel = new Label(String.valueOf(L.ScoreList.get(i).totalScore));
            userTotalScoreLabel.setFont(new Font("Arial", 28));

            userHBox.getChildren().addAll(userRankLabel,userNameLabel,userMoveLabel,userTimeLabel,userTotalScoreLabel);
            Label scoreLabel = new Label(rank + ". "+ L.ScoreList.get(i).toString());
            scoreLabel.setFont(new Font("Arial", 30));

            leaderboardVBox.getChildren().add(userHBox);
            rank++;
        }

        //new score present
        if(newScore == true){
            HBox playerHBox = new HBox(100);
            featureBox.setAlignment(Pos.CENTER);

            Label playerRankLabel = new Label("Your Score:");
            playerRankLabel.setFont(new Font("Arial", 32));

            TextField playerNameLabel = new TextField("Enter Name!");
            playerNameLabel.setPrefWidth(100);
            playerNameLabel.setPrefHeight(50);

            Label playerMoveLabel = new Label(String.valueOf(newPlayerScore.moveCount));
            playerMoveLabel.setFont(new Font("Arial", 32));

            Label playerTimeLabel = new Label(newPlayerScore.timeCount);
            playerTimeLabel.setFont(new Font("Arial", 32));

            Label playerTotalScoreLabel = new Label(String.valueOf(newPlayerScore.totalScore));
            playerTotalScoreLabel.setFont(new Font("Arial", 32));

            playerHBox.setAlignment(Pos.CENTER);
            playerHBox.getChildren().addAll(playerRankLabel,playerTextField,playerMoveLabel,playerTimeLabel,playerTotalScoreLabel,saveScore);
            playerHBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" +
                              "-fx-border-width: 2;" + "-fx-border-insets: 5;" +
                             "-fx-border-radius: 5;" + "-fx-border-color: blue;");
            leaderboardVBox.getChildren().add(playerHBox);
        }
        leaderboardVBox.getChildren().add(returnButton);
    } //display method ends

}
