package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.elements.Text;
import com.ejo.ui.element.elements.shape.RoundedRectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Button extends Widget {

    private String title;
    private Color color;

    private boolean pressed;

    public Button(Scene scene, Vector pos, Vector size, Color color, String title, Runnable action) {
        super(scene, pos, size, action);
        this.color = color;
        this.title = title;
    }

    public Button(Scene scene, Vector pos, Vector size, Color color, Runnable action) {
        this(scene,pos,size,color,"",action);
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        //Draw Button
        int sub = 50;
        int r = Math.clamp(color.getRed() - sub,0,255);
        int g = Math.clamp(color.getGreen() - sub,0,255);
        int b = Math.clamp(color.getBlue() - sub,0,255);
        int a = Math.clamp(color.getAlpha(),0,255);
        Color col = !pressed ? color : new Color(r,g,b,a);
        new RoundedRectangle(getScene(), getPos(), getSize(), col).draw();

        //Draw Title
        int border = getSize().getXi() / 20;

        //TODO: Deal with Horizontal titles being too large. Have an auto downscaling for the textSize
        int textSize = getSize().getYi() - border * 2;

        Text.Type type = Text.Type.STATIC;
        new Text(getScene(), getPos().getAdded(border + 2, border), getTitle(), new Font("Arial", Font.PLAIN, textSize), Color.WHITE, type).draw();
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            switch (action) {
                case GLFW.GLFW_PRESS -> {
                    if (isMouseHovered()) pressed = true;
                }
                case GLFW.GLFW_RELEASE -> {
                    if (isMouseHovered() && pressed) getAction().run();
                    pressed = false;
                }
            }
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public Color getColor() {
        return color;
    }
}
