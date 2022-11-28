package t8;

import java.util.*;
import java.io.*;

public class Leaderboard {

    ArrayList<Score> ScoreList = new ArrayList<Score>();
    File scoreFile; 
    Scanner myReader;
    FileWriter myWriter;

    Leaderboard(){
        try {
            scoreFile = new File("scoreList.txt");
            myReader = new Scanner(scoreFile);
            while (myReader.hasNextLine()) {
              String[] splitData = myReader.nextLine().split(",");
              // for (int i = 0; i < splitData.length; i++) {
              //   System.out.print(splitData[i] + " ");
              // }
              // System.out.println();
              if (splitData.length < 4) continue;
              String name = splitData[0];
              int moveCount = Integer.parseInt(splitData[1]);
              String timeCount = splitData[2];
              int totalScore = Integer.parseInt(splitData[3]);
              ScoreList.add(new Score(name, moveCount, timeCount, totalScore));
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error opening file!");
          }
    }

    public void addScoreToList(Score newScore){
      try {
        FileWriter myWriter = new FileWriter("scoreList.txt", true);
        myWriter.write("\n"+newScore.name+","+newScore.moveCount+","+newScore.timeCount+","+newScore.totalScore);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred while writing to file.");
        e.printStackTrace();
      }
    }
    
}
