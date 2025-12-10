package com.ejo.ui.element.builder;

import com.ejo.util.math.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public class TextureUtil {

    public static void drawTexture(ByteBuffer imageBuffer, Vector pos, Vector size) {
        GL11.glRasterPos2f((float)pos.getX(), (float)pos.getY());
        GL11.glDrawPixels(size.getXi(),size.getYi(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
    }

    public static void drawTexture(BufferedImage image, Vector pos) {
        GL11.glRasterPos2f((float)pos.getX(), (float)pos.getY());
        GL11.glDrawPixels(image.getWidth(),image.getHeight(),GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,getByteBuffer(image));
    }

    public static void applyTextureColorTint(Color color) {
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, color.getRed() / 255f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, color.getGreen() / 255f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, color.getBlue() / 255f);
        GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, color.getAlpha() / 255f);
    }

    public static void resetTextureColorTint() {
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1f);
    }


    //Note: Generating a buffered image like this is very intensive if done loop after loop.
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

    @FunctionalInterface
    public interface DrawGraphics {
        void run(Graphics2D graphics);
    }
}
