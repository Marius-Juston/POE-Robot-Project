package org.usfirst.frc.team2974.robot.controllers;

public class KinematicPose extends Pose {

  public final KinematicState left; // state of left side of robot
  public final KinematicState right; // state of right side of robot
  public final double t; // time
  public final boolean isFinished;

  public KinematicPose(Pose pose, KinematicState left, KinematicState right, double t, boolean isFinished) {
    super(pose.point, pose.angle);

    this.left = left;
    this.right = right;
    this.t = t;
    this.isFinished = isFinished;
  }

  /**
   * @see #interpolate(Pose, double, Pose, double)
   */
  public static KinematicPose interpolate(KinematicPose pose0, double p, KinematicPose pose1, double q) {
    Pose pose = Pose.interpolate(pose0, p, pose1, q);
    KinematicState left = KinematicState.interpolate(pose0.left, p, pose1.left, q);
    KinematicState right = KinematicState.interpolate(pose0.right, p, pose1.right, q);

    return new KinematicPose(pose, left, right, (p * pose0.t) + (q * pose1.t), false);
  }

  /**
   * @return length at center of robot
   */
  public double getCenterLength() {
    return (left.length + right.length) / 2f;
  }

  /**
   * @return velocity at center of robot
   */
  public double getCenterVelocity() {
    return (left.velocity + right.velocity) / 2f;
  }

  @Override
  public String toString() {
    return String.format("%s.Kinematic{left:%s, right:%s, t=%f, isFinished=%s}", super.toString(), left, right, t, isFinished);
  }

}
