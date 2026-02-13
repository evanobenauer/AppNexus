package com.ejo.ui.handler;

import com.ejo.ui.Scene;
import com.ejo.ui.Window;
import com.ejo.ui.element.polygon.Rectangle;
import com.ejo.util.math.Vector;
import com.ejo.util.misc.ColorUtil;

import java.awt.*;

public class SceneTransitionHandler {

    private final Window window;

    private final float fadeSpeed;
    private boolean fadeIn;
    private float fade;

    private Scene queuedScene;

    //Init
    private float initFade;

    public SceneTransitionHandler(Window window, float fadeSpeed) {
        this.window = window;
        this.fadeSpeed = fadeSpeed;
        this.fadeIn = false;
        this.fade = 0;

        this.queuedScene = null;

        this.initFade = 255;
    }

    public void drawFade(Scene scene) {
        Rectangle fade = new Rectangle(scene, Vector.NULL(),window.getSize(), ColorUtil.getWithAlpha(Color.BLACK,this.fade));
        fade.draw();
        drawInitFade(scene);
    }

    private void drawInitFade(Scene scene) {
        if (initFade > 0) {
            System.out.println("he");
            initFade -= fadeSpeed;
            initFade = Math.clamp(initFade, 0, 255);
            Rectangle fade = new Rectangle(scene, Vector.NULL(), window.getSize(), ColorUtil.getWithAlpha(Color.BLACK, this.initFade));
            fade.draw();
        }
    }

    public void updateSceneTransition() {
        if (fadeIn) {
            if (fade < 255) {
                fade += fadeSpeed;
                fade = Math.clamp(fade,0,255);
            } else {
                window.setScene(queuedScene);
                fadeIn = false;
            }
        } else {
            if (fade > 0) {
                fade -= fadeSpeed;
                fade = Math.clamp(fade,0,255);
            } else {
                queuedScene = null;
            }
        }

    }

    public void setScene(Scene scene) {
        fadeIn = true;
        queuedScene = scene;
    }


}