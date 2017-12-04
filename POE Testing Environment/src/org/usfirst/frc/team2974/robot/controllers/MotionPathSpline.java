package org.usfirst.frc.team2974.robot.controllers;


import java.util.Arrays;

public class MotionPathSpline extends MotionProvider {

    private final Point2D[] controlPoints = new Point2D[4];
    private final double length;
    private final double initialTheta; // start angle
    private final double finalTheta; // end angle
    private final boolean isForwards;

    /**
     * Constructs MotionPathSpline.
     *
     * @param initial initial pose
     * @param l0 offset length from initial pose, used to create control points.
     * i.e. creates how curvy you want the spline
     * @param final_ final pose
     * @param l1 offset length from final pose, used to create control points.
     * i.e. creates how curvy you want the spline
     * @param vCruise velocity to cruise at
     * @param aMax max acceleration/deceleration
     * @param isForwards is it forwards
     */
    public MotionPathSpline(Pose initial, double l0, Pose final_, double l1,
        double vCruise, double aMax, boolean isForwards) {
        super(vCruise, aMax);

        controlPoints[0] = initial.point;
        controlPoints[1] = initial.offsetPoint(isForwards ? l0 : -l0);
        controlPoints[2] = final_.offsetPoint(isForwards ? -l1 : l1);
        controlPoints[3] = final_.point;

        Point2D xPrevious = evaluate(B(0));
        double length = 0;
        for (int i = 1; i <= 100; i++) {
            final double s = i / 100.0;
            Point2D xNext = evaluate(B(s));
            length += xPrevious.distance(xNext);
            xPrevious = xNext;
        }

        this.length = length;
        initialTheta = initial.angle;
        finalTheta = final_.angle;
        this.isForwards = isForwards;
    }

    /**
     * Bezier curve
     *
     * @param s how far we have gone inside this motion
     * @return array of bezier curve element values, given s
     */
    private static double[] B(double s) {
        double[] result = new double[4];
        double r = 1.0 - s; // how far we have left
        result[0] = r * r * r;
        result[1] = 3.0 * r * r * s;
        result[2] = 3.0 * r * s * s;
        result[3] = s * s * s;
        return result;
    }

    /**
     * Derivative of B(s)
     */
    private static double[] dBds(double s) {
        double[] result = new double[4];
        double r = 1.0 - s;
        result[0] = -3.0 * r * r;
        result[1] = (3 * r * r) - (6 * r * s);
        result[2] = (-3 * s * s) + (6 * r * s);
        result[3] = 3.0 * s * s;
        return result;
    }

    @Override
    public final Pose evaluatePose(double s) {
        Point2D X = evaluate(B(s));
        Point2D dXds = evaluate(dBds(s));
        double theta = isForwards ? Math.atan2(dXds.getY(), dXds.getX())
            : Math.atan2(-dXds.getY(), -dXds.getX());
        return new Pose(X, theta); //Find the values for v and a and position
    }

    /**
     * Finds the next points to add into the curve
     *
     * @param shape the shape of the curve
     * @return the next point
     */
    private Point2D evaluate(double[] shape) {
        Point2D result = new Point2D(0, 0);
        for (int i = 0; i < controlPoints.length; i++) {
            result.setX(result.getX() + (shape[i] * controlPoints[i].getX()));
            result.setY(result.getY() + (shape[i] * controlPoints[i].getY()));
        }

        return result;
    }

    @Override
    public final MotionProvider.LimitMode getLimitMode() {
        return MotionProvider.LimitMode.LimitLinearAcceleration;
    }

    @Override
    public final double getLength() {
        return isForwards ? length : -length;
    }

    @Override
    public final double getInitialTheta() {
        return initialTheta;
    }

    @Override
    public final double getFinalTheta() {
        return finalTheta;
    }

    @Override
    public final String toString() {
        return "MotionPathSpline{" +
            "controlPoints=" + Arrays.toString(controlPoints) +
            ", length=" + length +
            ", initialTheta=" + initialTheta +
            ", finalTheta=" + finalTheta +
            ", isForwards=" + isForwards +
            ", vCruise=" + vCruise +
            ", aMax=" + aMax +
            '}';
    }
}
