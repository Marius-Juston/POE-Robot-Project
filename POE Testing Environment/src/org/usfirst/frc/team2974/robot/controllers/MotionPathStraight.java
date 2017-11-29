package org.usfirst.frc.team2974.robot.controllers;

/**
 * JUST DISTANCE, FORWARD/BACKWARDS
 */
public class MotionPathStraight extends MotionProvider {

  private final Pose pose0;
  private final Pose pose1;
  private final double length;

  public MotionPathStraight(Pose pose0, double distance, double vCruise, double aMax) {
    super(vCruise, aMax);
    synchronized (this) {
      this.pose0 = pose0;
      pose1 = new Pose(pose0.offsetPoint(distance), pose0.angle);
      length = distance;
    }
  }

  @Override
  public Pose evaluatePose(double s) {
    Point2D X = Point2D.interpolate(this.pose0.point, 1.0 - s, this.pose1.point, s);
    double angle = this.pose0.angle;
    return new Pose(X, angle);
  }

  @Override
  public MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitLinearAcceleration;
  }

  @Override
  public double getLength() {
    return this.length;
  }

  @Override
  public double getInitialTheta() {
    return this.pose0.angle;
  }

  @Override
  public double getFinalTheta() {
    return this.pose1.angle;
  }

}
