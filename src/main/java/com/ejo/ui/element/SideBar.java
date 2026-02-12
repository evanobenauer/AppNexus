package com.ejo.ui.element;

import com.ejo.ui.Scene;
import com.ejo.ui.element.base.Animatable;
import com.ejo.ui.element.base.Hoverable;
import com.ejo.ui.element.base.Interactable;
import com.ejo.ui.element.base.Tickable;
import com.ejo.ui.element.polygon.Rectangle;
import com.ejo.ui.element.widget.Button;
import com.ejo.ui.handler.MouseHoveredHandler;
import com.ejo.ui.render.FontRenderer;
import com.ejo.ui.render.GLUtil;
import com.ejo.util.lists.QueueableArrayList;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;

import java.awt.*;
import java.util.Arrays;

//The sidebar kind of acts like mini scene. It has its own sub-element lists just like a scene
public class SideBar extends DrawableElement implements Tickable, Interactable, Animatable {

    protected final QueueableArrayList<DrawableElement> drawableElements;
    protected final QueueableArrayList<Hoverable> hoverables;
    protected final QueueableArrayList<Tickable> tickables;
    protected final QueueableArrayList<Interactable> interactables;
    protected final QueueableArrayList<Animatable> animatables;

    private final MouseHoveredHandler mouseHoveredHandler;

    private final com.ejo.ui.element.widget.Button openButton;
    private final FontRenderer titleRenderer;

    private Type type;
    private double width;
    private String title;
    private Color color;

    private boolean open;
    protected int openPercent;


    public SideBar(Scene scene, Type type, double width, Color color, String title, DrawableElement... elements) {
        super(scene, Vector.NULL());
        this.type = type;
        this.width = width;
        this.color = color;
        this.title = title;

        this.drawableElements = new QueueableArrayList<>();
        this.tickables = new QueueableArrayList<>();
        this.interactables = new QueueableArrayList<>();
        this.animatables = new QueueableArrayList<>();
        this.hoverables = new QueueableArrayList<>();

        this.mouseHoveredHandler = new MouseHoveredHandler();

        this.openButton = new com.ejo.ui.element.widget.Button(scene, Vector.NULL(), Vector.NULL(), getColor(), () -> setOpen(!isOpen()));
        this.titleRenderer = new FontRenderer(new Font("Arial",Font.PLAIN,20));

        this.open = false;
        this.openPercent = 0;

        addElements(elements);
    }

    public SideBar(Scene scene, Type type, double width, Color color, DrawableElement... elements) {
        this(scene, type, width, color, "", elements);
    }

