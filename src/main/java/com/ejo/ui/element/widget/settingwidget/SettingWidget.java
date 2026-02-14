package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.element.Text;
import com.ejo.ui.element.base.Descriptable;
import com.ejo.ui.element.polygon.Rectangle;
import com.ejo.ui.element.polygon.RoundedRectangle;
import com.ejo.ui.element.widget.Widget;
import com.ejo.ui.Scene;
import com.ejo.ui.render.GLUtil;
import com.ejo.util.math.Vector;
import com.ejo.util.setting.Container;

import java.awt.*;

import static java.awt.SystemColor.text;

public abstract class SettingWidget<T> extends Widget implements Descriptable {

    protected static final Color SETTING_WIDGET_BACKGROUND_COLOR = new Color(50,50,50,175);

    //This is where the setting is held. Whether it is a savable setting or a simple container
    private Container<T> container;

    //All setting widgets MUST have a Title and a Description
    private String title;
    private String description;

    public SettingWidget(Scene scene, Vector pos, Vector size, Container<T> container, Runnable action, String title, String description) {
        super(scene, pos, new RoundedRectangle(scene,pos,size, SETTING_WIDGET_BACKGROUND_COLOR), action);
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

    //This variation is for internal access for SettingWidgets only
    protected void drawWidgetTitle(String title, int border, boolean centered, Color color) {
        drawWidgetTitle(getScene(),getPos(),getSize(),title,border,centered,color);
    }

    //This is so it may be accessed outside the widget class for non-setting widgets such as the ProgressBar or Button
    public static void drawWidgetTitle(Scene scene, Vector pos, Vector size, String title, int border, boolean centered, Color color) {
        int textSize = size.getYi() - border;

        Text text = new Text(scene, Vector.NULL(), title, new Font("Arial", Font.PLAIN, textSize), color, Text.Type.STATIC);

        if (centered) {
            //TODO: Add centered text scaling
            text.setPos(pos.getAdded(size.getX() / 2 - text.getSize().getX() / 2, size.getY() / 2 - textSize / 2));
        } else {
            double scale = 1;
            double textWidth = text.getSize().getX();
            double widgetWidth = size.getX() - border * 2;
            if (textWidth > widgetWidth) scale = widgetWidth / textWidth;
            Vector scaleV = new Vector(scale,1);
            GLUtil.textureScale(new Vector(scaleV.getX(),scaleV.getY(),1));
            text.setPos(pos.getAdded(border + 2, size.getY() / 2 - textSize / 2)); //Left-oriented Position
        }

        text.draw();
        GLUtil.textureScale(new Vector(1,1));
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
