package com.ejo.ui.handler;

import com.ejo.ui.element.DrawableElement;

import java.util.LinkedList;

//This class is a hub for all hovered items. The class will check and
// update if an item is hovered based on if it is the first one hovered
// in a stack to prevent overlap
public class MouseHoveredHandler {

    private LinkedList<DrawableElement> hoveredElements;
    private final LinkedList<DrawableElement> queuedHoveredElements;

    public MouseHoveredHandler() {
        this.hoveredElements = new LinkedList<>();
        this.queuedHoveredElements = new LinkedList<>();
    }

    public void cycleQueuedElements() {
        hoveredElements = ((LinkedList<DrawableElement>) queuedHoveredElements.clone());
        queuedHoveredElements.clear();
    }

    public void queueElement(DrawableElement element) {
        queuedHoveredElements.add(element);
    }

    //Checks if the hovered element is in the list at all.
    // This method can be used to bypass the "isFirst"
    // Think about potentially switching to a LinkedHashSet which is better for contains. May be unnecessary.
    public boolean isHovered(DrawableElement element) {
        return hoveredElements.contains(element);
    }

    //Checks if the hovered element is first in the list and should be interactable
    public boolean isTop(DrawableElement element) {
        return (!hoveredElements.isEmpty() && getTop() == element);
    }

    //This returned a NoSuchElementException one time... Double check consistency
    public DrawableElement getTop() {
        return hoveredElements.getLast();
    }

    public LinkedList<DrawableElement> getHoveredElements() {
        return hoveredElements;
    }
}
