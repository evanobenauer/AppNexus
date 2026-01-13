package com.ejo.ui.element.base;

import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.util.math.Vector;

public interface IHoverable {

    //private boolean mouseHovered;

    boolean getMouseHoveredCalculation(Vector mousePos);

    void updateMouseHovered(MouseHoveredHandler handler, Vector mousePos);

    boolean isMouseHovered(); //{return mouseHovered;}

}