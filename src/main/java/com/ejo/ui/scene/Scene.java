package com.ejo.ui.scene;

import com.ejo.ui.Window;
import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.IAnimatable;
import com.ejo.ui.element.base.IInteractable;
import com.ejo.ui.element.base.ITickable;
import com.ejo.ui.manager.RemovalQueueManager;
import com.ejo.ui.manager.scenemanager.DebugManager;
import com.ejo.ui.manager.MouseHoveredManager;
import com.ejo.ui.manager.scenemanager.NotificationManager;
import com.ejo.ui.manager.scenemanager.SceneManager;
import com.ejo.util.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public abstract class Scene {

    private Window window;

    private final String title;

    private final ArrayList<Element> elements;
    protected final ArrayList<ITickable> tickables;
    protected final ArrayList<IInteractable> interactables;
    protected final ArrayList<IAnimatable> animatables;

    private final MouseHoveredManager mouseHoveredManager;
    private final RemovalQueueManager<Element> removalQueueManager;

    private final ArrayList<SceneManager> sceneManagers;

    public Scene(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
        this.tickables = new ArrayList<>();
        this.interactables = new ArrayList<>();
        this.animatables = new ArrayList<>();

        this.mouseHoveredManager = new MouseHoveredManager();
        this.removalQueueManager = new RemovalQueueManager(elements,tickables,interactables,animatables);

        this.sceneManagers = new ArrayList<>();
        this.sceneManagers.add(new DebugManager(this)); //The debug manager is ALWAYS first
        this.sceneManagers.add(new NotificationManager(this,30,5));
    }

    // =================================================

    // ACTION FUNCTIONS - SHOULD BE INCLUDED IN YOUR SCENE FILE.
    // THE SUPER METHODS CAN BE MODIFIED DEPENDING ON THE CIRCUMSTANCES NEEDED

    // =================================================

    //Draw Thread
    public void draw() {
        for (Element element : elements) element.draw(getWindow().getMousePos());
        removalQueueManager.cycleQueuedItems(elements);
    }

    //Draw Thread
    public void updateAnimation() {
        for (IAnimatable e : animatables) e.updateAnimation(e.getAnimationSpeed());
        removalQueueManager.cycleQueuedItems(animatables);
    }

    //Draw Thread
    public final void updateMouseHovered() {
        for (Element element : elements) element.updateMouseHovered(mouseHoveredManager,getWindow().getMousePos());
        mouseHoveredManager.cycleQueuedItems();
    }

    //Tick Thread
    public void tick() {
        for (ITickable e : tickables) e.tick(getWindow().getMousePos());
        removalQueueManager.cycleQueuedItems(tickables);
    }

    //Tick Thread
    public void onKeyPress(int key, int scancode, int action, int mods) {
        for (IInteractable e : interactables) e.onKeyPress(key,scancode,action,mods);
        removalQueueManager.cycleQueuedItems(interactables);
    }

    //Tick Thread
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        for (IInteractable e : interactables) e.onMouseClick(button, action,mods,mousePos);
        removalQueueManager.cycleQueuedItems(interactables);
    }

    //Tick Thread
    public void onMouseScroll(double scroll, Vector mousePos) {
        for (IInteractable e : interactables) e.onMouseScroll(scroll,mousePos);
        removalQueueManager.cycleQueuedItems(interactables);
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
        this.removalQueueManager.queueRemoval(element);
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void initWindow(Window window) {
        this.window = window;
    }

    //These two managers are able to be settable. This is to allow custom managers for notifications and custom debug menus.
    // MouseHoveredManager is NOT settable as it is not a Scene Manager
    // The Debug manager will always be first in order to render the debug screen on top
    public void setDebugManager(DebugManager manager) {
        this.sceneManagers.set(0,manager);
    }

    public void setNotificationManager(NotificationManager manager) {
        this.sceneManagers.set(1,manager);
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

    public RemovalQueueManager<Element> getRemovalQueueManager() {
        return removalQueueManager;
    }

    public DebugManager getDebugManager() {
        return (DebugManager) sceneManagers.getFirst();
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) sceneManagers.get(1);
    }


    //The scene managers array can be accessed and edited.
    // This is to allow for the option to add custom managers
    public ArrayList<SceneManager> getSceneManagers() {
        return sceneManagers;
    }
}
