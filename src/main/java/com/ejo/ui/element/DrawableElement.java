package com.ejo.ui.element;

import com.ejo.ui.Scene;
import com.ejo.ui.element.base.IDrawable;
import com.ejo.ui.element.base.IHoverable;
import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.util.input.Mouse;
import com.ejo.util.math.Vector;

public abstract class DrawableElement implements IDrawable, IHoverable {

    private final Scene scene;

    private Vector pos;

    private boolean mouseHovered;

    public DrawableElement(Scene scene, Vector pos) {
        this.scene = scene;
        this.pos = pos;
        this.mouseHovered = false;
    }

    // =================================================

    // ABSTRACT METHODS

    // =================================================

    public abstract void draw(Vector mousePos);

    public abstract boolean getMouseHoveredCalculation(Vector mousePos);

    // =================================================

    // FUNCTIONAL METHODS

    // =================================================


    public final void draw() {
        draw(scene == null ? Mouse.NULL_POS() : scene.getWindow().getMousePos());
    }

    @Override
    public void updateMouseHovered(MouseHoveredHandler handler, Vector mousePos) {
        if (getMouseHoveredCalculation(mousePos)) {
            handler.queueElement(this);
            this.mouseHovered = handler.isTop(this);
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


    @Override
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