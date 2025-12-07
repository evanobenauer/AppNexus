package com.ejo.util.misc;

import java.lang.reflect.Array;

//TODO make this useful? maybe using arraylists?
public class Grid<T> {

    private T[][] grid;

    public Grid(int columns, int rows) {
        grid = (T[][]) new Object[rows][columns];
    }


    public T get(int column, int row) {
        return grid[row][column];
    }

    public void set(T value, int column, int row) {
        grid[row][column] = value;
    }

    public void print() {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                System.out.print(grid[row][column] + " ");
            }
            System.out.println();
        }
    }
}
