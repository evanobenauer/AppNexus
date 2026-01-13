package com.ejo.ui.manager;

import com.ejo.ui.Window;
import com.ejo.ui.Scene;
import com.ejo.ui.render.FontRenderer;
import com.ejo.util.math.Vector;

import java.awt.*;

public class DebugManager extends SceneManager {

    protected FontRenderer fontRenderer;

    public DebugManager(Scene scene) {
        super(scene);
        this.fontRenderer = new FontRenderer("Arial", Font.PLAIN,10);
    }

    @Override
    public void draw(Vector mousePos) {
        Window.DebugMode mode = scene.getWindow().getDebugMode();
        if (mode.equals(Window.DebugMode.OFF)) return;
        boolean showAdvanced = mode.equals(Window.DebugMode.DEBUG_ADVANCED);

        //Draw FPS/TPS
        Vector pos = new Vector(2,2);
        boolean label = true;
        fontRenderer.drawDynamicString(scene,(label ? "FPS: " : "") + scene.getWindow().getFPS() + (showAdvanced ? " (" + scene.getWindow().getMaxFPS() + (scene.getWindow().isVSync() ? "V" : "") + (scene.getWindow().getPerformanceMode().equals(Window.PerformanceMode.ECONOMIC) ? "E" : "") + ")" : ""),pos,Color.WHITE);
        fontRenderer.drawDynamicString(scene,(label ? "TPS: " : "") + scene.getWindow().getTPS() + (showAdvanced ? " (" + scene.getWindow().getMaxTPS() + ")" : ""),pos.getAdded(new Vector(0, fontRenderer.getFont().getSize())),Color.WHITE);
        pos.add(new Vector(0,20));

        //Draw Scene Name
        fontRenderer.drawStaticString(scene,"Scene: " + scene.getTitle(),pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw Hovered Items
        String hov = "Hov: null";
        if (!scene.getMouseHoveredHandler().getHoveredElements().isEmpty())
            hov = "Hov: " + scene.getMouseHoveredHandler().getTop() + (showAdvanced ? " " + scene.getMouseHoveredHandler().getHoveredElements().reversed().toString() : "");
        if (showAdvanced) fontRenderer.drawDynamicString(scene,hov,pos,Color.WHITE);
        else fontRenderer.drawStaticString(scene,hov,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw Mouse Pos
        int mouseXi = scene.getWindow().getMousePos().getXi();
        int mouseYi = scene.getWindow().getMousePos().getYi();
        String mousePosS = "MousePos: " + mouseXi + (showAdvanced ? "(" + Math.round(mouseXi * scene.getWindow().getUiScale()) + ")" : "") + ", " + mouseYi + (showAdvanced ? "(" + Math.round(mouseYi * scene.getWindow().getUiScale()) + ")" : "");
        fontRenderer.drawDynamicString(scene,mousePosS,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw Window Size
        int sizeXi = scene.getWindow().getSize().getXi();
        int sizeYi = scene.getWindow().getSize().getYi();
        String windowSize = "Window Size: " + sizeXi + (showAdvanced ? "(" + Math.round(sizeXi * scene.getWindow().getUiScale()) + ")" : "") + ", " + sizeYi + (showAdvanced ? "(" + Math.round(sizeYi * scene.getWindow().getUiScale()) + ")" : "");
        fontRenderer.drawStaticString(scene,windowSize,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw uiScale
        String uiScale = "UI Scale: " + (int)(scene.getWindow().getUiScale() * 100) + "%";
        fontRenderer.drawStaticString(scene,uiScale,pos,Color.WHITE);
        pos.add(new Vector(0,10));
    }

    public void onKeyPress(int key, int scancode, int action, int mods) {
        //TODO: Add debug keybinds here. Make an option to add custom debug keybinds using a list of runnables?
    }

    public void setFont(Font font) {
        this.fontRenderer = new FontRenderer(font);
    }

}
