package com.ejo.ui.element;

import java.util.LinkedList;

//This class is a hub for all hovered items. The class will check and
// update if an item is hovered based on if it is th first one hovered
// in a stack to prevent overlap
public class HoveredMouseManager {

    public LinkedList<Element> hoveredElements;
    public LinkedList<Element> queuedHoveredElements;

    public HoveredMouseManager() {
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

    //Checks if the hovered element is first in the list and should be interactable
    public boolean isFirst(Element element) {
        return (!hoveredElements.isEmpty() && hoveredElements.getLast() == element);
    }
}
