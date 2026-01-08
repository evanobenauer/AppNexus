package com.ejo.ui.element;

import com.ejo.ui.scene.Scene;
import com.ejo.ui.manager.MouseHoveredManager;
import com.ejo.util.input.Mouse;
import com.ejo.util.math.Vector;

public abstract class Element {

    private final Scene scene;

    private Vector pos;

    private boolean mouseHovered;

    public Element(Scene scene, Vector pos) {
        this.scene = scene;
        this.pos = pos;
        this.mouseHovered = false;
    }

    // =================================================

    // ABSTRACT METHODS

    // =================================================

    public abstract void draw(Vector mousePos);

    public final void draw() {
        draw(scene == null ? Mouse.NULL_POS() : scene.getWindow().getMousePos());
    }

    public abstract boolean getMouseHoveredCalculation(Vector mousePos);

    // =================================================

    // FUNCTIONAL METHODS

    // =================================================

    public void updateMouseHovered(MouseHoveredManager manager, Vector mousePos) {
        if (getMouseHoveredCalculation(mousePos)) {
            manager.queueElement(this);
            this.mouseHovered = manager.isTop(this);
        } else {
            this.mouseHovered = false;
        }
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setPos(Vector pos) {
        this.pos = pos;
    }


    public boolean isMouseHovered() {
        return this.mouseHovered;
    }

    public Vector getPos() {
        return this.pos;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ")";
    }

}