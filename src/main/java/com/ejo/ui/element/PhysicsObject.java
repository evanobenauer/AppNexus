package com.ejo.ui.element;

import com.ejo.ui.element.base.Tickable;
import com.ejo.ui.element.shape.ConvexPolygon;
import com.ejo.ui.Scene;
import com.ejo.util.math.Vector;

public class PhysicsObject extends DrawableElement implements Tickable {

    private final DrawableElement element;

    private double mass;
    private double charge;
    private double rotationalInertia;

    private Vector velocity;
    private Vector acceleration;
    private Vector netForce;

    //If you decide to transform this into 3D, you need to make sure each of these becomes a 2D vector
    private double theta;
    private double omega;
    private double alpha;
    private double netTorque;

    private double deltaT;

    public PhysicsObject(Scene scene, Vector pos, DrawableElement element) {
        super(scene, pos);

        this.element = element;

        this.mass = 1;
        this.charge = 0;
        this.rotationalInertia = 1;

        this.velocity = Vector.NULL();
        this.acceleration = Vector.NULL();
        this.netForce = Vector.NULL();
        this.deltaT = .1f;

        this.element.setPos(pos); //Just in case it was not set
        updateElement();
    }

    @Override
    public boolean getMouseHoveredCalculation(Vector mousePos) {
        return element.getMouseHoveredCalculation(mousePos);
    }

    @Override
    public void draw(Vector mousePos) {
        updateElement(); //The polygon is purely for visuals so it's best to update it in the draw method
        element.draw(mousePos);
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
        velocity = velocity.getAdded(acceleration.getMultiplied(deltaT));
        setPos(getPos().getAdded(velocity.getMultiplied(deltaT)));
    }

    private void updateAccelerationFromForce() {
        this.acceleration = netForce.getMultiplied(1/mass);
    }

    private void updateRotationalKinematics() {
        omega += alpha * deltaT;
        theta += omega * deltaT;
    }

    private void updateAlphaFromTorque() {
        this.alpha = netTorque / getRotationalInertia();
    }

    private void updateElement() {
        if (element instanceof ConvexPolygon p) p.setCenter(getPos());
        else element.setPos(getPos());
    }

    public void addForce(Vector force) {
        this.netForce.add(force);
    }

    public void addTorque(double torque) {
        this.netTorque += torque;
    }

    // =================================================

    // SETTERS
    // Some setters will return the PhysicsObject so they are able to be used as builders as they are optional

    // =================================================

    @Override
    public PhysicsObject setPos(Vector pos) {
        super.setPos(pos);
        if (element instanceof ConvexPolygon p) p.setCenter(getPos());
        else element.setPos(getPos());
        return this;
    }

    public PhysicsObject setMass(double mass) {
        this.mass = mass;
        return this;
    }

    public PhysicsObject setCharge(double charge) {
        this.charge = charge;
        return this;
    }

    public PhysicsObject setRotationalInertia(double rotationalInertia) {
        this.rotationalInertia = rotationalInertia;
        return this;
    }

    public PhysicsObject setVelocity(Vector velocity) {
        this.velocity = velocity;
        return this;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setOmega(double omega) {
        this.omega = omega;
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

    public double getRotationalInertia() {
        return rotationalInertia;
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


    public double getTheta() {
        return theta;
    }

    public double getOmega() {
        return omega;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getNetTorque() {
        return netTorque;
    }


    public double getDeltaT() {
        return deltaT;
    }


    public DrawableElement getElement() {
        return element;
    }


    @Override
    public PhysicsObject clone() {
        return new PhysicsObject(getScene(), getPos(), element.clone());
    }
}
