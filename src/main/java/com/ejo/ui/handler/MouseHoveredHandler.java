package com.ejo.ui.handler;

import com.ejo.ui.element.base.Hoverable;

import java.util.LinkedList;

//This class is a hub for all hovered items. The class will check and
// update if an item is hovered based on if it is the first one hovered
// in a stack to prevent overlap
public class MouseHoveredHandler {

    private LinkedList<Hoverable> hoverables;
    private final LinkedList<Hoverable> queuedHoverables;

    public MouseHoveredHandler() {
        this.hoverables = new LinkedList<>();
        this.queuedHoverables = new LinkedList<>();
    }

    public void cycleQueuedElements() {
        hoverables = ((LinkedList<Hoverable>) queuedHoverables.clone());
        queuedHoverables.clear();
    }

    public void queueHoverable(Hoverable hoverable) {
        queuedHoverables.add(hoverable);
    }

    //Checks if the hovered element is in the list at all.
    // This method can be used to bypass the "isFirst"
    // Think about potentially switching to a LinkedHashSet which is better for contains. May be unnecessary.
    public boolean isHovered(Hoverable hoverable) {
        return hoverables.contains(hoverable);
    }

    //Checks if the hovered element is first in the list and should be interactable
    public boolean isTop(Hoverable hoverable) {
        return (!hoverables.isEmpty() && getTop() == hoverable);
    }

    //This returned a NoSuchElementException one time... Double check consistency
    public Hoverable getTop() {
        return hoverables.getLast();
    }

    public LinkedList<Hoverable> getHoverables() {
        return hoverables;
    }
}
