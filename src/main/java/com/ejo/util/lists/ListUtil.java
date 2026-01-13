package com.ejo.util.lists;

public class ListUtil {

    public static <T> int getLargestRowSize(T[][] grid) {
        if (grid.length == 0) return 0;
        int length = grid[0].length;
        for (T[] row : grid) {
            if (row.length > length) length = row.length;
        }
        return length;
    }

    public static int getLongestStringLength(String[] list) {
        if (list.length == 0) return 0;
        int length = list[0].length();
        for (String string : list) {
            if (string.length() > length) length = string.length();
        }
        return length;
    }

}
