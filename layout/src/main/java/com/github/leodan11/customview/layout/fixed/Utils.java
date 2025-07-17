package com.github.leodan11.customview.layout.fixed;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<Integer> calculateMaxRowHeight(ArrayList<Integer> existHeights, SubTableLayout table) {
        for (int row = 0; row < table.getChildCount(); row++) {
            TableOnRow tableRow = (TableOnRow) table.getChildAt(row);
            if (existHeights.size() <= row) {
                // Not seen this row number before so add
                existHeights.add(tableRow.getMaxChildHeight());
            } else {
                // Take the max of existing value and new value
                existHeights.set(row, Math.max(existHeights.get(row), tableRow.getMaxChildHeight()));
            }
        }
        return existHeights;
    }

    public static void setMaxRowHeight(ArrayList<Integer> newHeights, SubTableLayout table) {
        for (int row = 0; row < table.getChildCount(); row++) {
            TableOnRow tableRow = (TableOnRow) table.getChildAt(row);
            tableRow.setMaxChildHeight(newHeights.get(row));
        }
    }

    public static ArrayList<Integer> calculateMaxColumnWidth(ArrayList<Integer> existWidths, SubTableLayout table) {
        for (int row = 0; row < table.getChildCount(); row++) {
            TableOnRow tableRow = (TableOnRow) table.getChildAt(row);
            ArrayList<Integer> rowColumnWidth = tableRow.getColumnWidths();
            for (int column = 0; column < rowColumnWidth.size(); column++) {
                if (existWidths.size() <= column) {
                    // Not seen this column number before so add
                    existWidths.add(rowColumnWidth.get(column));
                } else {
                    // Take the max of existing value and new value
                    existWidths.set(column, Math.max(existWidths.get(column), rowColumnWidth.get(column)));
                }
            }
        }
        return existWidths;
    }

    public static void setMaxColumnWidth(ArrayList<Integer> newWidths, SubTableLayout table) {
        for (int row = 0; row < table.getChildCount(); row++) {
            TableOnRow tableRow = (TableOnRow) table.getChildAt(row);
            tableRow.setColumnWidths(newWidths);
        }
    }

}