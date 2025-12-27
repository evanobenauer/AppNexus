package com.ejo.ui.element.elements;

import com.ejo.ui.element.Element;
import com.ejo.ui.element.elements.shape.Rectangle;
import com.ejo.ui.scene.manager.FontManager;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.HashMap;

public class Text extends Element {

    private static final HashMap<Font, FontManager> FONT_MANAGER_CACHE = new HashMap<>();

    private FontManager fontManager;

    private String text;
    private Color color;
    private Type type;

    public Text(Scene scene, Vector pos, String text, Font font, Color color, Type type) {
        super(scene, pos);
        this.text = text;
        this.color = color;
        this.type = type;
        updateFontManager(font);
    }

    @Override
    public void draw(Vector mousePos) {
        switch (type) {
            case STATIC -> fontManager.drawStaticString(getScene(),text,getPos(),color);
            case DYNAMIC -> fontManager.drawDynamicString(getScene(),text,getPos(),color);
        }
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return Rectangle.isInRectangleBoundingBox(getPos(),getSize(),mousePos);
    }

    //If a manager has already been created for the specific font, check for that manager. If the manager exists,
    // then use that manager. If the manager does not exist, create a new one
    private void updateFontManager(Font font) {
        if (FONT_MANAGER_CACHE.containsKey(font)) {
            this.fontManager = FONT_MANAGER_CACHE.get(font);
        } else {
            FontManager fontManager = new FontManager(font);
            FONT_MANAGER_CACHE.put(font,fontManager);
            this.fontManager = fontManager;
        }
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setFont(String fontName) {
        Font currentFont = fontManager.getFont();
        Font font = new Font(fontName,currentFont.getStyle(),currentFont.getSize());
        if (!currentFont.equals(font)) updateFontManager(font);
    }

    public void setStyle(int style) {
        Font currentFont = fontManager.getFont();
        Font font = new Font(currentFont.getFontName(),style,currentFont.getSize());
        if (!currentFont.equals(font)) updateFontManager(font);
    }

    public void setFontSize(int size) {
        Font currentFont = fontManager.getFont();
        Font font = new Font(currentFont.getFontName(),currentFont.getStyle(),size);
        if (!currentFont.equals(font)) updateFontManager(font);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getFont() {
        return fontManager.getFont().getFontName();
    }

    public int getStyle() {
        return fontManager.getFont().getStyle();
    }

    public int getFontSize() {
        return fontManager.getFont().getSize();
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Vector getSize() {
        return new Vector(fontManager.getWidth(getScene(),text),fontManager.getHeight(getScene()));
    }

    public enum Type {
        STATIC,
        DYNAMIC
    }
}
