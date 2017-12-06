package org.usfirst.frc.team2974.robot.util;

import org.usfirst.frc.team2974.robot.RobotConfiguration;

public abstract class MotionProvider {

    private final int numberOfPoints;
    private double vCruise;
    private double aMax; // max acceleration to vCruise, then goes to 0
    private double length;

    /**
     * Constructs MotionProvider.
     *
     * @param vCruise cruise velocity
     * @param aMax max acceleration/deceleration
     */
    public MotionProvider(double vCruise, double aMax) {
        this(vCruise, aMax, RobotConfiguration.N_POINTS);
    }

    /**
     * Constructs MotionProvider.
     *
     * @param vCruise cruise velocity
     * @param aMax max acceleration/deceleration
     * @param numberOfPoints number of points to separate the curve into
     */
    public MotionProvider(double vCruise, double aMax, int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
        if (vCruise == 0) {
            throw new IllegalArgumentException("vCruise cannot be 0");
        }
        this.setvCruise(vCruise);

        if (aMax == 0) {
            throw new IllegalArgumentException("aMax cannot be 0");
        }
        this.setaMax(aMax);
    }


    /**
     * FIXME?? find why angle is scaled by PI instead of TAU
     *
     * @param angle angle to put in bounds
     * @return angle in bounds [-PI, PI]
     */
    public static double boundAngle(final double angle) {
        if (angle > Math.PI) {
            return angle - Math.PI;
        }
        if (angle < -Math.PI) {
            return angle + Math.PI;
        }
        return angle;
    }

    /**
     * @param s how far we are into the motion
     * @return the next pose it needs to be
     */
    public abstract Pose evaluatePose(double s);

    /**
     * @return whether or not it's made to turn or go forward
     */
    public abstract MotionProvider.LimitMode getLimitMode();

    /**
     * @return length of the path
     */
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @return initial theta in radians
     */
    public abstract double getInitialTheta();

    /**
     * @return final theta in radians
     */
    public abstract double getFinalTheta();

    /**
     * @return position the robot should end at
     */
    public final Pose getFinalPose() {
        return this.evaluatePose(1.0);
    }

    /**
     * @return position the robot starts at
     */
    public final Pose getInitialPose() {
        return this.evaluatePose(0);
    }

    @Override
    public String toString() {
        return String.format(
            "MotionProvider{vCruise=%f, aMax=%f}"
            , getvCruise()
            , getaMax()
        );
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public double getvCruise() {
        return vCruise;
    }

    public void setvCruise(double vCruise) {
        this.vCruise = vCruise;
    }

    public double getaMax() {
        return aMax;
    }

    public void setaMax(double aMax) {
        this.aMax = aMax;
    }

    public enum LimitMode {
        LimitLinearAcceleration, LimitRotationalAcceleration
    }

}
