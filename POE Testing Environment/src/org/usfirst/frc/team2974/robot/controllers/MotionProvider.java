package org.usfirst.frc.team2974.robot.controllers;

public abstract class MotionProvider {

    public double vCruise;
    public double aMax; // max acceleration to vCruise, then goes to 0

    /**
     * Constructs MotionProvider.
     *
     * @param vCruise cruise velocity
     * @param aMax max acceleration/deceleration
     */
    public MotionProvider(double vCruise, double aMax) {
        if (vCruise == 0) {
            throw new IllegalArgumentException("vCruise cannot be 0");
        }
        this.vCruise = vCruise;

        if (aMax == 0) {
            throw new IllegalArgumentException("aMax cannot be 0");
        }
        this.aMax = aMax;
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
     * @return distance to next pose
     */
    public abstract double getLength();

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
        return "MotionProvider{" +
            "vCruise=" + vCruise +
            ", aMax=" + aMax +
            '}';
    }

    public enum LimitMode {
        LimitLinearAcceleration, LimitRotationalAcceleration
    }

}
