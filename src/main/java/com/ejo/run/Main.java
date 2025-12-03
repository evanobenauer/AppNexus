package com.ejo.run;

import com.ejo.ui.Window;
import com.ejo.util.math.Vector;

import java.util.ArrayList;

public class Main {

    private static final Window WINDOW = new Window(
            "Window Title",
            new Vector(100,100),
            new Vector(400,400),
            new TestScene())
            .setMaxFPS(60)
            .setMaxTPS(60)
            .setEconomic(true)
            .setVSync(true);

    public static void main(String[] args) {
        //WINDOW.run();

        ArrayList<ArrayList<String>> grid = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            grid.add(new ArrayList<>(100));
        }

        grid.get(1).set(2,"hi");
    }
}