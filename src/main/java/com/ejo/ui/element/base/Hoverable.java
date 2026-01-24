package com.ejo.ui.element.base;

import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.util.math.Vector;

//IDK if im a fan of this class. Think about recombining it into DrawableElement
public abstract class Hoverable {

    private boolean mouseHovered;

    public Hoverable() {
        this.mouseHovered = false;
    }

    public abstract boolean getMouseHoveredCalculation(Vector mousePos);

    public void updateMouseHovered(MouseHoveredHandler handler, Vector mousePos) {
        if (getMouseHoveredCalculation(mousePos)) {
            handler.queueHoverable(this);
            this.mouseHovered = handler.isTop(this);
        } else {
            this.mouseHovered = false;
        }
    }

    public boolean isMouseHovered() {
        return this.mouseHovered;
    }
}
