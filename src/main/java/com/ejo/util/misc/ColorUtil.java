package com.ejo.util.misc;

import java.awt.*;

public class ColorUtil {

    /**
     * Returns a color leaning towards Red or Green depending on the scale. Scale is given from 0 to 1
     * @param scale
     * @return
     */
    public static Color getRedGreenScaledColor(double scale) {
        int r = (int) Math.clamp((1 - scale) * 255,0,255);
        int g = (int) Math.clamp(scale * 255,0,255);
        int b = 0;

        return new Color(r, g, b);
    }

    public static Color getWithAlpha(Color color, float alpha) {
        return new Color(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,alpha / 255f);
    }

    public static Color getRainbowColor(float rainbowSpeed, long offset, float strength, float saturation, float brightness) {
        float speed = (float)(rainbowSpeed * Math.pow(10,10));
        float hue = (float) (System.nanoTime() - offset * Math.pow(10,8)) / speed;
        long colorHex = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, (saturation / 255), (brightness / 255))), 16);
        Color color = new Color((int) colorHex);
        return new Color((float) color.getRed() / 255.0f * strength, (float) color.getGreen() / 255.0f * strength, (float) color.getBlue() / 255.0f * strength, (float) color.getAlpha() / 255.0f);
    }

    public static Color getRainbowColor(long offset) {
        return getRainbowColor(.75f,offset,1,255,255);
    }

    public static Color getColorFromString(String string) {
        switch (string.toLowerCase()) {
            case "red" -> {
                return Color.RED;
            }
            case "orange" -> {
                return Color.ORANGE;
            }
            case "yellow" -> {
                return Color.YELLOW;
            }
            case "green" -> {
                return Color.GREEN;
            }
            case "blue" -> {
                return Color.BLUE;
            }
            case "purple" -> {
                return Color.MAGENTA;
            }
            case "white" -> {
                return Color.WHITE;
            }
            case "black" -> {
                return Color.BLACK;
            }
            default -> {
                return new Color(0,0,0,0);
            }
        }
    }
}
