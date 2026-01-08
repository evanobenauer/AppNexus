package com.ejo.ui.element.shape;

import com.ejo.ui.element.Element;
import com.ejo.ui.scene.Scene;
import com.ejo.util.action.OnChange;
import com.ejo.util.math.Vector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class ConvexPolygon extends Element {

    private final OnChange<Vector[]> onChange;
    protected Vector[] vertices;

    private boolean outlined;
    private float outlineWidth;
    private Color color;

    public ConvexPolygon(Scene scene, Vector pos, Color color, boolean outlined, float outlineWidth, Vector... vertices) {
        super(scene, pos);
        this.outlined = outlined;
        this.outlineWidth = outlineWidth;
        this.color = color;

        this.vertices = vertices;
        this.onChange = new OnChange<>();
    }

    public ConvexPolygon(Scene scene, Vector pos, Color color, Vector... vertices) {
        this(scene,pos,color, false,1,vertices);
    }


    @Override
    public void draw(Vector mousePos) {
        onChange.run(vertices, this::updateVertices);
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
        GL11.glLineWidth(outlineWidth);
        GL11.glBegin(outlined ? GL11.GL_LINE_LOOP : GL11.GL_POLYGON);
        for (Vector vert : vertices) GL11.glVertex2f((float) getPos().getX() + (float) vert.getX(), (float) getPos().getY() + (float) vert.getY());
        GL11.glEnd();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    //TODO: UpdateVertices is a little costly in child classes. Maybe only call when needed??
    protected void updateVertices() {}

    @Override
    //This algorithm (Separating Axis Thm). This only works if the polygon is convex
    // is kinda slow. So override it with polygon branches If possible. Use this only if unavailable
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        if (mousePos.getSubtracted(getCenter()).getMagnitude() > getMaximumVectorDistance())
            return false;

        ArrayList<Vector> axisList = new ArrayList<>();

        //Get Axes
        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            Vector vertex2 = vertices[i + 1 >= vertices.length ? 0 : i + 1];
            Vector sideVector = vertex2.getSubtracted(vertex);
            Vector perpendicular = sideVector.getCross(new Vector(0, 0, 1));
            Vector axis = perpendicular.getUnitVector();
            /*if (!axisList.contains(axis))*/ axisList.add(axis);
        }

        //Check Axes for Separation
        for (Vector axis : axisList) {
            double polygon1Max = 0;
            double polygon1Min = 0;

            double mouseComponent = axis.getDot(mousePos);

            for (int i = 0; i < vertices.length; i++) {
                Vector vertex = vertices[i].getAdded(getPos());
                double axisComponent = axis.getDot(vertex);
                if (i == 0) {
                    polygon1Min = axisComponent;
                    polygon1Max = axisComponent;
                }
                if (axisComponent > polygon1Max) polygon1Max = axisComponent;
                if (axisComponent < polygon1Min) polygon1Min = axisComponent;
            }

            if (mouseComponent > polygon1Min && mouseComponent < polygon1Max) {
                continue;
            } else {
                return false;
            }

        }
        return true;
    }


    //This method retrieves the farthest vertex from the center in order to get a circular radius around
    // the polygon to avoid having to do IAT every single time for multiple objects if it just won't need
    private double getMaximumVectorDistance() {
        double maxDistance = 0;
        for (Vector vertex : vertices) {
            double dist = vertex.getAdded(getPos()).getSubtracted(getCenter()).getMagnitude();
            if (dist > maxDistance) maxDistance = dist;
        }
        return maxDistance;
    }

    public Vector getCenter() {
        int iMinX = 0;
        int iMaxX = 0;

        int iMinY = 0;
        int iMaxY = 0;

        //Get minimum and maximum vertex indices
        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            if (vertex.getX() > vertices[iMaxX].getX()) iMaxX = i;
            if (vertex.getX() < vertices[iMinX].getX()) iMinX = i;
            if (vertex.getY() > vertices[iMaxY].getY()) iMaxY = i;
            if (vertex.getY() < vertices[iMinY].getY()) iMinY = i;
        }

        //Create a vector that centers around 0,0 with an offset towards the center of the shape
        Vector avgMinMax = new Vector(vertices[iMaxX].getX() + vertices[iMinX].getX(),vertices[iMaxY].getY() + vertices[iMinY].getY()).getMultiplied(.5);

        //Add the offset center to the original position
        return avgMinMax.getAdded(getPos());
    }

    // =================================================

    // GETTERS/SETTERS

    // =================================================

    public void setCenter(Vector pos) {
        Vector offset = getPos().getSubtracted(getCenter());
        setPos(offset.getAdded(pos));
    }

    public void setOutlined(boolean outlined) {
        this.outlined = outlined;
    }

    public void setOutlineWidth(float outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public boolean isOutlined() {
        return outlined;
    }

    public float getOutlineWidth() {
        return outlineWidth;
    }

    public Color getColor() {
        return color;
    }

    public Vector[] getVertices() {
        return vertices;
    }

    @Override
    public ConvexPolygon clone() {
        return new ConvexPolygon(getScene(),getPos(),color,outlined,outlineWidth,vertices);
    }
}
