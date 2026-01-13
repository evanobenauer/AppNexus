package com.ejo.ui.manager;

import com.ejo.ui.Scene;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IDrawable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.base.ITickable;
import com.ejo.util.math.Vector;

//The scene manager acts as a layer on top of the scene to "manage" certain aspects that should be split into
// a separate class overlaying the scene
public abstract class SceneManager implements IDrawable, ITickable, IInteractable, IAnimatable {

    protected final Scene scene;

    public SceneManager(Scene scene) {
        this.scene = scene;
    }

    public void draw(Vector mousePos) {
    }

    public void tick(Vector mousePos) {
    }

    public void updateAnimation(float speed) {
    }


    public void onKeyPress(int key, int scancode, int action, int mods) {
    }

    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
    }

    public void onMouseScroll(double scroll, Vector mousePos) {
    }

}
