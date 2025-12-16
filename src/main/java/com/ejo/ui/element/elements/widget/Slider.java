package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.MathUtil;
import com.ejo.util.math.Vector;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Slider<T extends Number> extends SettingWidget<T> {

    private T min;
    private T max;
    private T step;

    private boolean sliding;

    Color color;

    public Slider(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T min, T max, T step, String title, String description) {
        super(scene, pos, size, container, () -> {}, title, description);
        this.sliding = false;

        this.min = min;
        this.max = max;
        this.step = step;

        this.color = color;
    }

    public Slider(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T min, T max, T step) {
        this(scene, pos, size, color, container, min, max, step, "", "");
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double step = this.step.doubleValue();
        if (sliding) updateSliderValue(mousePos);

        //Draw the background
        new RoundedRectangle(getScene(), getPos(), getSize(), WIDGET_BACKGROUND_COLOR).draw();

        int border = getSize().getYi() / 5;

        //Draw Slider Fill
        double barPercent = Math.clamp(getContainer().get().doubleValue(),min,max) / max;
        Vector size = new Vector((getSize().getX() - 2 * border) * barPercent,getSize().getY() - 2 * border).getAdded(new Vector(getContainer().get().doubleValue() == min ? 2 : 0, 0));
        int colSub = 50;
        new RoundedRectangle(getScene(),getPos().getAdded(border,border),size,new Color(Math.clamp(color.getRed() - colSub,0,255),Math.clamp(color.getGreen() - colSub,0,255),Math.clamp(color.getBlue() - colSub,0,255),150)).draw();

        //Draw the slider node //TODO: This is actually so terribly written its not funny. Fix it
        int nodeWidth = getSize().getYi() / 4;
        double nodeX = border + size.getX() - nodeWidth / 2f - 1;

        boolean isNodeOnLeftCurve = nodeX < 15;
        boolean isNodeOnRightCurve = nodeX + nodeWidth > getSize().getX() - 15;
        boolean isNodeOnCurve = isNodeOnLeftCurve || isNodeOnRightCurve;

        double sidePercent = isNodeOnLeftCurve ? nodeX / 15 : -(nodeX + nodeWidth - getSize().getX()) / 15;
        double nodeHeight = isNodeOnCurve ? getSize().getY() - 15 + (15 * sidePercent) : getSize().getY();
        double nodeYOffset = isNodeOnCurve ? getSize().getY() / 2 - nodeHeight / 2 : 0;
        new RoundedRectangle(getScene(), getPos().getAdded(new Vector(nodeX, nodeYOffset)), new Vector(nodeWidth, nodeHeight), new Color(color.getRed(), color.getGreen(), color.getBlue(), 255)).draw();

        //Draw the slider text
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        //NA
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button != 0) return;
        switch (action) {
            case GLFW.GLFW_PRESS -> {
                if (isMouseHovered()) sliding = true;
            }
            case GLFW.GLFW_RELEASE -> {
                if (sliding) sliding = false;
            }
        }
    }

    @Override
    public void onMouseScroll(int scroll, Vector mousePos) {
        // add scroll sliding
    }

    private void updateSliderValue(Vector mousePos) {
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double step = this.step.doubleValue();

        int border = getSize().getYi() / 5;

        double settingRange = max - min;

        double sliderWidth = mousePos.getX() - (getPos().getX() + border);
        double sliderPercent = Math.clamp(sliderWidth, 0, getSize().getX()) / (getSize().getX() - border * 2);

        double calculatedValue = min + sliderPercent * settingRange;
        double val = MathUtil.roundDouble((((Math.round(calculatedValue / step)) * step)), 2); //Rounds the slider based off of the step val

        T value = getContainer().get() instanceof Integer ? (T) (Integer) (int) val : (T) (Double) val;
        getContainer().set(value);
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public T getStep() {
        return step;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public void setStep(T step) {
        this.step = step;
    }
}
