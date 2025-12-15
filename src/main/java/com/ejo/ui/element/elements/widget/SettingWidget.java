package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.elements.Text;
import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
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

    //Tooltips (Idk if i want to keep tooltips on SettingWidget)
    //TODO: Think about simply rerouting it to a separate element and removing all trace from here
    private final StopWatch tooltipHoverWatch;
    private boolean tooltipVisible;

    public SettingWidget(Scene scene, Vector pos, Vector size, Container<T> container, Runnable action, String title, String description) {
        super(scene, pos, size, action);
        this.container = container;

        this.title = title;
        this.description = description;

        this.tooltipHoverWatch = new StopWatch();
        this.tooltipVisible = false;
    }

    public SettingWidget(Scene scene, Vector pos, Vector size, Container<T> container, Runnable action) {
        this(scene, pos, size, container, action, "", "");
    }

    @Override
    public void draw(Vector mousePos) {
        super.draw(mousePos);
        if (tooltipVisible) {

            if (isMouseHovered()) {
                tooltipHoverWatch.start();
            } else {
                tooltipHoverWatch.stop();
            }

            if (!getDescription().isEmpty() && isMouseHovered() && tooltipHoverWatch.hasTimePassedS(.5))
                drawTooltip(mousePos, getDescription(), 50, Text.Type.STATIC);
        }
    }

    //TODO: Replace this eventually with a tooltip element object. This'll do for now
    private void drawTooltip(Vector mousePos, String description, int size, Text.Type type) {
        Vector pos = mousePos.getAdded(6, -size / 2);

        int border = getSize().getXi() / 20;
        int textSize = size - border * 2;

        Text text = new Text(getScene(), pos.getAdded(border + 2, border), description, new Font("Arial", Font.PLAIN, textSize), Color.WHITE, type);

        int width = text.getSize().getXi();
        if (pos.getX() + width + border > getScene().getWindow().getSize().getX())
            pos.setX(getScene().getWindow().getSize().getX() - width - border);

        new RoundedRectangle(getScene(), pos, new Vector(width + border * 2, text.getSize().getYi() + border * 2), new Color(0,0,0,150)).draw();

        text.draw();
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setTooltipVisible(boolean drawTooltip) {
        this.tooltipVisible = drawTooltip;
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
