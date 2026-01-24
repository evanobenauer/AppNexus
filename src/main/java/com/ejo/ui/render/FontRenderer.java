package com.ejo.ui.render;

import com.ejo.util.math.Vector;
import com.ejo.util.misc.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class FontRenderer {

    private final HashMap<String, ByteBuffer> staticStringCache;
    private final HashMap<Character, ByteBuffer> dynamicCharCache;

    private final Font font;
    private final FontMetrics fontMetrics;

    public FontRenderer(Font font) {
        this.font = font;
        this.fontMetrics = generateFontMetrics();

        this.staticStringCache = new HashMap<>();
        this.dynamicCharCache = new HashMap<>();
    }

    public FontRenderer(String font, int style, int size) {
        this(new Font(font,style,size));
    }

    //Use this if the string is not going to be changing its text value often.
    // This method will cache the full texture file and will save on memory and processing power
    public void drawStaticString(String text, Vector pos, Color color) {
        if (text.isEmpty()) return;
        drawCachedText(text,pos,color,staticStringCache);
    }

    //Use this if the string is going to be changing often. It caches all characters in their own texture file
    // It will draw each character individually, which takes more memory, but will not have to regenerate the image
    // every time it changes which can be super costly
    public void drawDynamicString(String text, Vector pos, Color color) {
        if (text.isEmpty()) return;
        pos = pos.clone(); //The position is cloned to avoid modifying the original position vector
        for (char c : text.toCharArray()) {
            drawCachedText(c,pos,color,dynamicCharCache);
            //Increment to the next position for the next character
            pos.add(new Vector(fontMetrics.stringWidth(String.valueOf(c)), 0));
        }
    }

    private <T> void drawCachedText(T text, Vector pos, Color color, HashMap<T, ByteBuffer> cache) {
        if (color.getAlpha() <= 0) return;
        ByteBuffer cachedImage;
        if (cache.containsKey(text)) {
            cachedImage = cache.get(text);
        } else {
            cachedImage = ImageUtil.getByteBuffer(generateTextBufferedImage(String.valueOf(text)));
            cache.put(text, cachedImage);
        }
        int yOffset = switch (font.getFontName()) {
            case "Arial" -> -2;
            default -> 0;
        };
        Vector size = new Vector(fontMetrics.stringWidth(String.valueOf(text)),fontMetrics.getHeight());
        GLUtil.applyTextureColorTint(color);
        GLUtil.drawTexture(cachedImage,pos.getAdded(0,yOffset),size);
        GLUtil.resetTextureColorTint();
    }

    protected BufferedImage generateTextBufferedImage(String text) {
        if (text.isEmpty()) return null;
        int width = fontMetrics.stringWidth(text);
        int height = fontMetrics.getHeight();
        return ImageUtil.getBufferedImage(width,height,(graphics) -> {
            graphics.setFont(font);
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

    public int getWidth(String text) {
        return fontMetrics.stringWidth(text);
    }

    public int getHeight() {
        return fontMetrics.getFont().getSize();
    }
}
