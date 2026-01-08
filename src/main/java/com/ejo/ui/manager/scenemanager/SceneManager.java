package com.ejo.ui.manager.scenemanager;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

public class SceneManager {

    protected final Scene scene;

    public SceneManager(Scene scene) {
        this.scene = scene;
    }

    public void draw() {

    }

    public void tick() {

    }

    public void updateAnimation(float speed) {
        //Probs implement this just for consistency
    }


    public void onKeyPress(int key, int scancode, int action, int mods) {

    }

    public void onMouseClick(int button, int action, int mods, Vector mousePos) {

    }

    public void onMouseScroll(double scroll, Vector mousePos) {

    }

}
