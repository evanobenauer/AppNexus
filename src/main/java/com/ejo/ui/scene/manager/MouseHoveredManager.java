package com.ejo.ui.scene.manager;

import com.ejo.ui.element.Element;

import java.util.LinkedList;

//This class is a hub for all hovered items. The class will check and
// update if an item is hovered based on if it is the first one hovered
// in a stack to prevent overlap
public class MouseHoveredManager {

    private LinkedList<Element> hoveredElements;
    private final LinkedList<Element> queuedHoveredElements;

    public MouseHoveredManager() {
        this.hoveredElements = new LinkedList<>();
        this.queuedHoveredElements = new LinkedList<>();
    }

    public void cycleQueuedItems() {
        hoveredElements = ((LinkedList<Element>) queuedHoveredElements.clone());
        queuedHoveredElements.clear();
    }

    public void queueElement(Element element) {
        queuedHoveredElements.add(element);
    }

    //Checks if the hovered element is in the list at all.
    // This method can be used to bypass the "isFirst"
    public boolean isHovered(Element element) {
        return hoveredElements.contains(element);
    }

    //Checks if the hovered element is first in the list and should be interactable
    public boolean isTop(Element element) {
        return (!hoveredElements.isEmpty() && getTop() == element);
    }

    public Element getTop() {
        return hoveredElements.getLast();
    }

    public LinkedList<Element> getHoveredElements() {
        return hoveredElements;
    }
}
