package org.perucchi.games.game_2048;

import org.perucchi.games.game_2048.datamodel.Grid;
import org.springframework.shell.table.*;

import java.util.List;

public class DisplayGrid {
    public static String simpleDisplay(Grid myGrid) {
        List<List<Integer>> myList = myGrid.getRawGrid();
        StringBuilder sb = new StringBuilder();
        for (List<Integer> row : myList) {
            for (int cell = 0; cell < myList.size(); cell++) {
                if (row.get(cell)==0) {
                    sb.append(".");
                } else {
                    sb.append(row.get(cell));
                }
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String tableDisplay(Grid myGrid) {
        List<List<Integer>> myList = myGrid.getRawGrid();
        final int size = myList.size();

        String[][] data = new String[size][size];
        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Integer myValue = myList.get(i).get(j);
                data[i][j]=String.format("%5s", myValue==0?"":myValue.toString());

                TableBuilder.CellMatcherStub cell = tableBuilder.on(at(i, j));
                cell.addAligner(SimpleVerticalAligner.middle);
                cell.addAligner(SimpleHorizontalAligner.center);
            }
        }

        return tableBuilder.addFullBorder(BorderStyle.fancy_double).build().render(80);
    }

    private static CellMatcher at(final int theRow, final int theCol) {
        return new CellMatcher() {
            @Override
            public boolean matches(int row, int column, TableModel model) {
                return row == theRow && column == theCol;
            }
        };
    }

    private static String center(String text, int len){
        String out = String.format("%"+len+"s%s%"+len+"s", "",text,"");
        float mid = (out.length()/2);
        float start = mid - (len/2);
        float end = start + len;
        return out.substring((int)start, (int)end);
    }
}
