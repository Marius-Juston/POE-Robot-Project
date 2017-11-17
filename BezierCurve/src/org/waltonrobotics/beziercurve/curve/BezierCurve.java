package org.waltonrobotics.beziercurve.curve;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import org.waltonrobotics.beziercurve.point.ArrowPoint;
import org.waltonrobotics.beziercurve.point.Point;

public class BezierCurve extends Curve {

  private double[] coefficients;

  public BezierCurve() {
    this(new ArrayList<>());
  }

  public BezierCurve(List<Point> point2DS) {
    super(FXCollections.observableArrayList(point2DS));

    addListChangeListener(c -> updateCoefficients());
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

  public double[] getXs() {
    return points.stream().mapToDouble(Point::getCenterX).toArray();
  }

  public double[] getYs() {
    return points.stream().mapToDouble(Point::getCenterY).toArray();
  }

  public List<Point> getCurvePoints(int numberOfSteps) {
    List<Point> point2DList = new ArrayList<>(numberOfSteps + 1);

    for (double i = 0; i <= numberOfSteps; i++) {
      point2DList.add(getPoint(i / ((double) numberOfSteps)));
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

  private Point getPoint(double percentage) {
    double xCoordinateAtPercentage = 0;
    double yCoordinateAtPercentage = 0;

    int n = getDegree();

    for (double i = 0; i <= n; i++) {
      double coefficient = coefficients[(int) i];

      double oneMinusT = Math.pow(1 - percentage, n - i);

      double powerOfT = Math.pow(percentage, i);

      Point pointI = points.get((int) i);

      xCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getCenterX());
      yCoordinateAtPercentage += (coefficient * oneMinusT * powerOfT * pointI.getCenterY());
    }

    return new ArrowPoint(xCoordinateAtPercentage, yCoordinateAtPercentage);
  }


  private int getDegree() {
    return points.size() - 1;
  }
}
