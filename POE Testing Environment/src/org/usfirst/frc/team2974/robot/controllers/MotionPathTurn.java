package org.usfirst.frc.team2974.robot.controllers;

/**
 * JUST ANGLES, TURNING
 */
public class MotionPathTurn extends MotionProvider {

  private final Pose pose0;
  private final Pose pose1;

  public MotionPathTurn(Pose pose0, double dAngle, double vCruise, double rotAccelMax) {
    super(vCruise, rotAccelMax);
    this.pose0 = pose0;
    pose1 = new Pose(pose0.point, pose0.angle + MotionProvider.boundAngle(dAngle));
  }

  @Override
  public Pose evaluatePose(double s) {
    double r = 1.0 - s;
    return new Pose(this.pose0.point, r * this.pose0.angle + s * this.pose1.angle);
  }

  @Override
  public MotionProvider.LimitMode getLimitMode() {
    return MotionProvider.LimitMode.LimitRotationalAcceleration;
  }

  @Override
  public double getLength() {
    return 0;
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
