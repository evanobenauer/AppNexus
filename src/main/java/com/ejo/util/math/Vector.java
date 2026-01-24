package com.ejo.util.math;

public class Vector {

    private static final Vector NULL = new Vector(0, 0, 0);
    private static final Vector I = new Vector(1, 0, 0);
    private static final Vector J = new Vector(0, 1, 0);
    private static final Vector K = new Vector(0, 0, 1);

    public static Vector NULL() {
        return NULL.clone();
    }

    public static Vector I() {
        return I.clone();
    }
    public static Vector J() {
        return J.clone();
    }
    public static Vector K() {
        return K.clone();
    }


    protected double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this(x, y, 0);
    }


    /**
     * Cylindrical Coordinates
     * 3D
     */
    public Vector(double radius, Angle theta, double z) {
        this.x = radius * Math.cos(theta.getRadians());
        this.y = radius * Math.sin(theta.getRadians());
        this.z = z;
    }

    /**
     * Polar Coordinates
     * 2D
     */
    public Vector(double radius, Angle angle) {
        this(radius, angle, 0);
    }


    /**
     * Spherical Coordinates
     * 3D
     */
    public Vector(double rho, Angle theta, Angle phi) {
        this.x = rho * theta.getCos() * phi.getSin();
        this.y = rho * theta.getSin() * phi.getSin();
        this.z = rho * phi.getCos();
    }


    public Vector x(double x) {
        return new Vector(x,y,z);
    }

    public Vector y(double y) {
        return new Vector(x,y,z);
    }

    public Vector z(double z) {
        return new Vector(x,y,z);
    }


    public Vector getAdded(Vector vec) {
        return new Vector(x + vec.getX(), y + vec.getY(), z + vec.getZ());
    }

    public Vector getAdded(double x, double y) {
        return getAdded(x, y, 0);
    }

    public Vector getAdded(double x, double y, double z) {
        return getAdded(new Vector(x, y, z));
    }


    public Vector getSubtracted(Vector vec) {
        return new Vector(x - vec.getX(), y - vec.getY(), z - vec.getZ());
    }

    public Vector getSubtracted(double x, double y) {
        return getSubtracted(x, y, 0);
    }

    public Vector getSubtracted(double x, double y, double z) {
        return getSubtracted(new Vector(x, y, z));
    }


    public Vector getScaled(Vector vec) {
        return new Vector(x * vec.getX(), y * vec.getY(), z * vec.getZ());
    }

    public Vector getScaled(double x, double y) {
        return new Vector(getX() * x, getY() * y, getZ() * 1);
    }

    public Vector getScaled(double x, double y, double z) {
        return new Vector(getX() * x, getY() * y, getZ() * z);
    }


    public Vector getMultiplied(double multiplier) {
        return new Vector(x * multiplier, y * multiplier, z * multiplier);
    }

    public Vector getCross(Vector vec) {
        return new Vector(
                y * vec.getZ() - z * vec.getY(),
                -x * vec.getZ() + z * vec.getX(),
                x * vec.getY() - y * vec.getX());
    }

    public double getDot(Vector vec) {
        return x * vec.getX() + y * vec.getY() + z * vec.getZ();
    }

    public Vector getProjection(Vector onto) {
        return onto.getMultiplied(getDot(onto) / Math.pow(onto.getMagnitude(),2));
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector getUnitVector() {
        if (getMagnitude() == 0) return Vector.NULL();
        return getMultiplied(1 / getMagnitude());
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getXf() {
        return (float) x;
    }

    public float getYf() {
        return (float) y;
    }

    public float getZf() {
        return (float) z;
    }

    public int getXi() {
        return (int) x;
    }

    public int getYi() {
        return (int) y;
    }

    public int getZi() {
        return (int) z;
    }

    public double getRadius() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    /**
     * Theta returns an angle that is simplified to be between 0 and 2PI
     * @return
     */
    public Angle getTheta() {
        return new Angle(Math.atan2(getY(), getX())).simplify();
    }

    /**
     * Rho is the 3D radius using spherical coordinates
     * @return
     */
    public double getRho() {
        return getMagnitude();
    }

    public Angle getPhi() {
        return new Angle(Math.acos(getZ() / getMagnitude()));
    }

    // ------------------------------ MODIFIABLE METHODS BELOW ------------------------------
    // Try and avoid using these unless you clone a vector as to not destroy static vectors

    public Vector setX(double x) {
        this.x = x;
        return this;
    }

    public Vector setY(double y) {
        this.y = y;
        return this;
    }

    public Vector setZ(double z) {
        this.z = z;
        return this;
    }

    public Vector add(Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public Vector subtract(Vector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    public Vector scale(Vector vec) {
        this.x *= vec.getX();
        this.y *= vec.getY();
        this.z *= vec.getZ();
        return this;
    }

    public Vector multiply(double multiplier) {
        this.x *= multiplier;
        this.y *= multiplier;
        this.z *= multiplier;
        return this;
    }

    public Vector cross(Vector vec) {
        this.x = getY() * vec.getZ() - getZ() * vec.getY();
        this.y = -getX() * vec.getZ() + getZ() * vec.getX();
        this.z = getX() * vec.getY() - getY() * vec.getX();
        return this;
    }

    public Vector normalize() {
        return multiply(1/getMagnitude());
    }

    @Override
    public Vector clone() {
        return new Vector(x,y,z);
    }

    @Override
    public String toString() {
        return "<" + getXf() + "|" + getYf() + "|" + getZf() + ">";
    }

    public String toString2D() {
        return "<" + getXf() + "|" + getYf() + ">";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector newVec)) return false;
        try {
            return x == newVec.getX() && y == newVec.getY() && z == newVec.getZ();
        } catch (Exception e) {
            return false;
        }
    }
}