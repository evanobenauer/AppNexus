package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.elements.Text;
import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.setting.Container;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Toggle extends SettingWidget<Boolean> {

    protected float toggleFade;
    private Color color;

    public Toggle(Scene scene, Vector pos, Vector size, Color color, Container<Boolean> toggleContainer, String title, String description) {
        super(scene, pos, size,toggleContainer, () ->{},title,description);
        this.color = color;
        this.toggleFade = 0;

        //Set the action after in order to call getContainer(), which can change.
        // if you just call it from the constructor, it will only modify the default set container
        setAction(() -> getContainer().set(!getContainer().get()));
    }

    public Toggle(Scene scene, Vector pos, Vector size, Color color, Container<Boolean> toggleContainer) {
        this(scene,pos,size,color,toggleContainer,"","");
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        //Draw Background
        new RoundedRectangle(getScene(), getPos(), getSize(), WIDGET_BACKGROUND_COLOR).draw();

        //Draw Fill
        new RoundedRectangle(getScene(), getPos(), getSize(), new Color(color.getRed(),color.getGreen(),color.getBlue(),(int) toggleFade)).draw();

        //Draw Title
        int border = getSize().getXi() / 20;

        //TODO: Deal with Horizontal titles being too large. Have an auto downscaling for the textSize
        int textSize = getSize().getYi() - border * 2;

        Text.Type type = Text.Type.STATIC;
        Text text = new Text(getScene(), getPos().getAdded(border + 2, border), getTitle(), new Font("Arial", Font.PLAIN, textSize), Color.WHITE, type);
        text.draw();
    }


    @Override
    public void updateAnimation(float speed) {
        super.updateAnimation(speed);

        //Update the solid fill of the widget, depending on whether it is enabled or not
        toggleFade = (int) AnimationUtil.getNextAnimationValue(getContainer().get(), toggleFade, 0, 150, speed);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (!isMouseHovered()) return;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
            this.action.run();
        }
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        //NA
    }

    @Override
    public void onMouseScroll(int scroll, Vector mousePos) {
        //NA
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
