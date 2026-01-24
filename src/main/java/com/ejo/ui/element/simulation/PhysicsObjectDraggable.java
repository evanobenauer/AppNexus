package com.ejo.ui.element.simulation;

import com.ejo.ui.element.DrawableElement;
import com.ejo.ui.element.base.Interactable;
import com.ejo.ui.element.shape.ConvexPolygon;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;
import org.lwjgl.glfw.GLFW;

public class PhysicsObjectDraggable extends PhysicsObject implements Interactable {

    private boolean dragging;

    public PhysicsObjectDraggable(Scene scene, Vector pos, DrawableElement element) {
        super(scene, pos, element);
        this.dragging = false;
    }

    @Override
    public void tick(Vector mousePos) {
        if (dragging) {
            setPos(mousePos);
            setVelocity(Vector.NULL());
        }
        super.tick(mousePos);
    }

    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {

    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (isMouseHovered() && button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS)
            dragging = true;

        if (action == GLFW.GLFW_RELEASE)
            dragging = false;
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {

    }
}
