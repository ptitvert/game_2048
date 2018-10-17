package org.perucchi.games.game_2048;

public class Scores {
    private static int highScore = 0;
    private int currentScore = 0;

    public void addScore(int number) {
        currentScore += 2 * number;
        // if (currentScore > highScore) {
        //    highScore = currentScore;
        //}
    }

    public int getHighScore() {
        return highScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void setCurrentScore(int score) {
        this.currentScore=score;
    }
}
