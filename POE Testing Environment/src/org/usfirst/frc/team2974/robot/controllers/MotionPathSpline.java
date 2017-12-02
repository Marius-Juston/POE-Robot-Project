package org.usfirst.frc.team2974.robot.controllers;


public class MotionPathSpline extends MotionProvider {

  private final Point2D[] controlPoints = new Point2D[4];
  private final double length;
  private final double angle0; // start angle
  private final double angle1; // end angle
  private final boolean isForwards;

  public MotionPathSpline(Pose initial, double l0, Pose final_, double l1, double vCruise,
      double aMax, boolean isForwards) {
    super(vCruise, aMax);
    controlPoints[0] = initial.point;
    controlPoints[1] = initial.offsetPoint(isForwards ? l0 : -l0);
    controlPoints[2] = final_.offsetPoint(isForwards ? -l1 : l1);
    controlPoints[3] = final_.point;

    Point2D Xprev = this.evaluate(this.B(0));
    double length = 0;
    for (int i = 1; i <= 100; i++) {
      double s = (double) i / 100.0;
      Point2D Xnext = this.evaluate(this.B(s));
      length += Xprev.distance(Xnext);
      Xprev = Xnext;
    }

    this.length = length;
    angle0 = initial.angle;
    angle1 = final_.angle;
    this.isForwards = isForwards;
    System.out.println(initial);
    System.out.println(this.controlPoints[1]);
    System.out.println(this.controlPoints[2]);
    System.out.println(final_);

  }


  @Override
  public Pose evaluatePose(double s) {
    Point2D X = this.evaluate(this.B(s));
    Point2D dXds = this.evaluate(this.dBds(s));
    double theta;
    if (this.isForwards) {
      theta = Math.atan2(dXds.y, dXds.x);
    } else {
      theta = Math.atan2(-dXds.y, -dXds.x);
    }
    return new Pose(X, theta); //Find the values for v and a and position
  }

  /**
   * Bezier curve
   *
   * @param s how far we have gone inside this motion
   * @return array of bezier curve element values, given s
   */
  private double[] B(double s) {
    double[] result = new double[4];
    double r = 1 - s; // how far we have left
    result[0] = r * r * r;
    result[1] = 3 * r * r * s;
    result[2] = 3 * r * s * s;
    result[3] = s * s * s;
    return result;
  }

  /**
   * Derivative of B(s)
   */
  private double[] dBds(double s) {
    double[] result = new double[4];
    double r = 1 - s;
    result[0] = -3 * r * r;
    result[1] = 3 * r * r - 6 * r * s;
    result[2] = -3 * s * s + 6 * r * s;
    result[3] = 3 * s * s;
    return result;
  }

  /**
   * Finds the next points to add into the curve
   *
   * @param shape the shape of the curve
   * @return the next point
   */
  private Point2D evaluate(double[] shape) {
    Point2D result = new Point2D(0, 0);
    for (int i = 0; i < 4; i++) {
      result.x += shape[i] * this.controlPoints[i].x;
      result.y += shape[i] * this.controlPoints[i].y;
    }
    return result;
  }

  @Override
  public MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitLinearAcceleration;
  }

  @Override
  public double getLength() {
    return this.isForwards ? this.length : -this.length;
  }

  @Override
  public double getInitialTheta() {
    return this.angle0;
  }

  @Override
  public double getFinalTheta() {
    return this.angle1;
  }


}
