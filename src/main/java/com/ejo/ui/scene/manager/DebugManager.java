package com.ejo.ui.scene.manager;

import com.ejo.ui.Window;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;

//TODO: If there is anything needed for debug keybinding, drawing, or actions. Please route it through this class
// Potentially think about adding a debug manager to each element
public class DebugManager {

    private final FontManager debugFontManager;

    private final Scene scene;

    public DebugManager(Scene scene) {
        this.scene = scene;
        this.debugFontManager = new FontManager("Arial", Font.PLAIN,10);
    }

    public void drawDebugMenu(Window.DebugMode mode) {
        if (mode.equals(Window.DebugMode.OFF)) return;
        boolean showAdvanced = mode.equals(Window.DebugMode.DEBUG_ADVANCED);

        //Draw FPS/TPS
        Vector pos = new Vector(2,2);
        boolean label = true;
        debugFontManager.drawDynamicString(scene,(label ? "FPS: " : "") + scene.getWindow().getFPS() + (showAdvanced ? " (" + scene.getWindow().getMaxFPS() + (scene.getWindow().isVSync() ? "V" : "") + (scene.getWindow().getPerformanceMode().equals(Window.PerformanceMode.ECONOMIC) ? "E" : "") + ")" : ""),pos,Color.WHITE);
        debugFontManager.drawDynamicString(scene,(label ? "TPS: " : "") + scene.getWindow().getTPS() + (showAdvanced ? " (" + scene.getWindow().getMaxTPS() + ")" : ""),pos.getAdded(new Vector(0,debugFontManager.getFont().getSize())),Color.WHITE);
        pos.add(new Vector(0,20));

        //Draw Hovered Items
        String hov = "Hov: null";
        if (!scene.getMouseHoveredManager().getHoveredElements().isEmpty())
            hov = "Hov: " + scene.getMouseHoveredManager().getTop() + (showAdvanced ? " " + scene.getMouseHoveredManager().getHoveredElements().reversed().toString() : "");
        if (showAdvanced) debugFontManager.drawDynamicString(scene,hov,pos,Color.WHITE);
        else debugFontManager.drawStaticString(scene,hov,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw Mouse Pos
        int mouseXi = scene.getWindow().getMousePos().getXi();
        int mouseYi = scene.getWindow().getMousePos().getYi();
        String mousePos = "MousePos: " + mouseXi + (showAdvanced ? "(" + Math.round(mouseXi * scene.getWindow().getUiScale()) + ")" : "") + ", " + mouseYi + (showAdvanced ? "(" + Math.round(mouseYi * scene.getWindow().getUiScale()) + ")" : "");
        debugFontManager.drawDynamicString(scene,mousePos,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw Window Size
        int sizeXi = scene.getWindow().getSize().getXi();
        int sizeYi = scene.getWindow().getSize().getYi();
        String windowSize = "Window Size: " + sizeXi + (showAdvanced ? "(" + Math.round(sizeXi * scene.getWindow().getUiScale()) + ")" : "") + ", " + sizeYi + (showAdvanced ? "(" + Math.round(sizeYi * scene.getWindow().getUiScale()) + ")" : "");
        debugFontManager.drawStaticString(scene,windowSize,pos,Color.WHITE);
        pos.add(new Vector(0,10));

        //Draw uiScale
        String uiScale = "UI Scale: " + (int)(scene.getWindow().getUiScale() * 100) + "%";
        debugFontManager.drawStaticString(scene,uiScale,pos,Color.WHITE);
        pos.add(new Vector(0,10));
    }
}
