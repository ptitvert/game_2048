package org.perucchi.games.game_2048;

import org.perucchi.games.game_2048.datamodel.Grid;

import java.util.*;

class Coordinates {
    int row = 0;
    int col = 0;

    Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    Coordinates() {
        super();
    }
}

public class GameRules {

    public static Grid initialBoard() {
        return randomPlacement(new Grid(),2);
    }

    public static Grid randomPlacement(final Grid currentGrid) {
        return randomPlacement(currentGrid, 1);
    }

    private static Grid randomPlacement(final Grid currentGrid, int numberOfRandomNumber) {
        Grid returnGrid = new Grid();
        List<List<Integer>> targetGridList = currentGrid.getRawGrid();

        List<Coordinates> currentEmptySpots = new ArrayList<>();
        for (int row = 0; row < targetGridList.size(); row++) {
            for (int col = 0; col < targetGridList.size(); col++) {
                if (targetGridList.get(row).get(col) == 0) {
                    currentEmptySpots.add(new Coordinates(row, col));
                }
            }
        }

        if (currentEmptySpots.isEmpty()) return currentGrid;

        Random rand = new Random();

        for (int i=0;i<numberOfRandomNumber;i++) {
            if (currentEmptySpots.isEmpty())
                break;

            int randomNumberToAdd = rand.nextInt(100);
            if (randomNumberToAdd < 90) {
                randomNumberToAdd = 2;
            } else {
                randomNumberToAdd = 4;
            }

            int random = rand.nextInt(currentEmptySpots.size());
            Coordinates randomCoordinates = currentEmptySpots.get(random);
            currentEmptySpots.remove(random);

            targetGridList.get(randomCoordinates.row).set(randomCoordinates.col, randomNumberToAdd);
        }
        returnGrid.setGrid(targetGridList);

        return returnGrid;
    }

    public static Grid move(final Grid currentGrid, final Scores score, final Directions direction) {
        Grid targetGrid = new Grid();

        List<List<Integer>> sourceGridList = currentGrid.getRawGrid();
        List<List<Integer>> targetGridList = targetGrid.getRawGrid();

        switch (direction) {
            case UP: {
                transposeList(sourceGridList);
                targetGridList = genericMove(sourceGridList, score);
                transposeList(targetGridList);
                break;
            }

            case DOWN: {
                transposeList(sourceGridList);
                sourceGridList.forEach(Collections::reverse);
                targetGridList = genericMove(sourceGridList, score);
                targetGridList.forEach(Collections::reverse);
                transposeList(targetGridList);
                break;
            }

            case LEFT: {
                targetGridList = genericMove(sourceGridList, score);
                break;
            }

            case RIGHT: {
                sourceGridList.forEach(Collections::reverse);
                targetGridList = genericMove(sourceGridList, score);
                targetGridList.forEach(Collections::reverse);
                break;
            }
        }

        targetGrid.setGrid(targetGridList);
        return targetGrid;
    }

    private static void transposeList(List<List<Integer>> sourceGridList) {
        for (int row = 0; row < sourceGridList.size() - 1; row++) {
            for (int col = row + 1; col < sourceGridList.size(); col++) {
                Integer a = sourceGridList.get(row).get(col);
                Integer b = sourceGridList.get(col).get(row);
                sourceGridList.get(row).set(col, b);
                sourceGridList.get(col).set(row, a);
            }
        }
    }

    private static List<List<Integer>> genericMove(final List<List<Integer>> sourceGridList, final Scores score) {
        List<List<Integer>> returnTarget = new ArrayList<>();
        for (int col = 0; col < sourceGridList.size(); col++) {
            returnTarget.add(new ArrayList<>());
            for (int row = 0; row < sourceGridList.size(); row++) {
                returnTarget.get(col).add(0);
            }
        }
        for (int currentRow = 0; currentRow < sourceGridList.size(); currentRow++) {
            List<Integer> sourceRow = sourceGridList.get(currentRow);
            List<Integer> targetRow = returnTarget.get(currentRow);

            // Move all columns that are not 0 to the left
            int targetColumn = 0;
            for (int currentColumn = 0; currentColumn < sourceRow.size(); currentColumn++) {
                Integer currentValue = sourceRow.get(currentColumn);
                if (currentValue == 0) continue;
                targetRow.set(targetColumn++, currentValue);
            }

            // Merge similar Cells together
            for (int currentColumn = 0; currentColumn < targetRow.size() - 1; currentColumn++) {
                Integer currentValue = targetRow.get(currentColumn);
                if (currentValue == 0) {
                    targetColumn = currentColumn;
                    break;
                }

                Integer nextValue = targetRow.get(currentColumn + 1);
                if (currentValue.equals(nextValue)) {
                    targetRow.set(currentColumn, currentValue * 2);
                    score.addScore(currentValue);
                    targetRow.remove(currentColumn + 1);
                    targetRow.add(0);
                }
            }

            // Put the rest of the columns to zero, should already be the case
            // but we never know!!!
            for (int col = targetColumn; col < targetRow.size(); col++) {
                targetRow.set(col, 0);
            }
        }
        return returnTarget;
    }
}
