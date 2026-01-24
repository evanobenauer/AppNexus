package com.ejo.ui.element;

import com.ejo.ui.Scene;
import com.ejo.ui.element.base.Hoverable;
import com.ejo.ui.element.base.Drawable;
import com.ejo.util.input.Mouse;
import com.ejo.util.math.Vector;

public abstract class DrawableElement extends Hoverable implements Drawable {

    private final Scene scene;

    private Vector pos;

    public DrawableElement(Scene scene, Vector pos) {
        super();
        this.scene = scene;
        this.pos = pos;
    }

    // =================================================

    // ABSTRACT METHODS
    // These are here so I don't forget they exist lol

    // =================================================

    public abstract void draw(Vector mousePos);

    // =================================================

    // FUNCTIONAL METHODS

    // =================================================


    public final void draw() {
        draw(scene == null ? Mouse.NULL_POS() : scene.getWindow().getMousePos());
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public DrawableElement setPos(Vector pos) {
        this.pos = pos;
        return this;
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

    @Override
    public DrawableElement clone() {
        try {
            return (DrawableElement) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}