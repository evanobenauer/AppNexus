package com.ejo.ui.scene;

import com.ejo.ui.Window;
import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.builder.FontManager;
import com.ejo.ui.element.builder.MouseHoveredManager;
import com.ejo.util.math.Vector;

import java.awt.*;
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

    private final FontManager debugFontManager = new FontManager("Arial", Font.PLAIN,10);
    public void drawSimpleDebugMenu() {
        //No Keybinds here. Show mouse position too
        drawFPSTPS(true,false);
    }

    public void drawAdvancedDebugMenu() {
        //Show keybinds & more
        drawFPSTPS(true,true);
    }

    private void drawFPSTPS(boolean label, boolean showMax) {
        debugFontManager.drawDynamicString(this,(label ? "FPS: " : "") + window.getFPS() + (showMax ? " (" + window.getMaxFPS() + (window.isVSync() ? "V" : "") + (window.getPerformanceMode().equals(Window.PerformanceMode.ECONOMIC) ? "E" : "") + ")" : ""),new Vector(2,2),Color.WHITE);
        debugFontManager.drawDynamicString(this,(label ? "TPS: " : "") + window.getTPS() + (showMax ? " (" + window.getMaxTPS() + ")" : ""),new Vector(2,debugFontManager.getFont().getSize() + 2),Color.WHITE);
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
