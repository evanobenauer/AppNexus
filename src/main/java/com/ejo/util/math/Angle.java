package com.ejo.util.math;

import static java.lang.Math.PI;
import static java.lang.Math.TAU;

public class Angle {

    /**
     * Angle (in radians)
     */
    protected double value;

    public Angle(double value, boolean isDegrees) {
        if (isDegrees) this.value = value * TAU / 360;
        else this.value = value;
    }

    public Angle(double value) {
        this(value, false);
    }


    public Angle getAdded(double value, boolean isDegrees) {
        return new Angle(value + (isDegrees ? getDegrees() : this.value), isDegrees);
    }

    public Angle getAdded(Angle angle) {
        return getAdded(angle.value,false);
    }


    public Angle getSubtracted(double value, boolean isDegrees) {
        return new Angle(value - (isDegrees ? getDegrees() : this.value), isDegrees);
    }

    public Angle getSubtracted(Angle angle) {
        return getSubtracted(angle.value,false);
    }

    /**
     * Simplifies the angle to be between 0 and 2PI
     * @return
     */
    public Angle getSimplified() {
        double rad = value;
        while (rad > TAU) rad -= TAU;
        while (rad < 0) rad += TAU;
        return new Angle(rad);
    }

    public Vector getUnitVector(boolean shouldRound) {
        double x = shouldRound ? MathUtil.roundDouble(Math.cos(getRadians()), 6) : Math.cos(getRadians());
        double y = shouldRound ? MathUtil.roundDouble(Math.sin(getRadians()), 6) : Math.sin(getRadians());
        return new Vector(x, y);
    }

    public Vector getUnitVector() {
        return getUnitVector(false);
    }


    public double getSin() {
        return Math.sin(getRadians());
    }

    public double getCos() {
        return Math.cos(getRadians());
    }


    public double getRadians() {
        return value;
    }

    public double getDegrees() {
        return getRadians() * 360 / TAU;
    }

    // ------------------------------ MODIFIABLE METHODS BELOW ------------------------------

    public Angle add(Angle angle) {
        this.value += angle.getRadians();
        return this;
    }

    public Angle subtract(Angle angle) {
        this.value -= angle.getRadians();
        return this;
    }

    /**
     * Simplifies the angle to be between 0 and 2PI
     * @return
     */
    public Angle simplify() {
        while (this.value > TAU) this.value -= TAU;
        while (this.value < 0) this.value += TAU;
        return this;
    }

    @Override
    protected Object clone() {
        return new Angle(value);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "[Angle: " + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Angle ang)) return false;
        return ang.getRadians() == value;
    }

}
