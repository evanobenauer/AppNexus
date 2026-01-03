package com.ejo.ui.element.elements.widget;

import com.ejo.ui.element.elements.Text;
import com.ejo.ui.element.elements.shape.Rectangle;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.AnimationUtil;
import com.ejo.util.misc.ColorUtil;
import com.ejo.util.setting.Container;
import com.ejo.util.time.StopWatch;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class TextInput extends SettingWidget<String> {

    private Mode mode;

    private boolean typing;
    private int charLimit;

    private boolean blinking;
    private float colorFade;
    private float cursorFade;

    private final StopWatch cursorTimer;

    public TextInput(Scene scene, Vector pos, Vector size, Container<String> container, Mode mode, String title, String description) {
        super(scene, pos, size, container, () -> {
        }, title, description);
        this.mode = mode;

        this.charLimit = 10000;
        this.typing = false;

        this.blinking = false;
        this.colorFade = 255;
        this.cursorFade = 0;

        this.cursorTimer = new StopWatch();
    }

    public TextInput(Scene scene, Vector pos, Vector size, Container<String> container, Mode mode) {
        this(scene, pos, size, container, mode, "", "");
    }

    @Override
    protected void drawWidget(Vector mousePos) {
        int border = getSize().getYi() / 5;

        String title = getTitle() + (!getTitle().isEmpty() ? ": " : "") + getContainer().get();
        Text text = new Text(null,null,title,new Font("Arial", Font.PLAIN, getSize().getYi() - border),null,null);

        //Draw Blinking Cursor
        if (typing) {
            cursorTimer.start();
            if (cursorTimer.hasTimePassedS(1)) cursorTimer.restart();
            blinking = cursorTimer.hasTimePassedMS(500);

            double x = getPos().getXi() + border * 1.5 + text.getSize().getXi();

            Rectangle cursor = new Rectangle(getScene(), new Vector(x, getPos().getY() + border), new Vector(1, getSize().getY() - border * 2), ColorUtil.getWithAlpha(Color.WHITE, cursorFade));
            cursor.draw();
        } else {
            cursorTimer.restart();
        }

        Color c = new Color(colorFade / 255f,1f,colorFade / 255f,1f);
        drawWidgetTitle(title, border, false,c);
    }

    @Override
    public void updateAnimation(float speed) {
        super.updateAnimation(speed);
        cursorFade = AnimationUtil.getNextAnimationValue(blinking,cursorFade,0,255,speed * 4);
        colorFade = AnimationUtil.getNextAnimationValue(!typing,colorFade,0,255,speed * 2);
    }

    @Override
    public void onKeyPress(int key, int scancode, int action, int mods) {
        String buttonText = getContainer().get();
        if (action == GLFW.GLFW_RELEASE) return;
        if (!typing) return;
        switch (key) {
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER -> typing = false;

            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!buttonText.isEmpty())
                    buttonText = buttonText.substring(0, buttonText.length() - 1);
            }

            case GLFW.GLFW_KEY_SPACE -> {
                if (isValidPress(key))
                    buttonText += " ";
            }

            default -> {
                if (GLFW.glfwGetKeyName(key, -1) == null || GLFW.glfwGetKeyName(key, -1).equals("null")) break;
                if (isValidPress(key))
                    buttonText += GLFW.glfwGetKeyName(key, -1);
            }
        }
        if (buttonText.length() > charLimit) return;
        getContainer().set(buttonText);
    }

    @Override
    public void onMouseClick(int button, int action, int mods, Vector mousePos) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            if (typing) typing = false;
            if (isMouseHovered()) typing = true;
        }
    }

    @Override
    public void onMouseScroll(double scroll, Vector mousePos) {

    }

    private boolean isValidPress(int key) {
        if (mode == Mode.NUMBER) {
            return key == GLFW.GLFW_KEY_PERIOD ||
                    key == GLFW.GLFW_KEY_KP_SUBTRACT ||
                    key == GLFW.GLFW_KEY_MINUS ||
                    key == GLFW.GLFW_KEY_0 ||
                    key == GLFW.GLFW_KEY_1 ||
                    key == GLFW.GLFW_KEY_2 ||
                    key == GLFW.GLFW_KEY_3 ||
                    key == GLFW.GLFW_KEY_4 ||
                    key == GLFW.GLFW_KEY_5 ||
                    key == GLFW.GLFW_KEY_6 ||
                    key == GLFW.GLFW_KEY_7 ||
                    key == GLFW.GLFW_KEY_8 ||
                    key == GLFW.GLFW_KEY_9 ||
                    key == GLFW.GLFW_KEY_KP_0 ||
                    key == GLFW.GLFW_KEY_KP_1 ||
                    key == GLFW.GLFW_KEY_KP_2 ||
                    key == GLFW.GLFW_KEY_KP_3 ||
                    key == GLFW.GLFW_KEY_KP_4 ||
                    key == GLFW.GLFW_KEY_KP_5 ||
                    key == GLFW.GLFW_KEY_KP_6 ||
                    key == GLFW.GLFW_KEY_KP_7 ||
                    key == GLFW.GLFW_KEY_KP_8 ||
                    key == GLFW.GLFW_KEY_KP_9;
        }
        return true;
    }

    public void setCharLimit(int charLimit) {
        this.charLimit = charLimit;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        TEXT,
        NUMBER
    }
}
