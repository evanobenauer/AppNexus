package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.MathUtil;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Slider<T extends Number> extends SettingWidget<T> {

    private boolean sliding;

    private T min;
    private T max;
    private T step;

    private boolean showValue;

    private Color color;


    public Slider(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T min, T max, T step, String title, String description) {
        super(scene, pos, size, container, () -> {}, title, description);
        this.min = min;
        this.max = max;
        this.step = step;

        this.color = color;

        this.sliding = false;
        this.showValue = true;
    }

    public Slider(Scene scene, Vector pos, Vector size, Color color, Container<T> container, T min, T max, T step) {
        this(scene, pos, size, color, container, min, max, step, "", "");
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();

        int border = getSize().getYi() / 5;

        if (sliding) updateSliderValue(border, mousePos);

        //Draw Slider Fill
        int colSub = 50;
        double barPercent = Math.clamp(getContainer().get().doubleValue(),min,max) / max;
        Vector size = getSize().getAdded(-2 * border, -2 * border).getScaled(barPercent,1);
        new RoundedRectangle(getScene(),getPos().getAdded(border,border),size,new Color(Math.clamp(color.getRed() - colSub,0,255),Math.clamp(color.getGreen() - colSub,0,255),Math.clamp(color.getBlue() - colSub,0,255),150)).draw();

        //Draw the slider node
        // -----------------------------------
        int halfCornerRadius = 30 / 2;
        int nodeWidth = getSize().getYi() / 4;
        double nodeX = border + size.getX() - nodeWidth / 2f - 1;

        boolean isNodeOnLeftCurve = nodeX < halfCornerRadius;
        boolean isNodeOnRightCurve = nodeX + nodeWidth > getSize().getX() - halfCornerRadius;
        boolean isNodeOnCurve = isNodeOnLeftCurve || isNodeOnRightCurve;

        double sidePercent = (isNodeOnLeftCurve ? nodeX : getSize().getX() - nodeX - nodeWidth) / halfCornerRadius;
        double nodeHeight = getSize().getY() - (isNodeOnCurve ? halfCornerRadius - (halfCornerRadius * sidePercent) : 0);
        double nodeYOffset = isNodeOnCurve ? getSize().getY() / 2 - nodeHeight / 2 : 0;
        new RoundedRectangle(getScene(), getPos().getAdded(new Vector(nodeX, nodeYOffset)), new Vector(nodeWidth, nodeHeight), ColorUtil.getWithAlpha(color,255)).draw();
        //-------------------------------

        //Draw the slider text
        String title = getTitle() + (!getTitle().isEmpty() ? ": " : "") + (showValue ? getContainer().get() : "");
        drawWidgetTitle(title,border * 2,false);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button != 0) return;
        switch (action) {
            case GLFW.GLFW_PRESS -> {
                if (isMouseHovered()) sliding = true;
            }
            case GLFW.GLFW_RELEASE -> {
                if (sliding) {
                    sliding = false;
                    getAction().run();
                }
            }
        }
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_RELEASE || (key != GLFW.GLFW_KEY_RIGHT && key != GLFW.GLFW_KEY_LEFT)) return;
        if (!isMouseHovered()) return;
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double step = this.step.doubleValue();
        double add = switch (key) {
            case GLFW.GLFW_KEY_RIGHT -> step;
            case GLFW.GLFW_KEY_LEFT -> -step;
            default -> 0;
        };
        double val = MathUtil.roundDouble(Math.clamp(getContainer().get().doubleValue() + add,min,max),5);
        setCastedContainer(val);
        getAction().run();
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        if (!isMouseHovered()) return;
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double step = this.step.doubleValue();
        double val = MathUtil.roundDouble(Math.clamp(getContainer().get().doubleValue() - (Math.round(scroll) * step),min,max),5);
        setCastedContainer(val);
        getAction().run();
    }

    private void updateSliderValue(int border, Vector mousePos) {
        double min = this.min.doubleValue();
        double max = this.max.doubleValue();
        double step = this.step.doubleValue();

        double settingRange = max - min;
        double sliderMin = getPos().getX() + border;
        double sliderMax = getSize().getX() - border * 2;

        double sliderWidth = mousePos.getX() - sliderMin;
        double sliderPercent = Math.clamp(sliderWidth, 0, sliderMax) / sliderMax;

        double calculatedValue = min + sliderPercent * settingRange;
        double val = MathUtil.roundDouble(Math.round(calculatedValue / step) * step, 5); //Rounds the slider based off of the step val

        setCastedContainer(val);
    }

    private void setCastedContainer(double val) {
        T value = getContainer().get() instanceof Integer ? (T) (Integer) (int) val : (T) (Double) val;
        getContainer().set(value);
    }

    public void setValueShown(boolean shown) {
        this.showValue = shown;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public Color getColor() {
        return color;
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

}
