package com.ejo.ui.element.elements.shape;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;

public class Rectangle extends ConvexPolygon {

    private Vector size;

    public Rectangle(Scene scene, Vector pos, Vector size, Color color, boolean outlined, float outlineWidth) {
        super(scene, pos, color, outlined,outlineWidth,new Vector(0,0),new Vector(0,size.getY()),size,new Vector(size.getX(),0));
        this.size = size;
    }

    public Rectangle(Scene scene, Vector pos, Vector size, Color color) {
        this(scene,pos,size, color,false,1);
    }

    @Override
    protected void updateVertices() {
        vertices[1] = new Vector(0,getSize().getY());
        vertices[2] = getSize();
        vertices[3] = new Vector(getSize().getX(),0);
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return isInRectangleBoundingBox(getPos(),getSize(),mousePos);
    }

    public void setSize(Vector size) {
        this.size = size;
    }


    public Vector getSize() {
        return size;
    }

    public static boolean isInRectangleBoundingBox(Vector pos, Vector size, Vector location) {
        boolean insideX = location.getX() >= pos.getX() && location.getX() <= pos.getX() + size.getX();
        boolean insideY = location.getY() >= pos.getY() && location.getY() <= pos.getY() + size.getY();
        return insideX && insideY;
    }

}
