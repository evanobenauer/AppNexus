package com.ejo.ui.element.elements.simulation;

import com.ejo.ui.element.Element;
import com.ejo.ui.element.base.ITickable;
import com.ejo.ui.element.elements.shape.ConvexPolygon;
import com.ejo.ui.scene.Scene;
import com.ejo.util.math.Vector;

import java.awt.*;

public class PhysicsObject extends Element implements ITickable {

    private final ConvexPolygon polygon;

    private double mass;
    private double charge;

    private Vector velocity;
    private Vector acceleration;
    private Vector netForce;

    //If you decide to transform this into 3D, you need to make sure each of these becomes a 2D vector
    private double theta;
    private double omega;
    private double alpha;
    private double netTorque;

    private double deltaT;

    public PhysicsObject(Scene scene, Vector pos, ConvexPolygon polygon) {
        super(scene, pos);

        this.polygon = polygon;

        this.mass = 1;
        this.charge = 0;

        this.velocity = Vector.NULL();
        this.acceleration = Vector.NULL();
        this.netForce = Vector.NULL();
        this.deltaT = .1f;

        this.polygon.setPos(pos); //Just in case it was not set
        updatePolygon();
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return polygon.getMouseHoveredCalculation(mousePos);
    }

    @Override
    public void draw(Vector mousePos) {
        updatePolygon(); //The polygon is purely for visuals so it's best to update it in the draw method
        polygon.draw(mousePos);
    }

    @Override
    public void tick(Vector mousePos) {
        updateAccelerationFromForce();
        updateKinematics();
        this.netForce = Vector.NULL();

        updateAlphaFromTorque();
        updateRotationalKinematics();
        this.netTorque = 0;
    }

    private void updateKinematics() {
        velocity.add(acceleration.getMultiplied(deltaT));
        getPos().add(velocity.getMultiplied(deltaT));
    }

    private void updateAccelerationFromForce() {
        this.acceleration = netForce.getMultiplied(1/mass);
    }

    private void updateRotationalKinematics() {
        omega += alpha * deltaT;
        theta += omega * deltaT;
    }

    private void updateAlphaFromTorque() {
        this.alpha = netTorque / mass;
    }

    private void updatePolygon() {
        this.polygon.setCenter(getPos());
    }

    public void addForce(Vector force) {
        this.netForce.add(force);
    }

    // =================================================

    // SETTERS
    // Some setters will return the PhysicsObject so they are able to be used as builders as they are optional

    // =================================================

    @Override
    public void setPos(Vector pos) {
        super.setPos(pos);
        this.polygon.setCenter(pos);
    }

    public PhysicsObject setMass(double mass) {
        this.mass = mass;
        return this;
    }

    public PhysicsObject setCharge(double charge) {
        this.charge = charge;
        return this;
    }

    public PhysicsObject setVelocity(Vector velocity) {
        this.velocity = velocity;
        return this;
    }

    public PhysicsObject setDeltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }



    public double getMass() {
        return mass;
    }

    public double getCharge() {
        return charge;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public Vector getNetForce() {
        return netForce;
    }

    public double getDeltaT() {
        return deltaT;
    }


    public ConvexPolygon getPolygon() {
        return polygon;
    }

}
