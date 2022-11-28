package t8;

class Score implements Comparable<Score> {

    String name;
    int moveCount;
    String timeCount;
    int totalScore;

    Score(String name, int moveCount, String timeCount, int totalScore) {
        this.name = name;
        this.moveCount = moveCount;
        this.timeCount = timeCount;
        this.totalScore = totalScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-10d %-10s %-10d", name, moveCount, timeCount, totalScore);
        //name + ": " + moveCount + ", " + timeCount + ", " + totalScore; 
    }

    @Override
    public int compareTo(Score comparePlayer) {
        int compareScore = ((Score)comparePlayer).getTotalScore();
        return compareScore - this.totalScore;
    }

} 
