package com.ejo.ui.element;

import com.ejo.ui.element.shape.Rectangle;
import com.ejo.ui.render.FontRenderer;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.HashMap;

public class Text extends DrawableElement {

    private static final HashMap<Font, FontRenderer> FONT_RENDERER_CACHE = new HashMap<>();

    private FontRenderer fontRenderer;

    private String text;
    private Color color;
    private Type type;

    public Text(Scene scene, Vector pos, String text, Font font, Color color, Type type) {
        super(scene, pos);
        this.text = text;
        this.color = color;
        this.type = type;
        updateFontRenderer(font);
    }

    @Override
    public void draw(Vector mousePos) {
        switch (type) {
            case STATIC -> fontRenderer.drawStaticString(text,getPos(),color);
            case DYNAMIC -> fontRenderer.drawDynamicString(text,getPos(),color);
        }
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return Rectangle.isInRectangleBoundingBox(getPos(),getSize(),mousePos);
    }

    //If a renderer has already been created for the specific font, check for that renderer. If the renderer exists,
    // then use that renderer. If the renderer does not exist, create a new one
    private void updateFontRenderer(Font font) {
        if (FONT_RENDERER_CACHE.containsKey(font)) {
            this.fontRenderer = FONT_RENDERER_CACHE.get(font);
        } else {
            FontRenderer fontRenderer = new FontRenderer(font);
            FONT_RENDERER_CACHE.put(font, fontRenderer);
            this.fontRenderer = fontRenderer;
        }
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setFont(String fontName) {
        Font currentFont = fontRenderer.getFont();
        Font font = new Font(fontName,currentFont.getStyle(),currentFont.getSize());
        if (!currentFont.equals(font)) updateFontRenderer(font);
    }

    public void setStyle(int style) {
        Font currentFont = fontRenderer.getFont();
        Font font = new Font(currentFont.getFontName(),style,currentFont.getSize());
        if (!currentFont.equals(font)) updateFontRenderer(font);
    }

    public void setFontSize(int size) {
        Font currentFont = fontRenderer.getFont();
        Font font = new Font(currentFont.getFontName(),currentFont.getStyle(),size);
        if (!currentFont.equals(font)) updateFontRenderer(font);
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
        return fontRenderer.getFont().getFontName();
    }

    public int getStyle() {
        return fontRenderer.getFont().getStyle();
    }

    public int getFontSize() {
        return fontRenderer.getFont().getSize();
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
        return new Vector(fontRenderer.getWidth(text), fontRenderer.getHeight());
    }

    public enum Type {
        STATIC,
        DYNAMIC
    }
}
