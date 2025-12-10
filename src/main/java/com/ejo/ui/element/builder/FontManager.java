package com.ejo.ui.element.builder;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class FontManager {

    private final HashMap<String, ByteBuffer> staticStringCache = new HashMap<>();
    private final HashMap<Character, ByteBuffer> dynamicCharCache = new HashMap<>();

    private final Font font;
    private final FontMetrics fontMetrics;

    public FontManager(Font font) {
        this.font = font;
        this.fontMetrics = generateFontMetrics();
    }

    public FontManager(String font, int style, int size) {
        this(new Font(font,style,size));
    }

    //Use this if the string is not going to be changing its text value often.
    // This method will cache the full texture file and will save on memory and processing power
    public void drawStaticString(Scene scene, String text, Vector pos, Color color) {
        drawCachedText(scene,text,pos,color,staticStringCache);
    }

    //Use this if the string is going to be changing often. It caches all characters in their own texture file
    // It will draw each character individually, which takes more memory, but will not have to regenerate the image
    // every time it changes which can be super costly
    public void drawDynamicString(Scene scene, String text, Vector pos, Color color) {
        pos = pos.clone(); //The position is cloned to avoid modifiying the original position vector
        for (char c : text.toCharArray()) {
            drawCachedText(scene,c,pos,color,dynamicCharCache);
            //Increment to the next position for the next character
            pos.add(new Vector(fontMetrics.stringWidth(String.valueOf(c)), 0));
        }
    }

    private <T> void drawCachedText(Scene scene, T text, Vector pos, Color color, HashMap<T, ByteBuffer> cache) {
        ByteBuffer cachedImage;
        if (cache.containsKey(text)) {
            cachedImage = cache.get(text);
        } else {
            cachedImage = TextureUtil.getByteBuffer(getTextBufferedImage(String.valueOf(text)));
            cache.put(text, cachedImage);
        }

        Vector size = new Vector(fontMetrics.stringWidth(String.valueOf(text)),fontMetrics.getHeight());
        TextureUtil.applyTextureColorTint(color);
        if (color.getAlpha() <= 0) return;
        TextureUtil.drawTexture(cachedImage,pos,size);
        TextureUtil.resetTextureColorTint();
    }

    protected BufferedImage getTextBufferedImage(String text) {
        if (text.isEmpty()) return null;
        int width = Math.max(1,fontMetrics.stringWidth(text));
        int height = Math.max(1,fontMetrics.getHeight());
        return TextureUtil.getBufferedImage(width,height,(graphics) -> {
            graphics.setFont(getFont());
            graphics.setColor(Color.WHITE);

            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 0;
            for (char c : text.toCharArray()) {
                graphics.drawString(String.valueOf(c), x, fontMetrics.getAscent());
                x += fontMetrics.stringWidth(String.valueOf(c));
            }
        });
    }

    private FontMetrics generateFontMetrics() {
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = tempImage.getGraphics();
        graphics.setFont(font);
        return graphics.getFontMetrics();
    }

    public Font getFont() {
        return font;
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    public int getWidth(Scene scene, String text) {
        return fontMetrics.stringWidth(text);
    }

    public int getHeight(Scene scene) {
        return fontMetrics.getFont().getSize();
    }
}
