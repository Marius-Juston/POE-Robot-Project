package org.waltonrobotics.curvedrawer.curve;


import org.waltonrobotics.curvedrawer.util.Point;
import org.waltonrobotics.curvedrawer.util.Vector2;

/**
 * Resources: https://pages.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/Bezier/bezier-der.html
 */
public class BezierCurve extends Path {

  private Point[] pathPoints;
  private Point[] leftPoints;
  private Point[] rightPoints;
  private Point[] controlPoints;
  private double[] coefficients;

  public BezierCurve(int numberOfSteps, double robotLength, String name, Point... controlPoints) {
    super(numberOfSteps, robotLength, name);
    this.controlPoints = controlPoints;

    updateCoefficients();
    pathPoints = getCurvePoints(numberOfSteps, controlPoints);

    rightPoints = offsetPoints(pathPoints, true);
    leftPoints = offsetPoints(pathPoints, false);
  }

  /**
   * n! / i!(n-i)!
   */
  private static double findNumberOfCombination(double n, double i) {
    double nFactorial = factorial(n);
    double iFactorial = factorial(i);
    double nMinusIFactorial = factorial(n - i);

    return nFactorial / (iFactorial * nMinusIFactorial);
  }

  /**
   * for decimal number and integers
   */
  private static double factorial(double d) {
    double r = d - Math.floor(d) + 1;
    for (; d > 1; d -= 1) {
      r *= d;
    }
    return r;
  }

  private Point[] getCurvePoints(int numberOfSteps, Point[] controlPoints) {
    Point[] point2DList = new Point[numberOfSteps + 1];

    for (double i = 0; i <= numberOfSteps; i++) {
      point2DList[(int) i] = getPoint(i / ((double) numberOfSteps), controlPoints);
    }

    return point2DList;
  }

  private Vector2[] getVectors(int numberOfSteps) {
    Vector2[] point2DList = new Vector2[numberOfSteps + 1];

    for (double i = 0; i <= numberOfSteps; i++) {
      point2DList[(int) i] = getVelocity(i / ((double) numberOfSteps));
    }

    return point2DList;
  }

  private void updateCoefficients() {
    int n = getDegree();
    coefficients = new double[n + 1];
    for (int i = 0; i < coefficients.length; i++) {
      coefficients[i] = findNumberOfCombination(n, i);
    }
  }

  private Point getPoint(double percentage, Point[] controlPoints) {
    double xCoordinateAtPercentage = 0;
    double yCoordinateAtPercentage = 0;

    int n = getDegree();

    for (double i = 0; i <= n; i++) {
      double coefficient = coefficients[(int) i];

      double oneMinusT = Math.pow(1 - percentage, n - i);

      double powerOfT = Math.pow(percentage, i);

      Point pointI = controlPoints[(int) i];

      xCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getX());
      yCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getY());
    }

    return new Point(xCoordinateAtPercentage, yCoordinateAtPercentage, getDerivative(percentage));
  }

  private Vector2 getVelocity(double percentage) {
    double leftVelocity = 0;
    double rightVelocity = 0;

    int n = getDegree() - 1;

    for (double i = 0; i <= n; i++) {
      double coefficient = findNumberOfCombination(n, i);

      double oneMinusT = Math.pow(1 - percentage, n - i); // TODO Fix math

      double powerOfT = Math.pow(percentage, i);

      // double dt = getDerivative(percentage);
      //
      // leftVelocity += (coefficient * oneMinusT * powerOfT * (dt));
      // rightVelocity += (coefficient * oneMinusT * powerOfT * (dt));
    }

    double slope = rightVelocity / leftVelocity;

    leftVelocity = leftVelocity - (getRobotLength() / 2 * slope);
    rightVelocity = rightVelocity + (getRobotLength() / 2 * slope);

    return new Vector2(leftVelocity, rightVelocity);
  }

  private int getDegree() {
    return controlPoints.length - 1;
  }

  /**
   * Given the control points defining the curve, find the derivative at any util on the curve
   *
   * @param t - percent along curve
   * @return derivative at util
   */
  private double getDerivative(double t) {
    int n = getDegree();
    double dx = 0;
    double dy = 0;
    for (int i = 0; i < n; i++) {
      double coefficient = findNumberOfCombination(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
      dx += coefficient * (n + 1) * (controlPoints[i + 1].getX() - controlPoints[i].getX());
      dy += coefficient * (n + 1) * (controlPoints[i + 1].getY() - controlPoints[i].getY());
    }
    return dy / dx;
  }

  /**
   * Offsets control points of a curve
   */
  private Point[] offsetPoints(Point[] pathPoints, boolean isRightSide) {
    int n = pathPoints.length;
    Point[] offsetPoints = new Point[n];
    for (int i = 0; i < n; i++) {
      offsetPoints[i] = pathPoints[i].offsetPerpendicular(pathPoints[i].getDerivative(),
          isRightSide ? getRobotLength() : -getRobotLength());
    }
    return offsetPoints;
  }

  @Override
  public Point[] getPathPoints() {
    return pathPoints;
  }

  @Override
  public void setPathPoints(Point... controlPoints) {
    this.controlPoints = controlPoints;

    updateCoefficients();
    pathPoints = getCurvePoints(getNumberOfSteps(), controlPoints);

    rightPoints = offsetPoints(pathPoints, true);
    leftPoints = offsetPoints(pathPoints, false);
  }

  @Override
  public Point[] getLeftPath() {
    return leftPoints;
  }

  @Override
  public Point[] getRightPath() {
    return rightPoints;
  }
}
