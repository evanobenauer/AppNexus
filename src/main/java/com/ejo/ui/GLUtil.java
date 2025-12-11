package com.ejo.ui;

import com.ejo.util.math.Vector;
import com.ejo.util.misc.ImageUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class GLUtil {

    public static void translate(Vector vec) {
        GL11.glTranslated(vec.getX(),vec.getY(),vec.getZ());
    }

    public static void scale(Vector vec) {
        GL11.glScaled(vec.getX(),vec.getY(),vec.getZ());
    }

    public static void scale(double val) {
        GL11.glScaled(val,val,val);
    }

    public static void scaleAboutPointStart(Vector point, Vector scale) {
        GLUtil.translate(new Vector(point.getX(),point.getY()));
        GLUtil.scale(new Vector(scale.getX(),scale.getY(),1));
        GLUtil.translate(new Vector(point.getX(),point.getY()).getMultiplied(-1));
    }

    public static void scaleAboutPointEnd(Vector point, Vector scale) {
        GLUtil.translate(new Vector(point.getX(),point.getY()));
        GLUtil.scale(new Vector(1/scale.getX(),1/scale.getY(),1));
        GLUtil.translate(point.getSubtracted(0,0,point.getZ()).getMultiplied(-1));
    }

    public static void textureScale(Vector vec) {
        GL11.glPixelZoom((float) vec.getX(), (float)-vec.getY());
    }

    public static void textureScale(double val) {
        GL11.glPixelZoom((float) val, (float)-val);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red/255f,green/255f,blue/255f,alpha/255f);
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


    //TODO: Place these into a DrawUtil file when you eventually create it
    public static void drawTexture(ByteBuffer imageBuffer, Vector pos, Vector size) {
        GL11.glRasterPos2f((float)pos.getX(), (float)pos.getY());
        GL11.glDrawPixels(size.getXi(),size.getYi(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
    }

    public static void drawTexture(BufferedImage image, Vector pos) {
        GL11.glRasterPos2f((float)pos.getX(), (float)pos.getY());
        GL11.glDrawPixels(image.getWidth(),image.getHeight(),GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, ImageUtil.getByteBuffer(image));
    }

}
