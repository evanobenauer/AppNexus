package com.ejo.ui.element;

import com.ejo.ui.element.polygon.Rectangle;
import com.ejo.ui.element.polygon.RoundedRectangle;
import com.ejo.ui.Scene;
import com.ejo.ui.element.widget.settingwidget.SettingWidget;
import com.ejo.util.math.Vector;
import com.ejo.util.setting.Container;

import java.awt.*;

public class ProgressBar<T extends Number> extends DrawableElement {

    private String title;
    private Vector size;
    private Color color;

    private Container<T> progressContainer;
    private final double min;
    private final double max;

    private boolean showPercentage;

    public ProgressBar(Scene scene, Vector pos, Vector size, Color color, Container<T> progressContainer, double min, double max, String title) {
        super(scene,pos);
        this.title = title;
        this.size = size;
        this.progressContainer = progressContainer;
        this.min = min;
        this.max = max;
        this.color = color;

        this.showPercentage = false;
    }

    public ProgressBar(Scene scene, Vector pos, Vector size, Color color, Container<T> progressContainer, double min, double max) {
        this(scene, pos, size, color, progressContainer, min, max,"");
    }

    @Override
    public void draw(Vector mousePos) {
        //Draw Background
        new RoundedRectangle(getScene(),getPos(),size,new Color(50,50,50,175)).draw();

        int border = getSize().getYi() / 5;

        //Draw Bar Fill
        double barPercent = Math.clamp(getProgressContainer().get().doubleValue(),getMin(),getMax()) / getMax();
        Vector size = new Vector((getSize().getX() - 2 * border) * barPercent,getSize().getY() - 2 * border).getAdded(new Vector(getProgressContainer().get().doubleValue() == getMin() ? 2 : 0, 0));
        new RoundedRectangle(getScene(),getPos().getAdded(border,border),size,getColor()).draw();

        //Draw Title
        int percentage = (int)(barPercent * 100);
        String title = getTitle() + (!getTitle().isEmpty() ? ": " : "") + (showPercentage ? percentage + "%" : "");
        SettingWidget.drawWidgetTitle(getScene(),getPos(),getSize(),title,border,true,Color.WHITE);
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return Rectangle.isInRectangleBoundingBox(getPos(),getSize(),mousePos);
    }

    public void setPercentageShown(boolean shown) {
        this.showPercentage = shown;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setProgressContainer(Container<T> container) {
        this.progressContainer = container;
    }


    public String getTitle() {
        return title;
    }

    public Vector getSize() {
        return size;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public Color getColor() {
        return color;
    }

    public Container<T> getProgressContainer() {
        return progressContainer;
    }

}