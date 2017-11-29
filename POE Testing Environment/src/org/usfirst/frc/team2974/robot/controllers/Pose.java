package org.usfirst.frc.team2974.robot.controllers;

public class Pose {

  public final Point2D point;
  public final double angle;

  public Pose(Point2D point, double angle) {
    this.point = point;
    this.angle = angle;
  }

  static Pose interpolate(Pose pose0, double p, Pose pose1, double q) {
    return new Pose(Point2D.interpolate(pose0.point, p, pose1.point, q),
        p * pose0.angle + q * pose1.angle);
  }

  public Point2D offsetPoint(double l) {
    return new Point2D(this.point.x + l * Math.cos(this.angle), this.point.y + l * Math.sin(this.angle));
  }

  public String toString() {
    return String.format("x=%f, y=%f, angle=%f", this.point.x, this.point.y, this.angle);
  }

}
