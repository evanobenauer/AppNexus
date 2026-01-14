package com.ejo.ui.manager;

import com.ejo.ui.Scene;
import com.ejo.ui.element.DrawableElement;
import com.ejo.ui.element.Text;
import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.element.widget.settingwidget.SettingWidget;
import com.ejo.util.math.Vector;
import com.ejo.util.time.StopWatch;

import java.awt.*;

//The purpose of this is to render all tooltips above the widgets. Only create a render if needed for the scene
public class WidgetTooltipManager extends SceneManager {

    private final float hoverDelayS;

    private final StopWatch tooltipHoverWatch;

    public WidgetTooltipManager(Scene scene, float hoverDelayS) {
        super(scene);
        this.hoverDelayS = hoverDelayS;
        this.tooltipHoverWatch = new StopWatch();
    }

    @Override
    public void draw(Vector mousePos) {
        boolean hovered = false;
        for (DrawableElement element : scene.getDrawableElements()) {
            if (!(element instanceof SettingWidget<?> widget)) continue;
            if (widget.isMouseHovered()) {
                hovered = true;
                break;
            }
        }
        for (DrawableElement element : scene.getDrawableElements()) {
            if (!(element instanceof SettingWidget<?> widget)) continue;
            if (widget.getDescription().isEmpty()) continue;
            if (hovered) {
                tooltipHoverWatch.start();
            } else {
                tooltipHoverWatch.stop();
            }

            if (!widget.getDescription().isEmpty() && widget.isMouseHovered() && tooltipHoverWatch.hasTimePassedS(hoverDelayS))
                this.drawTooltip(widget, scene.getMousePos(), widget.getDescription(), 50, Text.Type.STATIC);
        }
    }

    //TODO: Replace this eventually with a tooltip element object. This'll do for now
    // This will prevent the tooltip from rendering underneath higher items.
    @Deprecated
    private void drawTooltip(SettingWidget<?> widget, Vector mousePos, String description, int size, Text.Type type) {
        Vector pos = mousePos.getAdded(6, -size / 2);

        int border = size / 5;
        int textSize = size - border * 2;

        Text text = new Text(widget.getScene(), pos.getAdded(border + 2, border), description, new Font("Arial", Font.PLAIN, textSize), Color.WHITE, type);

        int width = text.getSize().getXi();
        if (pos.getX() + width + border > widget.getScene().getWindow().getSize().getX())
            pos.setX(widget.getScene().getWindow().getSize().getX() - width - border);

        new RoundedRectangle(widget.getScene(), pos, new Vector(width + border * 2, text.getSize().getYi() + border * 2), new Color(0, 0, 0, 150)).draw();

        text.draw();
    }

}
