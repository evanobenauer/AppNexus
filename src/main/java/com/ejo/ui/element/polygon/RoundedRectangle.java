package com.ejo.ui.element.polygon;

import com.ejo.ui.Scene;
import com.ejo.util.math.Angle;
import com.ejo.util.math.Vector;

import java.awt.*;
import java.util.ArrayList;

public class RoundedRectangle extends Rectangle {

    private int cornerRadius;
    private int originalCornerRadius;

    private static final int DEFAULT_CORNER_RADIUS = 30;

    public RoundedRectangle(Scene scene, Vector pos, Vector size, Color color, int cornerRadius, boolean outlined, float outlineWidth) {
        super(scene, pos, size, color, outlined, outlineWidth);
        this.cornerRadius = cornerRadius;
        this.originalCornerRadius = cornerRadius;
    }

    public RoundedRectangle(Scene scene, Vector pos, Vector size, Color color, int cornerRadius) {
        this(scene, pos, size, color,cornerRadius,false,1);
    }

    public RoundedRectangle(Scene scene, Vector pos, Vector size, Color color) {
        this(scene, pos, size, color, DEFAULT_CORNER_RADIUS);
    }

    @Override
    protected void updateVertices() {
        updateCornerRadius();

        //Mark the 4 quarter circle centers
        Vector[] corners = {
                new Vector(cornerRadius, getSize().getY() - cornerRadius), //Bottom Left
                new Vector(getSize().getX() - cornerRadius,  getSize().getY() - cornerRadius), //Bottom Right
                new Vector(getSize().getX() - cornerRadius, cornerRadius), //Upper Right
                new Vector(cornerRadius, cornerRadius) //Upper Left
        };

        double vertexCount = 5;
        Angle range = new Angle(Math.TAU/4);

        ArrayList<Vector> vertices = new ArrayList<>();

        //For each corner, draw a quarter circle
        for (int c = 0; c < 4; c++) {
            Vector center = corners[c];

            //Draw Quarter Circle
            double radianIncrement = range.getRadians() / vertexCount;
            double rotation = new Angle(Math.PI).getAdded(range.getMultiplied(c)).getRadians();
            for (int i = 0; i <= vertexCount; i++) {
                Vector vertex = new Vector(Math.cos(radianIncrement*i + rotation),-Math.sin(radianIncrement*i + rotation)).getMultiplied(cornerRadius).getAdded(center);
                vertices.add(vertex);
            }
        }

        this.vertices = vertices.toArray(new Vector[0]);
    }

    //This is terribly written. TODO: Make this less disgusting to look at
    private void updateCornerRadius() {
        boolean xSmall = originalCornerRadius * 2 > getSize().getXi();
        boolean ySmall = originalCornerRadius * 2 > getSize().getYi();

        if (xSmall && !ySmall) {
            cornerRadius = getSize().getXi() / 2;
        } else if (!xSmall && ySmall) {
            cornerRadius = getSize().getYi() / 2;
        } else if (xSmall && ySmall) {
            if (getSize().getXi() > getSize().getYi()) {
                cornerRadius = getSize().getYi() / 2;
            } else {
                cornerRadius = getSize().getXi() / 2;
            }
        } else {
            cornerRadius = originalCornerRadius;
        }
    }

    public void setCornerRadius(int cornerRadius) {
        this.originalCornerRadius = cornerRadius;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    @Override
    public RoundedRectangle clone() {
        return new RoundedRectangle(getScene(),getPos(),getSize(),getColor(),cornerRadius,isOutlined(),getOutlineWidth());
    }
}
