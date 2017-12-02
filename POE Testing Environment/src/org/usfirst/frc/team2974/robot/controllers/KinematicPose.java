package org.usfirst.frc.team2974.robot.controllers;

public class KinematicPose extends Pose {

  public final KinematicState left; // state of left side of robot
  public final KinematicState right; // state of right side of robot
  public final double t; // time
  public final boolean isFinished;

  KinematicPose(final Pose pose, final KinematicState left, final KinematicState right,
      final double t,
      final boolean isFinished) {
    super(pose.point, pose.angle);
    this.left = left;
    this.right = right;
    this.t = t;
    this.isFinished = isFinished;
  }

  public static KinematicPose interpolate(final KinematicPose pose0, final double p,
      final KinematicPose pose1,
      final double q) {
    final Pose pose = Pose.interpolate(pose0, p, pose1, q);
    final KinematicState left = KinematicState.interpolate(pose0.left, p, pose1.left, q);
    final KinematicState right = KinematicState.interpolate(pose0.right, p, pose1.right, q);
    return new KinematicPose(pose, left, right, (p * pose0.t) + (q * pose1.t), false);
  }

  // length center of robot
  public double getCenterLength() {
    return (left.length + right.length) / 2.0;
  }

  // velocity center of robot
  public double getCenterVelocity() {
    return (left.velocity + right.velocity) / 2.0;
  }

  public final String toString() {
    return String
        .format("%s, left:%s, right:%s, t=%f, isFinished=%s", super.toString(), this.left,
            this.right, this.t,
            this.isFinished);
  }
}
