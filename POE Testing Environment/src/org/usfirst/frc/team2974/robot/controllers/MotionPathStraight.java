package org.usfirst.frc.team2974.robot.controllers;

/**
 * JUST DISTANCE, FORWARD/BACKWARDS
 */
public class MotionPathStraight extends MotionProvider {

  private final Pose pose0;
  private final Pose pose1;
  private final double length;

  public MotionPathStraight(final Pose pose0, final double distance, final double vCruise,
      final double aMax) {
    super(vCruise, aMax);
    synchronized (this) {
      this.pose0 = pose0;
      pose1 = new Pose(pose0.offsetPoint(distance), pose0.angle);
      length = distance;
    }
  }

  @Override
  public final Pose evaluatePose(final double s) {
    final Point2D X = Point2D.interpolate(this.pose0.point, 1.0 - s, this.pose1.point, s);
    final double angle = this.pose0.angle;
    return new Pose(X, angle);
  }

  @Override
  public final MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitLinearAcceleration;
  }

  @Override
  public final double getLength() {
    return this.length;
  }

  @Override
  public final double getInitialTheta() {
    return this.pose0.angle;
  }

  @Override
  public final double getFinalTheta() {
    return this.pose1.angle;
  }

  @Override
  public final String toString() {
    return "MotionPathStraight{" +
        "pose0=" + pose0 +
        ", pose1=" + pose1 +
        ", length=" + length +
        ", vCruise=" + vCruise +
        ", aMax=" + aMax +
        '}';
  }
}
