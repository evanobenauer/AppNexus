package com.ejo.ui.element.shape;

import com.ejo.ui.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.ArrayList;

//Yes, I understand a circle is NOT a polygon. But for all purposes, it is a polygon according to this program.
// It is just made up of many vertices so it is a "Circle"...
// I have this not extending RegularPolygon for a reason... I don't remember why... But there is a reason
public class Circle extends ConvexPolygon {

    private static final Angle FULL = new Angle(Math.TAU);

    private double radius;
    private Quality quality;

    private Angle range;

    public Circle(Scene scene, Vector pos, double radius, Color color, boolean outlined, float outlineWidth, Angle range, Quality quality) {
        super(scene, pos, color, outlined, outlineWidth);
        this.radius = radius;
        this.range = range;
        this.quality = quality;
        updateVertices();
    }

    public Circle(Scene scene, Vector pos, double radius, Color color, Angle range, Quality quality) {
        this(scene,pos,radius,color,false,1,range, quality);
    }

    public Circle(Scene scene, Vector pos, double radius, Color color, Quality quality) {
        this(scene,pos,radius,color,FULL, quality);
    }

    @Override
    protected void updateVertices() {
        double radianIncrement = range.getRadians() / quality.getVertices();
        ArrayList<Vector> vertices = new ArrayList<>();
        if (!range.equals(FULL)) vertices.add(Vector.NULL()); //Add center vertex if not full to make partial circle
        for (int i = 0; i < quality.getVertices(); i++) {
            Vector vert = new Vector(Math.cos(radianIncrement*i),Math.sin(radianIncrement*i)).getMultiplied(getRadius());
            vertices.add(vert);
        }
        this.vertices = vertices.toArray(new Vector[0]);
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        Vector relativeMousePos = mousePos.getSubtracted(getCenter());
        if (radius < relativeMousePos.getMagnitude()) {
            return false;
        }
        if (range.equals(FULL)) {
            return true;
        }
        Angle theta = relativeMousePos.getTheta().getSimplified();
        return theta.getDegrees() <= range.getDegrees();
    }


    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setRange(Angle range) {
        this.range = range;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    @Override
    public Vector getCenter() {
        return getPos();
    }

    public double getRadius() {
        return radius;
    }

    public Angle getRange() {
        return range;
    }

    public Quality getQuality() {
        return quality;
    }

    @Override
    public ConvexPolygon clone() {
        return new Circle(getScene(),getPos(),getRadius(),getColor(),isOutlined(),getOutlineWidth(),range,quality);
    }

    public enum Quality {
        POOR(10),
        LOW(16),
        MEDIUM(22),
        HIGH(28),
        ULTRA(34);

        private final int vertices;
        Quality(int vertices) {
            this.vertices = vertices;
        }
        public int getVertices() {
            return vertices;
        }
    }
}
