package com.ejo.ui.element.elements;

import com.ejo.ui.element.Element;
import com.ejo.ui.element.elements.shape.Rectangle;
import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.setting.Container;

import java.awt.*;

public class ProgressBar<T extends Number> extends Element {

    private String title;
    private Vector size;
    private Color color;


    private Container<T> progressContainer;
    private final double min;
    private final double max;

    public ProgressBar(Scene scene, Vector pos, Vector size, Color color, Container<T> progressContainer, double min, double max, String title) {
        super(scene,pos);
        this.title = title;
        this.size = size;
        this.progressContainer = progressContainer;
        this.min = min;
        this.max = max;
        this.color = color;
    }

    public ProgressBar(Scene scene, Vector pos, Vector size, Color color, Container<T> progressContainer, double min, double max) {
        this(scene, pos, size, color, progressContainer, min, max,"");
    }

    @Override
    public void draw(Vector mousePos) {
        //Draw Background
        new RoundedRectangle(getScene(),getPos(),size,new Color(50,50,50,200)).draw();

        int border = 4;

        //Draw Bar Fill
        double barPercent = Math.clamp(getProgressContainer().get().doubleValue(),getMin(),getMax()) / getMax();
        Vector size = new Vector((getSize().getX() - 2 * border) * barPercent,getSize().getY() - 2 * border).getAdded(new Vector(getProgressContainer().get().doubleValue() == getMin() ? 2 : 0, 0));
        new RoundedRectangle(getScene(),getPos().getAdded(border,border),size,getColor()).draw();

        //Draw Title
        Text text = new Text(getScene(),Vector.NULL(),getTitle(),new Font("Arial", Font.PLAIN,getSize().getYi() - border * 2),Color.WHITE, Text.Type.STATIC);
        text.setPos(getPos().getAdded(getSize().getX() / 2 - text.getSize().getX() / 2,border - 1));
        //text.setPos(getPos().getAdded(border + 2,2)); //Left-oriented Position
        text.draw();
    }

    @Override
    public void updateMouseHovered(Vector mousePos) {
        boolean mouseOverX = mousePos.getX() >= getPos().getX() && mousePos.getX() <= getPos().getX() + getSize().getX();
        boolean mouseOverY = mousePos.getY() >= getPos().getY() && mousePos.getY() <= getPos().getY() + getSize().getY();
        setHovered(mouseOverX && mouseOverY);
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