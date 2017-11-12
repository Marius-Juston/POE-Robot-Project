package org.waltonrobotics.beziercurve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

public class BezierCurve implements Serializable {

  private static final long serialVersionUID = -4267911601533053275L;
  private final int numberOfSteps;
  private List<Point2D> points;
  private double[] coefficients;

  public BezierCurve(int numberOfSteps) {
    this.numberOfSteps = numberOfSteps;
    this.points = new ArrayList<>();
    coefficients = new double[0];
  }

  /**
   * n! / i!(n-i)!
   */
  private static double findNumberOfCombination(double n, double i) {
    double n_factorial = factorial(n);
    double i_factorial = factorial(i);
    double n_minus_i_factorial = factorial(n - i);

    return n_factorial / (i_factorial * n_minus_i_factorial);
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

  public List<Point2D> getCurvePoints() {
    List<Point2D> point2DList = new ArrayList<>(numberOfSteps + 1);

    for (double i = 0; i <= numberOfSteps; i++) {
      point2DList.add(getPoint(i / ((double) numberOfSteps)));
    }

    return point2DList;
  }

  public void addPoint(Point2D point2D) {
    points.add(point2D);
    updateCoefficients();
  }

  public void addAllPoints(List<Point2D> point2D) {
    points.addAll(point2D);
    updateCoefficients();
  }

  public void setPoints(List<Point2D> point2D) {
    points = point2D;
    updateCoefficients();
  }

  private void updateCoefficients() {
    int n = getDegree();
    coefficients = new double[n + 1];
    for (int i = 0; i < coefficients.length; i++) {
      coefficients[i] = findNumberOfCombination(n, i);
    }
  }

  private Point2D getPoint(double t) {
    double tX = 0;
    double tY = 0;

    int n = getDegree();

    for (double i = 0; i <= n; i++) {
      double coefficient = coefficients[(int) i];

      double one_minus_t = Math.pow(1 - t, n - i);

      double power_of_t = Math.pow(t, i);

      Point2D point_i = points.get((int) i);

      tX += (coefficient * one_minus_t * power_of_t * point_i.getX());
      tY += (coefficient * one_minus_t * power_of_t * point_i.getY());
    }

    return new Point2D(tX, tY);
  }

  private int getDegree() {
    return points.size() - 1;
  }
}
