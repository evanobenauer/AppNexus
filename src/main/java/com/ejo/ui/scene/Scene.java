package com.ejo.ui.scene;

import com.ejo.ui.Window;
import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.builder.MouseHoveredManager;
import com.ejo.util.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {

    private Window window;

    private final String title;

    private final ArrayList<Element> elements;
    private final ArrayList<IInteractable> interactables;
    private final ArrayList<IAnimatable> animatables;

    private final MouseHoveredManager mouseHoveredManager;

    public Scene(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
        this.interactables = new ArrayList<>();
        this.animatables = new ArrayList<>();

        this.mouseHoveredManager = new MouseHoveredManager();
    }

    // =================================================

    // ACTION FUNCTIONS

    // =================================================

    public void draw() {
        for (Element element : elements) element.draw(getWindow().getMousePos());
    }

    public void tick() {
        for (Element element : elements) element.tick(getWindow().getMousePos());
    }

    public void updateMouseHovered() {
        for (Element element : elements) element.updateMouseHovered(getWindow().getMousePos());
        mouseHoveredManager.cycleQueuedItems();
    }

    public void runAnimation() {
        for (IAnimatable e : animatables) e.runAnimation();
    }

    public void onKeyPress(int key, int scancode, int action, int mods) {
        for (IInteractable e : interactables) e.onKeyPress(key,scancode,action,mods);
    }

    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        for (IInteractable e : interactables) e.onMouseClick(button, action,mods,mousePos);
    }

    public void onMouseScroll(int scroll, Vector mousePos) {
        for (IInteractable e : interactables) e.onMouseScroll(scroll,mousePos);
    }

    public void addElements(Element... elements) {
        this.elements.addAll(Arrays.asList(elements));
        for (Element element : elements) {
            if (element instanceof IAnimatable e) animatables.add(e);
            if (element instanceof IInteractable e) interactables.add(e);
        }
    }

    public void removeElement(Element element) {
        this.elements.remove(element);
        this.animatables.remove(element);
        this.interactables.remove(element);
    }

    // =================================================

    // DEBUG MENUS

    // =================================================
    //TODO: Implement this
    public void drawSimpleDebugMenu() {

    }

    public void drawAdvancedDebugMenu() {

    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void initWindow(Window window) {
        this.window = window;
    }


    public Window getWindow() {
        return window;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public String getTitle() {
        return title;
    }

    public Vector getMousePos() {
        return this.window.getMousePos();
    }

    public MouseHoveredManager getMouseHoveredManager() {
        return mouseHoveredManager;
    }
}
