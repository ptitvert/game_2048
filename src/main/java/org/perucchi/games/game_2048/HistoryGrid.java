package org.perucchi.games.game_2048;

import org.perucchi.games.game_2048.Exceptions.IllegalStringFormatException;
import org.perucchi.games.game_2048.datamodel.Grid;

import java.util.ArrayList;
import java.util.List;

public class HistoryGrid {
    private List<Grid> grids = new ArrayList<>();
    private List<Scores> scores = new ArrayList<>();

    private List<Grid> currentGridCheckPoint = null;
    private List<Scores> currentScoresCheckPoint = null;

    private Grid currentGrid = null;
    private Scores currentScore = null;
    private Boolean undoFlag = true;

    public boolean isEmpty() {
        return grids.isEmpty();
    }

    public void resetHistory() {
        currentGridCheckPoint = null;
        currentScoresCheckPoint = null;
        grids.clear();
        scores.clear();
        currentGrid = null;
        currentScore = null;
    }

    public Grid getCurrentGrid() {
        Grid myReturnGrid = null;

        if (currentGrid != null) {
            try {
                myReturnGrid = new Grid();
                myReturnGrid.setGrid(currentGrid.getGrid());
            } catch (IllegalStringFormatException e) {
                throw new RuntimeException(e);
            }
        }

        return myReturnGrid;
    }

    public Scores getCurrentScore() {
        Scores myReturnScore = null;

        if (currentScore != null) {
            myReturnScore = new Scores();
            myReturnScore.setCurrentScore(currentScore.getCurrentScore());
        }

        return myReturnScore;
    }

    public void addToHistory(Grid newGrid, Scores score) {
        List<Grid> myGrids = currentGridCheckPoint != null ? currentGridCheckPoint : grids;
        Grid gridToAdd = new Grid();
        gridToAdd.setGrid(newGrid.getRawGrid());
        myGrids.add(gridToAdd);
        currentGrid = gridToAdd;

        List<Scores> myScores = currentScoresCheckPoint != null ? currentScoresCheckPoint : scores;
        Scores scoreToAdd = new Scores();
        scoreToAdd.setCurrentScore(score.getCurrentScore());
        myScores.add(scoreToAdd);
        currentScore = scoreToAdd;
    }

    public void setCheckPoint() {
        if (currentGridCheckPoint != null) {
            for (Grid grid : currentGridCheckPoint) {
                grids.add(grid);
            }
            for (Scores score : currentScoresCheckPoint) {
                scores.add(score);
            }
        }
        currentGridCheckPoint = new ArrayList<>();
        currentScoresCheckPoint = new ArrayList<>();
    }

    public void restoreCheckPoint() {
        currentGridCheckPoint = new ArrayList<>();
        currentScoresCheckPoint = new ArrayList<>();
        currentGrid = grids.get(grids.size()-1);
        currentScore = scores.get(scores.size()-1);

    }

    public void setUndoFlag(boolean undoFlag) {
        this.undoFlag = undoFlag;
    }

    public Boolean isUndoFlagActive() {
        return undoFlag;
    }

    public void undo() {

        if (undoFlag) {

            if (currentGridCheckPoint != null) {
                if (currentGridCheckPoint.size() > 1) {
                    currentGridCheckPoint.remove(currentGridCheckPoint.size() - 1);
                    currentGrid = currentGridCheckPoint.get(currentGridCheckPoint.size() - 1);

                    currentScoresCheckPoint.remove(currentScoresCheckPoint.size() - 1);
                    currentScore = currentScoresCheckPoint.get(currentScoresCheckPoint.size() - 1);
                } else {
                    if (currentGridCheckPoint.size() == 1) {
                        currentGridCheckPoint.remove(0);

                        currentScoresCheckPoint.remove(0);
                    }
                    currentGrid = grids.get(grids.size() - 1);

                    currentScore = scores.get(scores.size() - 1);
                }
            } else {
                if (grids.size() > 1) {
                    grids.remove(grids.size() - 1);
                    currentGrid = grids.get(grids.size() - 1);

                    scores.remove(scores.size() - 1);
                    currentScore = scores.get(scores.size() - 1);
                } else {
                    currentGrid = grids.get(0);
                    currentScore = scores.get(0);
                }
            }

        }
    }

    public void toggleUndo() {
        undoFlag = undoFlag ? false : true;
    }
}
