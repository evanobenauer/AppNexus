package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;

import java.awt.*;

public abstract class Widget extends Element implements IInteractable, IAnimatable  {

    protected static final Color WIDGET_BACKGROUND_COLOR = new Color(50,50,50,200);

    //Widget Size? TODO: Potentially replace this with a "Base Rectangle" that handles all the backend hovering and drawing
    protected Vector size;

    //Hover Highlight Variables
    private boolean drawHoverHighlight;
    protected float hoverHighlightFade;

    //Action
    protected Runnable action;

    public Widget(Scene scene, Vector pos, Vector size, Runnable action) {
        super(scene, pos);
        this.size = size;

        this.drawHoverHighlight = true;
        this.hoverHighlightFade = 0;

        this.action = action;
    }

    // =================================================

    // ABSTRACT METHODS

    // =================================================

    protected abstract void drawWidget(Vector mousePos);

    // =================================================

    // OVERWRITTEN DRAW/TICK FUNCTIONS

    // =================================================

    @Override
    public void draw(Vector mousePos) {
        drawWidget(mousePos);
        if (drawHoverHighlight) drawHoverHighlight(mousePos);
    }

    private void drawHoverHighlight(Vector mousePos) {
        new RoundedRectangle(getScene(),getPos(),getSize(),new Color(255,255,255,(int) hoverHighlightFade)).draw();
    }

    @Override
    public void updateMouseHovered(Vector mousePos) {
        boolean mouseHoveredX = mousePos.getX() >= getPos().getX() && mousePos.getX() <= getPos().getX() + getSize().getX();
        boolean mouseHoveredY = mousePos.getY() >= getPos().getY() && mousePos.getY() <= getPos().getY() + getSize().getY();
        setHovered(mouseHoveredX && mouseHoveredY);
    }

    @Override
    public void updateAnimation(float speed) {
        this.hoverHighlightFade = AnimationUtil.getNextAnimationValue(isMouseHovered(), hoverHighlightFade, 0, 50, speed);
    }

    @Override
    public float getAnimationSpeed() {
        return 10;
    }


    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setHoverHighlightVisible(boolean drawHoverHighlight) {
        this.drawHoverHighlight = drawHoverHighlight;
    }

    public void setSize(Vector vector) {
        this.size = vector;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }


    public Vector getSize() {
        return size;
    }

    public Runnable getAction() {
        return action;
    }

}
