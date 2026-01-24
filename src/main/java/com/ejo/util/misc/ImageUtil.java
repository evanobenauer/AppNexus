package com.ejo.util.misc;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public class ImageUtil {

    //Note: Generating a buffered image like this is very resource heavy.
    // If done in a loop, the consequences are an exploding computer
    // Caching the buffered image is much smarter for performance
    public static BufferedImage getBufferedImage(int width, int height, DrawGraphics drawCode) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        drawCode.run(graphics);
        return image;
    }

    public static BufferedImage getBufferedImage(int width, int height, URL imageURL) {
        //Red and Blue are inverted for some reason. So we swap them back. Idk why man...
        return invertRB(getBufferedImage(width,height,(g) -> {
            try {
                //This isn't reading transparency for some reason. Use blackToTransparent to apply it
                BufferedImage read = ImageIO.read(imageURL);
                g.scale((double) width / read.getWidth(), (double) height / read.getHeight());
                g.drawImage(read, 0, 0, null);
                g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            } catch (IOException ignored) {
            }
        }));
    }

    public static ByteBuffer getByteBuffer(BufferedImage image) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] imageData = ((DataBufferInt) dataBuffer).getData();
        ByteBuffer buffer = BufferUtils.createByteBuffer(imageData.length * 8);
        for (int pixel : imageData) buffer.putInt(pixel);
        buffer.flip();
        return buffer;
    }

    private static BufferedImage invertRB(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));
                Color c2 = new Color(c.getBlue(),c.getGreen(),c.getRed(),c.getAlpha());
                image.setRGB(x,y,c2.hashCode());
            }
        }
        return image;
    }

    public static BufferedImage blackToTransparent(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));
                if (c.equals(Color.BLACK)) {
                    Color c2 = new Color(0,0,0,0);
                    image.setRGB(x,y,c2.hashCode());
                }
            }
        }
        return image;
    }

    @FunctionalInterface
    public interface DrawGraphics {
        void run(Graphics2D graphics);
    }
}
