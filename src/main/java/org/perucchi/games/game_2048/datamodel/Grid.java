package org.perucchi.games.game_2048.datamodel;

import org.perucchi.games.game_2048.Exceptions.IllegalStringFormatException;

import java.util.*;

public class Grid {
    private static int gridSize = 4;
    private Integer[][] grid;

    public Grid() {
        this(gridSize);
    }

    public Grid(int gridSize) {
        super();
        Grid.gridSize = gridSize;
        this.grid = new Integer[Grid.gridSize][];
        for (int row = 0; row < Grid.gridSize; row++) {
            this.grid[row] = new Integer[Grid.gridSize];
            for (int cell = 0; cell < Grid.gridSize; cell++) {
                this.grid[row][cell] = 0;
            }
        }
    }

    public String getGrid() {
        return getGrid(getRawGrid());
    }

    public String getGrid(List<List<Integer>> listToBeConverted) {
        StringBuilder sb = new StringBuilder();

        sb.append('{');
        StringJoiner mainArray = new StringJoiner(",");
        for (List<Integer> row : listToBeConverted) {
            StringJoiner internalArray = new StringJoiner(",", "[", "]");
            for (int cell = 0; cell < Grid.gridSize; cell++) {
                internalArray.add(String.valueOf(row.get(cell)));
            }
            mainArray.add(internalArray.toString());
        }
        sb.append(mainArray.toString());
        sb.append('}');

        return sb.toString();
    }

    Integer[][] getInternalGrid() {
        return grid;
    }

    public List<List<Integer>> getRawGrid() {
        List<List<Integer>> myReturnArray = new ArrayList<>();
        for (Integer[] myRow : grid) {
            myReturnArray.add(new ArrayList<>(Arrays.asList(myRow)));
        }
        return myReturnArray;
    }

    // Format: {[a,b,c,d],[e,f,g,h],[i,j,k,l],[m,n,o,p]}
    //
    //
    public void setGrid(String jsonGrid) throws IllegalStringFormatException {
        String[] internal = jsonGrid.split("[{}]");
        if (internal.length != 2) {
            throw new IllegalStringFormatException();
        }
        String workingString = internal[1];
        workingString = workingString.substring(1, workingString.length() - 1);
        internal = workingString.split("],\\[");
        for (int row = 0; row < Grid.gridSize; row++) {
            String[] intInternal = internal[row].split(",");
            for (int cell = 0; cell < Grid.gridSize; cell++) {
                grid[row][cell] = Integer.valueOf(intInternal[cell]);
            }
        }
    }

    public void setGrid(List<List<Integer>> targetGridList) {
        try {
            setGrid(getGrid(targetGridList));
        } catch (IllegalStringFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid1 = (Grid) o;
        return getGrid().equals(grid1.getGrid());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getInternalGrid());
    }

    public int getGridSize() {
        return gridSize;
    }

    @Override
    public String toString() {
        return getRawGrid().toString();
    }
}
