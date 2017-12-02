package org.usfirst.frc.team2974.robot.controllers;

public class Pose {

  public final Point2D point;
  public final double angle;

  public Pose(final Point2D point, final double angle) {
    this.point = point;
    this.angle = angle;
  }

  static Pose interpolate(final Pose pose0, final double p, final Pose pose1, final double q) {
    return new Pose(Point2D.interpolate(pose0.point, p, pose1.point, q),
        (p * pose0.angle) + (q * pose1.angle));
  }

  public final Point2D offsetPoint(final double l) {
    return new Point2D(this.point.getX() + (l * StrictMath.cos(this.angle)),
        this.point.getY() + (l * StrictMath.sin(this.angle)));
  }

  public String toString() {
    return String.format("x=%f, y=%f, angle=%f", this.point.getX(), this.point.getY(), this.angle);
  }

}
