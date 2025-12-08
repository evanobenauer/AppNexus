package com.ejo.ui.element;

import com.ejo.ui.scene.Scene;
import com.ejo.util.input.Mouse;
import com.ejo.util.math.Vector;

//TODO: Your goal with the UI system is to take elements from the minecraft system and cleanly incorporate
// This includes: Animations, tooltips, fades, and structure
// Don't forget to drag over the rounded rectangle and widget code from minecraft instead of taking it from GlowUI

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

    public abstract void tick(Vector mousePos);

    public abstract void updateMouseHovered(Vector mousePos);


    public void draw() {
        draw(scene == null ? Mouse.NULL_POS() : scene.getWindow().getMousePos());
    }

    public void tick() {
        tick(scene == null ? Mouse.NULL_POS() : scene.getWindow().getMousePos());
    }


    // =================================================

    // GETTERS/SETTERS

    // =================================================

    protected void setHovered(boolean hovered) {
        if (hovered) {
            getScene().getHoveredMouseManager().queueElement(this);
            this.mouseHovered = getScene().getHoveredMouseManager().isFirst(this);
        } else {
            this.mouseHovered = false;
        }
    }

    public Element setPos(Vector pos) {
        this.pos = pos;
        return this;
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

}