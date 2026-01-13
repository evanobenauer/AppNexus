package com.ejo.ui;

import com.ejo.ui.element.DrawableElement;
import com.ejo.ui.element.base.*;
import com.ejo.ui.manager.DebugManager;
import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.ui.manager.SceneManager;
import com.ejo.util.math.Vector;
import com.ejo.util.lists.QueueableArrayList;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {

    private Window window;

    private final String title;

    //The scene is made up of multiple lists for each element base.
    // All lists are kept in sync through the addElement and removeElement methods.
    // All lists are QueueableArrayLists. So they have the ability to have easy and SAFE addition/removals
    protected final QueueableArrayList<DrawableElement> drawableElements; //0
    protected final QueueableArrayList<IHoverable> hoverables; //1
    protected final QueueableArrayList<ITickable> tickables; //2
    protected final QueueableArrayList<IInteractable> interactables; //3
    protected final QueueableArrayList<IAnimatable> animatables; //4

    private final MouseHoveredHandler mouseHoveredHandler;

    private final ArrayList<SceneManager> sceneManagers;

    public Scene(String title) {
        this.title = title;
        this.drawableElements = new QueueableArrayList<>();
        this.tickables = new QueueableArrayList<>();
        this.interactables = new QueueableArrayList<>();
        this.animatables = new QueueableArrayList<>();
        this.hoverables = new QueueableArrayList<>();

        this.mouseHoveredHandler = new MouseHoveredHandler();

        this.sceneManagers = new ArrayList<>();
        this.sceneManagers.add(new DebugManager(this)); //The debug manager is ALWAYS added
    }

    // =================================================

    // ACTION FUNCTIONS - SHOULD BE INCLUDED IN YOUR SCENE FILE.
    // THE SUPER METHODS CAN BE MODIFIED DEPENDING ON THE CIRCUMSTANCES NEEDED

    // =================================================

    //Draw Thread
    public void draw() {
        drawableElements.forIQueued((e) -> e.draw(getWindow().getMousePos()));
    }

    //Draw Thread
    public void updateAnimation() {
        animatables.forIQueued((e) -> e.updateAnimation(e.getAnimationSpeed()));
    }

    //Tick Thread
    public void tick() {
        tickables.forIQueued((e) -> e.tick(getWindow().getMousePos()));
    }

    //Tick Thread
    public void onKeyPress(int key, int scancode, int action, int mods) {
        interactables.forIQueued((e) -> e.onKeyPress(key, scancode, action, mods));
    }

    //Tick Thread
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        interactables.forIQueued((e) -> e.onMouseClick(button, action, mods, mousePos));
    }

    //Tick Thread
    public void onMouseScroll(double scroll, Vector mousePos) {
        interactables.forIQueued((e) -> e.onMouseScroll(scroll,mousePos));
    }

    //Tick Thread
    public final void updateMouseHovered() {
        for (int i = 0; i < hoverables.size(); i++)
            hoverables.get(i).updateMouseHovered(mouseHoveredHandler,getWindow().getMousePos());
        mouseHoveredHandler.cycleQueuedElements();
        hoverables.cycleQueuedElements();
    }

    // =================================================

    // ADD/REMOVE ELEMENT FUNCTIONS

    // =================================================

    public void addElements(DrawableElement... elements) {
        this.drawableElements.addAll(Arrays.asList(elements));
        for (DrawableElement element : elements) {
            this.hoverables.add(element);
            if (element instanceof ITickable e) tickables.add(e);
            if (element instanceof IInteractable e) interactables.add(e);
            if (element instanceof IAnimatable e) animatables.add(e);
        }
    }

    public void addElement(DrawableElement element, boolean queued) {
        if (queued) {
            this.drawableElements.queueAdd(element);
            this.hoverables.queueAdd(element);
            if (element instanceof ITickable e) tickables.queueAdd(e);
            if (element instanceof IInteractable e) interactables.queueAdd(e);
            if (element instanceof IAnimatable e) animatables.queueAdd(e);
        } else {
            addElements(element);
        }
    }

    public void removeElement(DrawableElement element, boolean queued) {
        if (queued) {
            this.drawableElements.queueRemove(element);
            this.hoverables.queueRemove(element);
            if (element instanceof ITickable e) tickables.queueRemove(e);
            if (element instanceof IInteractable e) interactables.queueRemove(e);
            if (element instanceof IAnimatable e) animatables.queueRemove(e);
        } else {
            this.drawableElements.remove(element);
            this.hoverables.remove(element);
            if (element instanceof ITickable e) tickables.remove(e);
            if (element instanceof IInteractable e) interactables.remove(e);
            if (element instanceof IAnimatable e) animatables.remove(e);
        }
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void initWindow(Window window) {
        this.window = window;
    }

    public void setDebugManager(DebugManager manager) {
        this.sceneManagers.set(0,manager);
    }

    public Window getWindow() {
        return window;
    }

    public String getTitle() {
        return title;
    }

    public Vector getMousePos() {
        return this.window.getMousePos();
    }

    public ArrayList<DrawableElement> getDrawableElements() {
        return drawableElements;
    }

    // =================================================

    // MANAGER GETTERS

    // =================================================

    public MouseHoveredHandler getMouseHoveredHandler() {
        return mouseHoveredHandler;
    }

    public DebugManager getDebugManager() {
        return (DebugManager) sceneManagers.getFirst();
    }

    //The scene managers array can be accessed and edited.
    // This is to allow for the option to add custom managers
    public ArrayList<SceneManager> getSceneManagers() {
        return sceneManagers;
    }
}
