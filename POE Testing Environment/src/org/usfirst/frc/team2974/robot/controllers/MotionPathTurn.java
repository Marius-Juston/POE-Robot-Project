package org.usfirst.frc.team2974.robot.controllers;

/**
 * JUST ANGLES, TURNING
 */
public class MotionPathTurn extends MotionProvider {

  private final Pose pose0;
  private final Pose pose1;

  public MotionPathTurn(final Pose pose0, final double dAngle, final double vCruise,
      final double rotationMaxAcceleration) {
    super(vCruise, rotationMaxAcceleration);
    this.pose0 = pose0;
    pose1 = new Pose(pose0.point, pose0.angle + MotionProvider.boundAngle(dAngle));
  }

  @Override
  public final Pose evaluatePose(final double s) {
    final double r = 1.0 - s;
    return new Pose(this.pose0.point, (r * this.pose0.angle) + (s * this.pose1.angle));
  }

  @Override
  public final MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitRotationalAcceleration;
  }

  @Override
  public final double getLength() {
    return 0;
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
    return "MotionPathTurn{" +
        "pose0=" + pose0 +
        ", pose1=" + pose1 +
        ", vCruise=" + vCruise +
        ", aMax=" + aMax +
        '}';
  }
}
