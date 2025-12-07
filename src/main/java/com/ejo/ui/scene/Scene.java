package com.ejo.ui.scene;

import com.ejo.util.math.Vector;

import java.util.ConcurrentModificationException;

public abstract class Scene {

    private String title;

    public Scene(String title) {
        this.title = title;
    }


    public void draw() {

    }

    public void tick() {

    }

    /**
     * This method is a user-input detection method. It will detect all key presses for added elements, returning the key, scancode, action, and modifiers for the key.
     * To scan for more input that the elements provided, you must call the super of this method inside your override to continue to support the added elements.
     * This method is called in the tick thread of the window.
     * @param key
     * @param scancode
     * @param action
     * @param mods
     */
    public void onKeyPress(int key, int scancode, int action, int mods) {
    }

    /**
     * This method is a user-input detection method. It will detect all mouse clicks for added elements, returning the button, action, modifiers, and mousePos for the click.
     * To scan for more input that the elements provided, you must call the super of this method inside your override to continue to support the added elements.
     * This method is called in the tick thread of the window.
     * @param button
     * @param action
     * @param mods
     * @param mousePos
     */
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {

    }

    public void onMouseScroll(int scroll, Vector mousePos) {

    }
}
