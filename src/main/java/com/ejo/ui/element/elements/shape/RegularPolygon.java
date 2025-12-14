package com.ejo.ui.element.elements.shape;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.ArrayList;

public class RegularPolygon extends Polygon {

    protected static final Angle FULL = new Angle(Math.TAU,true);

    private double radius;
    private int vertexCount;
    private Angle rotation;

    private final Angle range;

    public RegularPolygon(Scene scene, Vector pos, Color color, boolean outlined, float outlineWidth, double radius, int vertexCount, Angle rotation) {
        super(scene, pos, color, outlined,outlineWidth);
        this.radius = radius;
        this.vertexCount = vertexCount;
        this.rotation = rotation;
        this.range = FULL;
        updateVertices();
    }

    public RegularPolygon(Scene scene, Vector pos, Color color, double radius, int vertexCount, Angle rotation) {
        this(scene,pos,color,false,1,radius,vertexCount,rotation);
    }

    public RegularPolygon(Scene scene, Vector pos, Color color, double radius, int vertexCount) {
        this(scene,pos,color,radius,vertexCount,Angle.NULL());
    }

    @Override
    protected void updateVertices() {
        double rot = (getRotation() != null ? getRotation().getRadians() : 0);
        double radianIncrement = range.getRadians() / getVertexCount();
        ArrayList<Vector> vertices = new ArrayList<>();
        if (!range.equals(FULL)) vertices.add(Vector.NULL()); //Add center vertex if not full to make partial circle
        for (int i = 0; i < getVertexCount(); i++) {
            Vector vert = new Vector(Math.cos(radianIncrement * i + rot), Math.sin(radianIncrement * i + rot)).getMultiplied(getRadius());
            vertices.add(vert);
        }
        this.vertices = vertices.toArray(new Vector[0]);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public void setRotation(Angle rotation) {
        this.rotation = rotation;
    }


    @Override
    public Vector getCenter() {
        return getPos();
    }

    public double getRadius() {
        return radius;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Angle getRotation() {
        return rotation;
    }
}