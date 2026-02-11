package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.element.shape.ConvexPolygon;
import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Cycle<T> extends SettingWidget<T> {

    private final ArrayList<T> cycles;

    private Color color;

    private boolean pressingR;
    private boolean pressingL;

    private float fadeL;
    private float fadeR;

    //This constructor is different from other settings widgets. The title and description come before the container as to not disrupt the cycles variable
    @SafeVarargs
    public Cycle(Scene scene, Vector pos, Vector size, Color color, String title, String description, Container<T> container, T... cycles) {
        super(scene, pos, size, container, () -> {}, title, description);
        this.cycles = new ArrayList<>(Arrays.asList(cycles));
        this.color = color;

        this.pressingR = false;
        this.pressingL = false;

        this.fadeR = 0;
        this.fadeL = 0;
    }

    @SafeVarargs
    public Cycle(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T... cycles) {
        this(scene, pos, size, color, "","",container, cycles);
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        int border = getSize().getYi() / 5;

        //Draw Left & Right Buttons
        //-----------------------------
        int arrowSize = getSize().getYi() - border * 3;
        RoundedRectangle lRect = new RoundedRectangle(getScene(),getPos().getAdded(border,border),new Vector(getSize().getYi() - border * 2,getSize().getYi() - border * 2),ColorUtil.getWithAlpha(color,fadeL));
        lRect.draw();
        drawLArrow(lRect.getPos().getAdded(lRect.getSize().getMultiplied(.5).getAdded(-arrowSize/2 - 2,-arrowSize/2)),arrowSize,ColorUtil.getWithAlpha(Color.WHITE,fadeL));

        RoundedRectangle rRect = new RoundedRectangle(getScene(),getPos().getAdded(getSize().getX() - getSize().getYi() + border,border),new Vector(getSize().getYi() - border * 2,getSize().getYi() - border * 2),ColorUtil.getWithAlpha(color,fadeR));
        rRect.draw();
        drawRArrow(rRect.getPos().getAdded(rRect.getSize().getMultiplied(.5).getAdded(-arrowSize/2 + 2,-arrowSize/2)),arrowSize,ColorUtil.getWithAlpha(Color.WHITE,fadeR));
        //-----------------------------

        //Draw Title
        String text = getTitle().isEmpty() ? getContainer().get().toString() : getTitle() + ": " + getContainer().get();
        drawWidgetTitle(text,border * 2,true);
    }

    @Override
    public void updateAnimation(float speed) {
        super.updateAnimation(speed);

        //--------------------------------------------------------
        boolean isMouseOnRight = getScene().getMousePos().getX() > getSize().getX() / 2 + getPos().getX();
        int maxHover = 25;
        if (pressingR) {
            fadeR = AnimationUtil.getNextAnimationValue(pressingR, fadeR, 0, 255, speed * 3);
        } else {
            boolean activateR = isMouseOnRight && isMouseHovered() && fadeR < maxHover + speed;
            fadeR = AnimationUtil.getNextAnimationValue(activateR, fadeR, 0, maxHover, fadeR < maxHover ? speed / 5 : speed * 1.5f);
        }

        if (pressingL) {
            fadeL = AnimationUtil.getNextAnimationValue(pressingL, fadeL, 0, 255, speed * 3);
        } else {
            boolean activateL = !isMouseOnRight && isMouseHovered() && fadeL < maxHover + speed;
            fadeL = AnimationUtil.getNextAnimationValue(activateL, fadeL, 0, maxHover, fadeL < maxHover ? speed / 5 : speed * 1.5f);
        }
        //--------------------------------------------------------

        //TODO: Add a text left/right scrolling animation when the mode is changed
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        switch (key) {
            case GLFW.GLFW_KEY_RIGHT -> {
                switch (action) {
                    case GLFW.GLFW_PRESS -> {
                        if (isMouseHovered()) pressingR = true;
                    }
                    case GLFW.GLFW_RELEASE -> {
                        if (isMouseHovered()) doCycle(true);
                        pressingR = false;
                    }
                }
            }

            case GLFW.GLFW_KEY_LEFT -> {
                switch (action) {
                    case GLFW.GLFW_PRESS -> {
                        if (isMouseHovered()) pressingL = true;
                    }
                    case GLFW.GLFW_RELEASE -> {
                        if (isMouseHovered()) doCycle(false);
                        pressingL = false;
                    }
                }
            }
        }
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;
        //You can use this to replace left/right-clicking in favor of sided clicking
        boolean isMouseOnRight = mousePos.getX() > getSize().getX() / 2 + getPos().getX();

        if (isMouseOnRight) {
            switch (action) {
                case GLFW.GLFW_PRESS -> {
                    if (isMouseHovered()) pressingR = true;
                }
                case GLFW.GLFW_RELEASE -> {
                    if (isMouseHovered()) doCycle(true);
                    pressingR = false;
                }
            }
        }

        if (!isMouseOnRight) {
            switch (action) {
                case GLFW.GLFW_PRESS -> {
                    if (isMouseHovered()) pressingL = true;
                }
                case GLFW.GLFW_RELEASE -> {
                    if (isMouseHovered()) doCycle(false);
                    pressingL = false;
                }
            }
        }
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        if (!isMouseHovered()) return;
        pressingR = scroll < 0;
        pressingL = scroll > 0;
        doCycle(scroll < 0);
    }

    private void doCycle(boolean forward) {
        int increment = forward ? 1 : -1;
        int wrapAroundIndex = forward ? cycles.size() : - 1;
        int endIndex = forward ? 0 : cycles.size() - 1;

        int currentIndex = cycles.indexOf(getContainer().get());
        int arrayNumber = currentIndex + increment;
        if (currentIndex == -1) arrayNumber = 0;
        getContainer().set(cycles.get(arrayNumber == wrapAroundIndex ? endIndex : arrayNumber));
        getAction().run();
    }

    private void drawLArrow(Vector pos, int size, Color color) {
        int depth = size / 2;
        ConvexPolygon top = new ConvexPolygon(getScene(),pos,color,
                new Vector(size,0),
                new Vector(size - depth,size / 2),
                new Vector(0,size / 2),
                new Vector(depth,0));
        ConvexPolygon bottom = new ConvexPolygon(getScene(),pos,color,
                new Vector(size - depth,size / 2),
                new Vector(size,size),
                new Vector(depth,size),
                new Vector(0,size / 2));
        top.draw();
        bottom.draw();
    }

    private void drawRArrow(Vector pos, int size, Color color) {
        int depth = size / 2;
        ConvexPolygon top = new ConvexPolygon(getScene(),pos,color,
                new Vector(0,0),
                new Vector(size - depth,0),
                new Vector(size,size / 2),
                new Vector(depth,size / 2));
        ConvexPolygon bottom = new ConvexPolygon(getScene(),pos,color,
                new Vector(depth,size / 2),
                new Vector(size,size / 2),
                new Vector(size - depth,size),
                new Vector(0,size));
        top.draw();
        bottom.draw();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<T> getCycles() {
        return cycles;
    }

}
