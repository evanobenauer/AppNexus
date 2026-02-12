package com.ejo.ui.element.widget.settingwidget;

import com.ejo.ui.element.polygon.RoundedRectangle;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Toggle extends SettingWidget<Boolean> {

    protected float toggleFade;
    private Color color;

    public Toggle(Scene scene, Vector pos, Vector size, Color color, Container<Boolean> toggleContainer, String title, String description) {
        super(scene, pos, size, toggleContainer, () -> {}, title, description);
        this.color = color;
        this.toggleFade = 0;
    }

    public Toggle(Scene scene, Vector pos, Vector size, Color color, Container<Boolean> toggleContainer) {
        this(scene, pos, size, color, toggleContainer, "", "");
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        //Draw Fill
        new RoundedRectangle(getScene(), getPos(), getSize(), ColorUtil.getWithAlpha(color,toggleFade)).draw();

        //Draw Title
        int border = getSize().getYi() / 5;
        drawWidgetTitle(getTitle(),border,true);
    }


    @Override
    public void updateAnimation(float speed) {
        super.updateAnimation(speed);

        //Update the solid fill of the widget, depending on whether it is enabled or not
        toggleFade = AnimationUtil.getNextAnimationValue(getContainer().get(), toggleFade, 0, 150, speed);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (!isMouseHovered()) return;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
            getContainer().set(!getContainer().get());
            this.action.run();
        }
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        //NA
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {
        //NA
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
