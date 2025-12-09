package com.ejo.ui.element.builder;

import com.ejo.util.math.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;

public class TextureUtil {

    //Note: Generating a buffered image like this is very intensive if done loop after loop.
    // Caching the buffered image is much smarter for performance
    public static BufferedImage getBufferedImage(int width, int height, DrawGraphics drawCode) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        drawCode.run(graphics);
        return image;
    }

    public static BufferedImage getBufferedImage(int width, int height, File file) {
        //TODO: Add this
        return null;
    }

    public static ByteBuffer getByteBuffer(BufferedImage image) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        int[] imageData = ((DataBufferInt) dataBuffer).getData();
        ByteBuffer buffer = BufferUtils.createByteBuffer(imageData.length * 8);
        for (int pixel : imageData) buffer.putInt(pixel);
        buffer.flip();
        return buffer;
    }

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

    @FunctionalInterface
    public interface DrawGraphics {
        void run(Graphics2D graphics);
    }
}
