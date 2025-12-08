package com.ejo.ui.element.elements.shape;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.ArrayList;

//Yes, I understand a circle is NOT a polygon. But for all purposes, it is a polygon according to this program.
// It is just made up of many vertices so it is a "Circle"...
public class Circle extends Polygon {

    private static final Angle FULL = new Angle(360,true);

    private double radius;
    private Angle range;
    private Type type;

    public Circle(Scene scene, Vector pos, Color color, boolean outlined, float outlineWidth, double radius, Angle range, Type type) {
        super(scene, pos, color, outlined, outlineWidth);
        this.radius = radius;
        this.range = range;
        this.type = type;
        updateVertices();
    }

    public Circle(Scene scene, Vector pos, Color color, double radius, Angle range, Type type) {
        this(scene,pos,color,false,1,radius,range,type);
    }

    public Circle(Scene scene, Vector pos, Color color, double radius, Type type) {
        this(scene,pos,color,radius,FULL,type);
    }

    @Override
    public void draw(Vector mousePos) {
        updateVertices();
        super.draw(mousePos);
    }

    private void updateVertices() {
        double radianIncrement = range.getRadians() / type.getVertices();
        ArrayList<Vector> vertices = new ArrayList<>();
        if (!range.equals(FULL)) vertices.add(Vector.NULL()); //Add center vertex if not full to make partial circle
        for (int i = 0; i < type.getVertices(); i++) {
            Vector vert = new Vector(Math.cos(radianIncrement*i),Math.sin(radianIncrement*i)).getMultiplied(getRadius());
            vertices.add(vert);
        }
        this.vertices = vertices.toArray(new Vector[0]);
    }

    @Override
    public void updateMouseHovered(Vector mousePos) {
        Vector relativeMousePos = mousePos.getSubtracted(getCenter());
        if (radius < relativeMousePos.getMagnitude()) {
            setHovered(false);
            return;
        }
        if (range.equals(FULL)) {
            setHovered(true);
            return;
        }
        Angle theta = relativeMousePos.getTheta().getSimplified();
        setHovered(theta.getDegrees() <= range.getDegrees());
    }


    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setRange(Angle range) {
        this.range = range;
    }

    public void setType(Type type) {
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public enum Type {
        POOR(10),
        LOW(16),
        MEDIUM(22),
        HIGH(28),
        ULTRA(34);

        private final int vertices;
        Type(int vertices) {
            this.vertices = vertices;
        }
        public int getVertices() {
            return vertices;
        }
    }
}
