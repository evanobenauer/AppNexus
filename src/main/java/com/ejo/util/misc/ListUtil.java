package com.ejo.util.misc;

public class ListUtil {

    public static <T> int getLargestRowSize(T[][] grid) {
        int length = grid[0].length;
        for (T[] row : grid) {
            if (row.length > length) length = row.length;
        }
        return length;
    }

    public static int getLongestStringLength(String[] list) {
        int length = list[0].length();
        for (String string : list) {
            if (string.length() > length) length = string.length();
        }
        return length;
    }

}
