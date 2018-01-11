package org.curvedrawer.path;

import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.util.Arrays;

/**
 * Resources:
 * https://pages.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/Bezier/bezier-der.html
 * https://medium.freecodecamp.org/nerding-out-with-bezier-curves-6e3c0bc48e2f
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
     * Given n items in how many ways can you choose i items out of it ( n is always greater than or equal to i).
     *
     * @param n number of items
     * @param i how many items to choose
     * @return nCr returns how many ways you can choose i from n
     */
    private static double findNumberOfCombination(double n, double i) {
        double nFactorial = factorial(n);
        double rFactorial = factorial(i);
        double nMinusRFactorial = factorial(n - i);

        return nFactorial / (rFactorial * nMinusRFactorial);
    }

    /**
     * Finds the factorial of any integer or double, d
     *
     * @param d double to find the factorial of
     * @return the factorial of d
     */
    private static double factorial(double d) {
        double d1 = d;
        double r = (d1 - Math.floor(d1)) + 1.0;
        while (d1 > 1.0) {
            r *= d1;
            d1 -= 1.0;
        }
        return r;
    }

    /**
     * Creates an array of poses by looping through getPoint numberOfSteps times.
     *
     * @return an array of Points that define the curve
     */
    @Override
    public final Pose[] createPathPoses() {
        int numberOfSteps = getNumberOfSteps();

        Pose[] poses = new Pose[numberOfSteps];
        Point[] points = getPoints().toArray(new Point[getPoints().size()]);

        for (double i = 0; i < numberOfSteps; i++) {
            poses[(int) i] = getPoint(i / (numberOfSteps - 1), points);
        }

        return poses;
    }

    /**
     * Updates the coefficients array used to define the curve polynomial
     */
    private void setCoefficients() {
        int n = getDegree();
        coefficients = new double[n + 1];
        for (int i = coefficients.length - 1; i >= 0; i--) {
            coefficients[i] = findNumberOfCombination(n, i);
        }
    }

    /**
     * Returns the point on the curve at any percentage on the line, t
     *
     * @param percentage    percentage on the line must be in the range of 0 and 1 inclusive
     * @param controlPoints the control Points to pass in to define the curve
     * @return the Point that is at percentage t along the curve
     */
    private Pose getPoint(double percentage, Point[] controlPoints) {
        if ((percentage < 0) || (percentage > 1.0))
            throw new RuntimeException("");

        double xCoordinateAtPercentage = 0;
        double yCoordinateAtPercentage = 0;

        int n = getDegree();

        if (coefficients.length != (n + 1)) {
            setCoefficients();
        }

        for (double i = 0; i <= n; i++) {
            double coefficient = coefficients[(int) i];

            double oneMinusT = StrictMath.pow(1.0 - percentage, n - i);

            double powerOfT = StrictMath.pow(percentage, i);

            Point pointI = controlPoints[(int) i];

            xCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getX());
            yCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getY());
        }

        return new Pose(xCoordinateAtPercentage, yCoordinateAtPercentage, getDerivative(percentage));
    }

    /**
     * Returns the degree of the curve number of points - 1
     *
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
    private double getDerivative(double t) {
        if ((t < 1.0) && !getPoints().isEmpty()) {
            int n = getDegree();
            double dx = 0;
            double dy = 0;

            for (int i = 0; i < n; i++) {
                double coefficient = findNumberOfCombination(n, i) * StrictMath.pow(t, i) * StrictMath.pow(1.0 - t, n - i);
                dx += coefficient * (n + 1) * (getPoints().get(i + 1).getX() - getPoints().get(i).getX());
                dy += coefficient * (n + 1) * (getPoints().get(i + 1).getY() - getPoints().get(i).getY());
            }

            if (dx == 0)
                return 0;

            return Math.atan(dy / dx);
        } else if (getPoints().size() > 1) {

            return Math.atan((getPoints().get(getPoints().size() - 1).getY() - getPoints().get(getPoints().size() - 2).getY()) / (getPoints().get(getPoints().size() - 1).getX() - getPoints().get(getPoints().size() - 2).getX()));
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return "BezierCurve{" +
                "coefficients=" + Arrays.toString(coefficients) +
                "} " + super.toString();
    }
}
