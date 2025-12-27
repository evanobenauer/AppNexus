package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.elements.shape.ConvexPolygon;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;

import java.awt.*;

public abstract class Widget extends Element implements IInteractable, IAnimatable  {

    protected static final Color WIDGET_BACKGROUND_COLOR = new Color(50,50,50,175);

    protected final ConvexPolygon baseShape;

    //Every widget has some sort of action. This action is to be called whenever interacted with
    protected Runnable action;

    //Hover Highlight Variables
    private boolean drawHoverHighlight;
    protected float hoverHighlightFade;

    public Widget(Scene scene, Vector pos, ConvexPolygon baseShape, Runnable action) {
        super(scene, pos);
        this.baseShape = baseShape;

        this.action = action;

        this.drawHoverHighlight = true;
        this.hoverHighlightFade = 0;

        baseShape.setPos(pos);
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
        drawWidgetBase(mousePos);
        drawWidget(mousePos);
        if (drawHoverHighlight) drawHoverHighlight(mousePos);
    }

    protected void drawWidgetBase(Vector mousePos) {
        baseShape.draw();
    }

    protected void drawHoverHighlight(Vector mousePos) {
        ConvexPolygon hov = baseShape.clone();
        hov.setColor(ColorUtil.getWithAlpha(Color.WHITE,(int)hoverHighlightFade));
        hov.draw();
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return baseShape.getMouseHoveredCalculation(mousePos);
    }

    @Override
    public void updateAnimation(float speed) {
        //All widgets have a fade overlay that appears when hovered. This updates the fade value depending on if hovered
        this.hoverHighlightFade = AnimationUtil.getNextAnimationValue(isMouseHovered(), hoverHighlightFade, 0, 20, speed);
    }

    @Override
    public final float getAnimationSpeed() {
        return 10;
    }


    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setHoverHighlightVisible(boolean drawHoverHighlight) {
        this.drawHoverHighlight = drawHoverHighlight;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void setPos(Vector pos) {
        super.setPos(pos);
        this.baseShape.setPos(pos);
    }

    public Runnable getAction() {
        return action;
    }
}
