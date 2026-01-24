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

    public static <T> void forEach(T[] list, Action<T> action) {
        for (T t : list) action.run(t);
    }

    public static <T> void forI(T[] list, Action<T> action) {
        for (int i = 0; i < list.length; i++) {
            action.run(list[i]);
        }
    }

    public static <T> void forIReversed(T[] list, Action<T> action) {
        for (int i = list.length - 1; i >= 0; i--) {
            action.run(list[i]);
        }
    }

    @FunctionalInterface
    public interface Action<T> {
        void run(T t);
    }

}
