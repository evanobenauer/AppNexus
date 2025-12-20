package com.ejo.ui.scene;

import com.ejo.ui.Window;
import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.base.ITickable;
import com.ejo.ui.scene.manager.DebugManager;
import com.ejo.ui.scene.manager.MouseHoveredManager;
import com.ejo.util.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {

    private Window window;

    private final String title;

    private final ArrayList<Element> elements;
    private final ArrayList<ITickable> tickables;
    private final ArrayList<IInteractable> interactables;
    private final ArrayList<IAnimatable> animatables;

    private final MouseHoveredManager mouseHoveredManager;
    private final DebugManager debugManager;

    public Scene(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
        this.tickables = new ArrayList<>();
        this.interactables = new ArrayList<>();
        this.animatables = new ArrayList<>();

        this.mouseHoveredManager = new MouseHoveredManager();
        this.debugManager = new DebugManager(this);
    }

    // =================================================

    // ACTION FUNCTIONS - SHOULD BE INCLUDED IN YOUR SCENE FILE

    // =================================================

    public void draw() {
        for (Element element : elements) element.draw(getWindow().getMousePos());
    }

    public void tick() {
        for (ITickable e : tickables) e.tick(getWindow().getMousePos());
    }

    public void updateAnimation() {
        for (IAnimatable e : animatables) e.updateAnimation(e.getAnimationSpeed());
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

    public void updateMouseHovered() {
        for (Element element : elements) element.updateMouseHovered(getWindow().getMousePos());
        mouseHoveredManager.cycleQueuedItems();
    }

    // =================================================

    // ADD/REMOVE ELEMENT FUNCTIONS

    // =================================================

    public void addElements(Element... elements) {
        this.elements.addAll(Arrays.asList(elements));
        for (Element element : elements) {
            if (element instanceof ITickable e) tickables.add(e);
            if (element instanceof IInteractable e) interactables.add(e);
            if (element instanceof IAnimatable e) animatables.add(e);
        }
    }

    public void removeElement(Element element) {
        this.elements.remove(element);
        this.tickables.remove(element);
        this.animatables.remove(element);
        this.interactables.remove(element);
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

    public DebugManager getDebugManager() {
        return debugManager;
    }
}
