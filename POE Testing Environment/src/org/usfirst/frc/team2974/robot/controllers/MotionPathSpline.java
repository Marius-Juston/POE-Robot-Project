package org.usfirst.frc.team2974.robot.controllers;


import java.util.Arrays;

public class MotionPathSpline extends MotionProvider {

  private final Point2D[] controlPoints = new Point2D[4];
  private final double length;
  private final double initialTheta; // start angle
  private final double finalTheta; // end angle
  private final boolean isForwards;

  public MotionPathSpline(final Pose initial, final double l0, final Pose final_, final double l1,
      final double vCruise,
      final double aMax, final boolean isForwards) {
    super(vCruise, aMax);
    controlPoints[0] = initial.point;
    controlPoints[1] = initial.offsetPoint(isForwards ? l0 : -l0);
    controlPoints[2] = final_.offsetPoint(isForwards ? -l1 : l1);
    controlPoints[3] = final_.point;

    Point2D xPrevious = this.evaluate(MotionPathSpline.B(0));
    double length = 0;
    for (int i = 1; i <= 100; i++) {
      final double s = i / 100.0;
      final Point2D xNext = this.evaluate(MotionPathSpline.B(s));
      length += xPrevious.distance(xNext);
      xPrevious = xNext;
    }

    this.length = length;
    initialTheta = initial.angle;
    finalTheta = final_.angle;
    this.isForwards = isForwards;
    System.out.println(initial);
    System.out.println(this.controlPoints[1]);
    System.out.println(this.controlPoints[2]);
    System.out.println(final_);

  }

  /**
   * Bezier curve
   *
   * @param s how far we have gone inside this motion
   * @return array of bezier curve element values, given s
   */
  private static double[] B(final double s) {
    final double[] result = new double[4];
    final double r = 1.0 - s; // how far we have left
    result[0] = r * r * r;
    result[1] = 3.0 * r * r * s;
    result[2] = 3.0 * r * s * s;
    result[3] = s * s * s;
    return result;
  }

  /**
   * Derivative of B(s)
   */
  private static double[] dBds(final double s) {
    final double[] result = new double[4];
    final double r = 1.0 - s;
    result[0] = -3.0 * r * r;
    result[1] = (3 * r * r) - (6 * r * s);
    result[2] = (-3 * s * s) + (6 * r * s);
    result[3] = 3.0 * s * s;
    return result;
  }

  @Override
  public final Pose evaluatePose(final double s) {
    final Point2D X = this.evaluate(MotionPathSpline.B(s));
    final Point2D dXds = this.evaluate(MotionPathSpline.dBds(s));
    final double theta;
    theta = this.isForwards ? StrictMath.atan2(dXds.getY(), dXds.getX())
        : StrictMath.atan2(-dXds.getY(), -dXds.getX());
    return new Pose(X, theta); //Find the values for v and a and position
  }

  /**
   * Finds the next points to add into the curve
   *
   * @param shape the shape of the curve
   * @return the next point
   */
  private Point2D evaluate(final double[] shape) {
    final Point2D result = new Point2D(0, 0);
    for (int i = 0; i < controlPoints.length; i++) {
      result.setX(result.getX() + (shape[i] * this.controlPoints[i].getX()));
      result.setY(result.getY() + (shape[i] * this.controlPoints[i].getY()));
    }
    return result;
  }

  @Override
  public final MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitLinearAcceleration;
  }

  @Override
  public final double getLength() {
    return this.isForwards ? this.length : -this.length;
  }

  @Override
  public final double getInitialTheta() {
    return this.initialTheta;
  }

  @Override
  public final double getFinalTheta() {
    return this.finalTheta;
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
