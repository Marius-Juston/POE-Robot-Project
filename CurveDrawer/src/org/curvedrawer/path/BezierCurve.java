package org.curvedrawer.path;

import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

/**
 * Resources:
 * https://pages.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/Bezier/bezier-der.html
 */
public class BezierCurve extends Path {
    private double[] coefficients;

    /**
     * Creates a new bezier curve
     *
     * @param numberOfSteps - the amount of points to define the curve, the resolution of the curve
     * @param controlPoints - the control points that define the robot
     */
    public BezierCurve(int numberOfSteps, Point... controlPoints) {
        super(numberOfSteps, controlPoints);
        setCoefficients();
    }

    /**
     * Uses the formula to find the value of nCr
     *
     * @param n
     * @param r
     * @return nCr
     */
    private static double findNumberOfCombination(double n, double r) {
        double nFactorial = factorial(n);
        double rFactorial = factorial(r);
        double nMinusRFactorial = factorial(n - r);

        return nFactorial / (rFactorial * nMinusRFactorial);
    }

    /**
     * Finds the factorial of any integer or double, d
     *
     * @param d
     * @return the factorial of d
     */
    private static double factorial(double d) {
        double r = d - Math.floor(d) + 1;
        for (; d > 1; d -= 1) {
            r *= d;
        }
        return r;
    }

    /**
     * @return an array of Points that define the curve
     */
    private Pose[] getCurvePoints() {
        Pose[] poses = new Pose[getNumberOfSteps() + 1];

        for (double i = 0; i <= getNumberOfSteps(); i++) {
            poses[(int) i] = getPoint(i / ((double) getNumberOfSteps()), getPoints().toArray(new Point[0]));
        }

        return poses;
    }

    /**
     * Updates the coefficients used for calculations
     */
    private void setCoefficients() {
        int n = getDegree();
        coefficients = new double[n + 1];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = findNumberOfCombination(n, i);
        }
    }

    /**
     * Returns the point on the curve at any percentage on the line, t
     *
     * @param percentage    - t
     * @param controlPoints
     * @return the Point that is at percentage t along the curve
     */
    private Pose getPoint(double percentage, Point[] controlPoints) {
        double xCoordinateAtPercentage = 0;
        double yCoordinateAtPercentage = 0;

        int n = getDegree();

        if (coefficients.length != n + 1) {
            setCoefficients();
        }

        for (double i = 0; i <= n; i++) {
            double coefficient = coefficients[(int) i];

            double oneMinusT = Math.pow(1 - percentage, n - i);

            double powerOfT = Math.pow(percentage, i);

            Point pointI = controlPoints[(int) i];

            xCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getX());
            yCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getY());
        }

        return new Pose(xCoordinateAtPercentage, yCoordinateAtPercentage, getDT(percentage));
    }

    /**
     * @return the degree of the curve
     */
    private int getDegree() {
        return getPoints().size() - 1;
    }

    /**
     * Given the control points defining the curve, find the derivative at any point
     * on the curve
     *
     * @param t - percent along curve
     * @return derivative at point
     */
    private double getDT(double t) {
        if (t < 1 && getPoints().size() > 0) {
            int n = getDegree();
            double dx = 0;
            double dy = 0;

            for (int i = 0; i < n; i++) {
                double coefficient = findNumberOfCombination(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
                dx += coefficient * (n + 1) * (getPoints().get(i + 1).getX() - getPoints().get(i).getX());
                dy += coefficient * (n + 1) * (getPoints().get(i + 1).getY() - getPoints().get(i).getY());
            }

            if (dx == 0)
                return 0;

            return dy / dx;  //FIXME what to do if dx == 0? it will return NaN
        } else if (getPoints().size() > 1) {

            return (getPoints().get(getPoints().size() - 1).getY() - getPoints().get(getPoints().size() - 2).getY()) / (getPoints().get(getPoints().size() - 1).getX() - getPoints().get(getPoints().size() - 2).getX());
        } else {
            return 0;
        }

    }


    @Override
    public Pose[] createPathPoses() {
        return getCurvePoints();
    }
}
