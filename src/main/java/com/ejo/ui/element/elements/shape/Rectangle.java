package com.ejo.ui.element.elements.shape;

import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;

public class Rectangle extends Polygon {

    private Vector size;

    public Rectangle(Scene scene, Vector pos, Color color, Vector size, boolean outlined, float outlineWidth) {
        super(scene, pos, color, outlined,outlineWidth,new Vector(0,0),new Vector(0,size.getY()),size,new Vector(size.getX(),0));
        this.size = size;
    }

    public Rectangle(Scene scene, Vector pos, Color color, Vector size) {
        this(scene,pos,color, size,false,1);
    }

    @Override
    public void draw(Vector mousePos) {
        vertices[1] = new Vector(0,getSize().getY());
        vertices[2] = getSize();
        vertices[3] = new Vector(getSize().getX(),0);
        super.draw(mousePos);
    }

    @Override
    public void updateMouseHovered(Vector mousePos) {
        boolean mouseHoveredX = mousePos.getX() >= getPos().getX() && mousePos.getX() <= getPos().getX() + getSize().getX();
        boolean mouseHoveredY = mousePos.getY() >= getPos().getY() && mousePos.getY() <= getPos().getY() + getSize().getY();
        setHovered(mouseHoveredX && mouseHoveredY);
    }

    public void setSize(Vector size) {
        this.size = size;
    }


    public Vector getSize() {
        return size;
    }

}
