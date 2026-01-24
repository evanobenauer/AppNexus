package com.ejo.ui.manager;

import com.ejo.ui.Scene;
import com.ejo.ui.element.base.Animatable;
import com.ejo.ui.element.base.Drawable;
import com.ejo.ui.element.base.Interactable;
import com.ejo.ui.element.base.Tickable;
import com.ejo.util.math.Vector;

//The scene manager acts as a layer on top of the scene to "manage" certain aspects that should be split into
// a separate class overlaying the scene
public abstract class SceneManager implements Drawable, Tickable, Interactable, Animatable {

    protected Scene scene;

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

    //The reason this exists is in case you want to continue using the same manager, even after changing scenes.
    // An example of why this is good, is the NotificationManager, which won't reset the overlay after a change
    // A manager with a progress bar or any other item not needed to change will be useful too.
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
