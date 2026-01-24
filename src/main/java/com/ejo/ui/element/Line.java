package com.ejo.ui.element;

import com.ejo.ui.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Line extends DrawableElement {

    private final Vector[] vertices;

    private Type type;
    private double width;

    private Color color;

    private boolean faded;

    public Line(Scene scene, double width, Type type, Color color, Vector... vertices) {
        super(scene, vertices[0]);

        this.vertices = vertices;

        this.width = width;
        this.type = type;
        this.color = color;

        this.faded = false;
    }

    public Line(Scene scene, Vector pos, Angle angle, double length, double width, Type type, Color color) {
        this(scene,width,type,color,pos,pos.getAdded(angle.getUnitVector().getMultiplied(length)));
    }

    public Line(Scene scene, Vector pos1, Vector pos2, double width,Type type,Color color ) {
        this(scene,width,type,color,pos1,pos2);
    }

    @Override
    public void draw(Vector mousePos) {
        GL11.glLineWidth((float)getWidth());

        switch(getType()) {
            case PLAIN -> {
                GL11.glDisable(GL11.GL_LINE_STIPPLE);
                GL11.glEnable(GL11.GL_LINE_STRIP);
            }
            case DOTTED -> {
                GL11.glEnable(GL11.GL_LINE_STIPPLE);
                GL11.glLineStipple(1,(short)0xAAAA);
            }
            case DASHED -> {
                GL11.glEnable(GL11.GL_LINE_STIPPLE);
                GL11.glLineStipple(5,(short)0xBBBB);
            }
        }

        GL11.glBegin(GL11.GL_LINE_STRIP);
        float alpha = color.getAlpha();
        for (Vector vertex : vertices) {
            GL11.glColor4f(color.getRed() / 255f,color.getGreen() / 255f,color.getBlue() / 255f,alpha / 255f);
            GL11.glVertex2d(vertex.getX(), vertex.getY());
            if (faded) alpha -= (float) color.getAlpha() / vertices.length;
        }
        GL11.glEnd();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }


    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        //A line is a 1D object... It cannot physically by hovered
        return false;
    }


    @Override
    public Line setPos(Vector pos) {
        Vector basePos = vertices[0];
        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            vertices[i] = vertex.getAdded(pos.getSubtracted(basePos));
        }
        return (Line) super.setPos(pos);
    }

    public Line setCenter(Vector pos) {
        Vector oldCenter = getCenter();
        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            vertices[i] = vertex.getAdded(pos.getSubtracted(oldCenter));
        }
        return (Line) super.setPos(vertices[0]);
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFaded(boolean faded) {
        this.faded = faded;
    }

    public Vector getCenter() {
        int iMinX = 0;
        int iMaxX = 0;

        int iMinY = 0;
        int iMaxY = 0;

        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            if (vertex.getX() > vertices[iMaxX].getX()) iMaxX = i;
            if (vertex.getX() < vertices[iMinX].getX()) iMinX = i;
            if (vertex.getY() > vertices[iMaxY].getY()) iMaxY = i;
            if (vertex.getY() < vertices[iMinY].getY()) iMinY = i;
        }

        return new Vector(vertices[iMaxX].getX() + vertices[iMinX].getX(),vertices[iMaxY].getY() + vertices[iMinY].getY()).getMultiplied(.5);
    }

    public double getLength() {
        double length = 0;
        for (int i = 0; i < vertices.length; i++) {
            boolean isThereNextVertex = i == vertices.length - 1;
            Vector vertex = vertices[i];
            Vector nextVertex = (isThereNextVertex) ? vertices[i + 1] : null;
            if (isThereNextVertex) length += nextVertex.getSubtracted(vertex).getMagnitude();
        }

        return length;
    }

    public double getWidth() {
        return width;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public Vector[] getVertices() {
        return vertices;
    }


    public enum Type {
        PLAIN,
        DOTTED,
        DASHED
    }
}
