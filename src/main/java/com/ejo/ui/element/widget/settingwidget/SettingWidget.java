package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.element.Text;
import com.ejo.ui.element.shape.Rectangle;
import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.element.widget.Widget;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.setting.Container;
import com.ejo.util.time.StopWatch;

import java.awt.*;

public abstract class SettingWidget<T> extends Widget {

    //This is where the setting is held. Whether it is a savable setting or a simple container
    private Container<T> container;

    //All setting widgets MUST have a Title and a Description
    private String title;
    private String description;

    public SettingWidget(Scene scene, Vector pos, Vector size, Container<T> container, Runnable action, String title, String description) {
        super(scene, pos, new RoundedRectangle(scene,pos,size,WIDGET_BACKGROUND_COLOR), action);
        this.container = container;

        this.title = title;
        this.description = description;
    }

    public SettingWidget(Scene scene, Vector pos, Vector size, Container<T> container, Runnable action) {
        this(scene, pos, size, container, action, "", "");
    }

    // =================================================

    // SPECIAL SETTING DRAW METHODS

    // =================================================


    protected void drawWidgetTitle(String title, int border, boolean centered) {
        drawWidgetTitle(title,border,centered,Color.WHITE);
    }

    protected void drawWidgetTitle(String title, int border, boolean centered, Color color) {
        //TODO: Deal with Horizontal titles being too large. Have an auto downscaling for the textSize
        int textSize = getSize().getYi() - border;

        Text text = new Text(getScene(), Vector.NULL(), title, new Font("Arial", Font.PLAIN, textSize), color, Text.Type.STATIC);
        if (centered) {
            text.setPos(getPos().getAdded(getSize().getX() / 2 - text.getSize().getX() / 2, getSize().getY() / 2 - textSize / 2));
        } else {
            text.setPos(getPos().getAdded(border + 2, getSize().getY() / 2 - textSize / 2)); //Left-oriented Position
        }
        text.draw();
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setSize(Vector size) {
        ((Rectangle) baseShape).setSize(size);
    }

    public void setContainer(Container<T> container) {
        this.container = container;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Vector getSize() {
        return ((Rectangle) baseShape).getSize();
    }

    public Container<T> getContainer() {
        return container;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