    @Override
    public void draw(Vector mousePos) {
        updatePosition();

        updateButton(getScene());
        openButton.draw(mousePos);

        //Draw Background and Button Lines
        double borderS = 8;
        double borderL = 4;
        double size = 2;
        switch (getType()) {
            case TOP, BOTTOM -> {
                //Button Lines
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(borderS, borderL), new Vector(getOpenButton().getSize().getX() - 2 * borderS, size), Color.WHITE).draw();
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(borderS, getOpenButton().getSize().getY() / 2 - size / 2), new Vector(getOpenButton().getSize().getX() - 2 * borderS, size), Color.WHITE).draw();
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(borderS, getOpenButton().getSize().getY() - borderL - size), new Vector(getOpenButton().getSize().getX() - 2 * borderS, size), Color.WHITE).draw();
                //Background
                new Rectangle(getScene(),getPos(), new Vector(getScene().getWindow().getSize().getX(), getWidth()), getColor()).draw();
                //Title
                GLUtil.translate(getPos());
                titleRenderer.drawStaticString(getTitle(),new Vector(getScene().getWindow().getSize().getX() / 2 - (double) titleRenderer.getWidth(getTitle()) / 2,14),Color.WHITE);
                GLUtil.translate(getPos().getMultiplied(-1));
            }
            case LEFT, RIGHT -> {
                //Button Lines
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(borderL, borderS), new Vector(size, getOpenButton().getSize().getY() - 2 * borderS), Color.WHITE).draw();
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(getOpenButton().getSize().getX() / 2 - size / 2, borderS), new Vector(size, getOpenButton().getSize().getY() - 2 * borderS), Color.WHITE).draw();
                new Rectangle(getScene(),getOpenButton().getPos().getAdded(getOpenButton().getSize().getX() - borderL - size, borderS), new Vector(size, getOpenButton().getSize().getY() - 2 * borderS), Color.WHITE).draw();
                //Background
                new Rectangle(getScene(),getPos(), new Vector(getWidth(), getScene().getWindow().getSize().getY()), getColor()).draw();
                //Title
                GLUtil.translate(getPos());
                titleRenderer.drawStaticString(getTitle(),new Vector(getWidth() / 2 - (double) titleRenderer.getWidth(getTitle()) / 2,14),Color.WHITE);
                GLUtil.translate(getPos().getMultiplied(-1));
            }
        }

        //Draw Elements - TRANSLATED
        GLUtil.translate(getPos());
        drawableElements.forIQueued((e,i) -> e.draw(getTranslatedMousePos(mousePos)));
        GLUtil.translate(getPos().getMultiplied(-1));
    }

    @Override
    public void tick(Vector mousePos) {
        this.openButton.updateMouseHovered(getScene().getMouseHoveredHandler(), mousePos);

        //Update mouse hovered elements
        hoverables.forIQueued((e, i) -> e.updateMouseHovered(mouseHoveredHandler, getTranslatedMousePos(mousePos)));
        mouseHoveredHandler.cycleQueuedElements();

        //Tick Elements
        tickables.forIQueued((e, i) -> e.tick(getTranslatedMousePos(mousePos)));
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        this.openButton.onMouseClick(button, action, mods, mousePos);

        interactables.forIQueued((e,i) -> e.onMouseClick(button, action, mods, getTranslatedMousePos(mousePos)));
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        interactables.forIQueued((e,i) -> e.onMouseScroll(scroll,getTranslatedMousePos(mousePos)));
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        interactables.forIQueued((e,i) -> e.onKeyPress(key, scancode, action, mods));
    }

    @Override
    public void updateAnimation(float speed) {
        //Update button animation
        this.openButton.updateAnimation(speed * 15);

        //Update Open/Close animation
        openPercent = (int) AnimationUtil.getNextAnimationValue(isOpen(), openPercent, 0, 100, speed * 10);

        //Update animatable elements
        animatables.forIQueued((e,i) -> e.updateAnimation(e.getAnimationSpeed()));
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        switch (getType()) {
            case TOP, BOTTOM -> {
                boolean mouseOverX = mousePos.getX() >= getPos().getX() && mousePos.getX() <= getPos().getX() + getScene().getWindow().getSize().getX();
                boolean mouseOverY = mousePos.getY() >= getPos().getY() && mousePos.getY() <= getPos().getY() + getWidth();
                return mouseOverX && mouseOverY;
            }
            case LEFT, RIGHT -> {
                boolean mouseOverX = mousePos.getX() >= getPos().getX() && mousePos.getX() <= getPos().getX() + getWidth();
                boolean mouseOverY = mousePos.getY() >= getPos().getY() && mousePos.getY() <= getPos().getY() + getScene().getWindow().getSize().getY();
                return mouseOverX && mouseOverY;
            }
        }
        return false;
    }

    private void updatePosition() {
        setPos(switch (getType()) {
            case TOP -> new Vector(0, 0 - getWidth() * (100 - openPercent) / 100);
            case LEFT -> new Vector(0 - getWidth() * (100 - openPercent) / 100, 0);
            case BOTTOM -> new Vector(0, getScene().getWindow().getSize().getY() - getWidth() * openPercent / 100);
            case RIGHT -> new Vector(getScene().getWindow().getSize().getX() - getWidth() * openPercent / 100, 0);
        });
    }

    private void updateButton(Scene scene) {
        int sizeS = 25;
        int sizeL = 120;
        switch (getType()) {
            case TOP -> {
                openButton.setPos(getPos().getAdded(new Vector(scene.getWindow().getSize().getX() / 2 - getOpenButton().getSize().getX() / 2, getWidth())));
                openButton.setSize(new Vector(sizeL, sizeS));
            }
            case BOTTOM -> {
                openButton.setPos(getPos().getAdded(new Vector(scene.getWindow().getSize().getX() / 2 - getOpenButton().getSize().getX() / 2, -getOpenButton().getSize().getY())));
                openButton.setSize(new Vector(sizeL, sizeS));
            }
            case LEFT -> {
                openButton.setPos(getPos().getAdded(new Vector(getWidth(), scene.getWindow().getSize().getY() / 2 - getOpenButton().getSize().getY() / 2)));
                openButton.setSize(new Vector(sizeS, sizeL));
            }
            case RIGHT -> {
                openButton.setPos(getPos().getAdded(new Vector(-getOpenButton().getSize().getX(), scene.getWindow().getSize().getY() / 2 - getOpenButton().getSize().getY() / 2)));
                openButton.setSize(new Vector(sizeS, sizeL));
            }
        }
    }

    private Vector getTranslatedMousePos(Vector mousePos) {
        return mousePos.getSubtracted(getPos());
    }

    // =================================================

    // ADD/REMOVE ELEMENT FUNCTIONS
    // DIRECTLY COPIED OVER FROM SCENE

    // =================================================

    public void addElements(DrawableElement... elements) {
        this.drawableElements.addAll(Arrays.asList(elements));
        for (DrawableElement element : elements) {
            this.hoverables.add(element);
            if (element instanceof Tickable e) tickables.add(e);
            if (element instanceof Interactable e) interactables.add(e);
            if (element instanceof Animatable e) animatables.add(e);
        }
    }

    public void addElement(DrawableElement element, boolean queued) {
        if (queued) {
            this.drawableElements.queueAdd(element);
            this.hoverables.queueAdd(element);
            if (element instanceof Tickable e) tickables.queueAdd(e);
            if (element instanceof Interactable e) interactables.queueAdd(e);
            if (element instanceof Animatable e) animatables.queueAdd(e);
        } else {
            addElements(element);
        }
    }

    public void removeElement(DrawableElement element, boolean queued) {
        if (queued) {
            this.drawableElements.queueRemove(element);
            this.hoverables.queueRemove(element);
            if (element instanceof Tickable e) tickables.queueRemove(e);
            if (element instanceof Interactable e) interactables.queueRemove(e);
            if (element instanceof Animatable e) animatables.queueRemove(e);
        } else {
            this.drawableElements.remove(element);
            this.hoverables.remove(element);
            if (element instanceof Tickable e) tickables.remove(e);
            if (element instanceof Interactable e) interactables.remove(e);
            if (element instanceof Animatable e) animatables.remove(e);
        }
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setType(Type type) {
        this.type = type;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    public Type getType() {
        return type;
    }

    public double getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public boolean isOpen() {
        return open;
    }

    public Button getOpenButton() {
        return openButton;
    }

    public QueueableArrayList<DrawableElement> getDrawableElements() {
        return drawableElements;
    }

    public enum Type {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
}