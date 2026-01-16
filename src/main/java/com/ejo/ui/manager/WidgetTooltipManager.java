package com.ejo.ui.manager;

import com.ejo.ui.Scene;
import com.ejo.ui.element.DrawableElement;
import com.ejo.ui.element.shape.RoundedRectangle;
import com.ejo.ui.element.widget.settingwidget.SettingWidget;
import com.ejo.ui.render.FontRenderer;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.time.StopWatch;

import java.awt.*;
import java.util.*;

//The purpose of this is to render all tooltips above the widgets. Only create a render if needed for the scene
public class WidgetTooltipManager extends SceneManager {

    private final HashSet<Tooltip> tooltips;
    private final ArrayList<Tooltip> queuedTooltipsRemoval;

    private float hoverDelayS;

    private FontRenderer fontRenderer;

    public WidgetTooltipManager(Scene scene, int tooltipSize, float hoverDelayS) {
        super(scene);
        this.tooltips = new HashSet<>();
        this.queuedTooltipsRemoval = new ArrayList<>();

        this.hoverDelayS = hoverDelayS;
        this.fontRenderer = new FontRenderer("Arial", Font.PLAIN, tooltipSize);
    }

    // =================================================

    // USABLE METHODS

    // =================================================

    @Override
    public void draw(Vector mousePos) {
        cycleQueuedItems();

        addTooltips();

        for (Tooltip tooltip : tooltips) {
            tooltip.draw(scene, fontRenderer, mousePos);
            queueTooltipRemoval(tooltip);
        }
    }

    @Override
    public void updateAnimation(float speed) {
        for (Tooltip tooltip : tooltips) {
            tooltip.updateAnimation(speed * 20);
        }
    }

    // =================================================

    // INTERNAL METHODS

    // =================================================

    private void addTooltips() {
        for (DrawableElement element : scene.getDrawableElements()) {
            if (!(element instanceof SettingWidget<?> widget)) continue;
            if (widget.getDescription().isEmpty()) continue;

            if (widget.isMouseHovered())
                tooltips.add(new Tooltip(widget, hoverDelayS, new Color(0, 0, 0, 150)));
        }
    }

    private void queueTooltipRemoval(Tooltip tooltip) {
        if (tooltip.hoverFade <= 0) queuedTooltipsRemoval.add(tooltip);
    }

    private void cycleQueuedItems() {
        queuedTooltipsRemoval.forEach(tooltips::remove);
        queuedTooltipsRemoval.clear();
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setHoverDelay(float hoverDelayS) {
        this.hoverDelayS = hoverDelayS;
    }

    public void setFont(Font font) {
        this.fontRenderer = new FontRenderer(font);
    }

    private static class Tooltip {

        final SettingWidget<?> widget;

        final float hoverDelayS;

        final Color color;


        final StopWatch tooltipHoverWatch;

        float hoverFade;

        Tooltip(SettingWidget<?> widget, float hoverDelayS, Color color) {
            this.widget = widget;
            this.hoverDelayS = hoverDelayS;
            this.color = color;

            this.tooltipHoverWatch = new StopWatch();

            this.hoverFade = 1f;
        }

        void draw(Scene scene, FontRenderer fontRenderer, Vector mousePos) {
            int size = fontRenderer.getFont().getSize();
            int border = size / 5;

            String str = widget.getDescription();

            Vector pos = mousePos.getAdded(border, -(size + border * 2) / 2);
            int width = fontRenderer.getWidth(scene, str);
            if (pos.getX() + width + border > widget.getScene().getWindow().getSize().getX())
                pos.setX(widget.getScene().getWindow().getSize().getX() - width - border);

            Color backgroundColor = ColorUtil.getWithAlpha(color, color.getAlpha() * (hoverFade / 255));
            new RoundedRectangle(widget.getScene(), pos, new Vector(width + border * 2, fontRenderer.getHeight(null) + border * 2), backgroundColor).draw();

            fontRenderer.drawStaticString(scene, str, pos.getAdded(border, border), ColorUtil.getWithAlpha(Color.WHITE, hoverFade));
        }

        void updateAnimation(float speed) {
            if (isHoverDelayCleared()) {
                hoverFade = AnimationUtil.getNextAnimationValue(true, hoverFade, 0, 255, speed * 2);
            } else if (!widget.isMouseHovered()) {
                hoverFade = AnimationUtil.getNextAnimationValue(false, hoverFade, 0, 255, speed);
            }
        }

        boolean isHoverDelayCleared() {
            if (widget.isMouseHovered()) {
                tooltipHoverWatch.start();
            } else {
                tooltipHoverWatch.stop();
            }
            return tooltipHoverWatch.hasTimePassedS(hoverDelayS);
        }


        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Tooltip tooltip)) return false;
            return tooltip.widget.equals(widget) && tooltip.color.equals(color);
        }

        @Override
        public int hashCode() {
            return widget.hashCode() + color.hashCode();
        }
    }

}
