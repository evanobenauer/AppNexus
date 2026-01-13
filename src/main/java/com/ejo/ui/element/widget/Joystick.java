package com.ejo.ui.element.widget;

import com.ejo.ui.element.base.ITickable;
import com.ejo.ui.element.shape.Circle;
import com.ejo.ui.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Joystick extends Widget implements ITickable {

    private Color color;

    private boolean dragging;

    private Circle stick;

    private Angle angle;
    private double magnitude;

    public Joystick(Scene scene, Vector pos, Color color, int radius) {
        super(scene, pos, new Circle(scene,pos,radius,WIDGET_BACKGROUND_COLOR, Circle.Quality.ULTRA), () -> {});
        this.color = color;
        this.dragging = false;

        this.stick = new Circle(scene,getPos(),radius / 1.5,color, Circle.Quality.HIGH);
        this.angle = Angle.NULL();
        this.magnitude = 0;
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        if (dragging) {
            Vector pos;
            if (mousePos.getSubtracted(getPos()).getMagnitude() < getRadius())
                pos = mousePos;
            else
                pos = mousePos.getSubtracted(getPos()).getUnitVector().getMultiplied(getRadius()).getAdded(getPos());
            stick.setPos(pos);
        } else {
            stick.setPos(getPos());
        }
        stick.draw();
    }

    @Override
    public void tick(Vector mousePos) {
        Vector vec = stick.getPos().getSubtracted(getPos());
        this.angle = vec.getTheta();
        this.magnitude = Math.clamp(vec.getMagnitude() / getRadius(),0,1);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button != 0) return;
        switch (action) {
            case GLFW.GLFW_PRESS -> {
                if (isMouseHovered()) dragging = true;
            }
            case GLFW.GLFW_RELEASE -> {
                if (dragging) dragging = false;
            }
        }
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        //NA
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        //NA
    }

    public void setColor(Color color) {
        this.stick.setColor(color);
        this.color = color;
    }

    public void setRadius(int radius) {
        ((Circle) baseShape).setRadius(radius);
    }

    public Color getColor() {
        return color;
    }

    public int getRadius() {
        return (int) ((Circle) baseShape).getRadius();
    }

    public Angle getAngle() {
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }
}
