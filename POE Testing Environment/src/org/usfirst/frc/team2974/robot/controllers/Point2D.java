package org.usfirst.frc.team2974.robot.controllers;

public class Point2D {

  private double x;
  private double y;

  public Point2D(final double x, final double y) {
    this.x = x;
    this.y = y;

  }

  /**
   * @param pose0 initial point
   * @param p how far the robot has gone
   * @param pose1 final point in the tiny sequence
   * @param q how far it has yet to go
   */
  public static Point2D interpolate(final Point2D pose0, final double p, final Point2D pose1,
      final double q) {
    return new Point2D((pose0.x * p) + (pose1.x * q), (pose0.y * p) + (pose1.y * q));
  }

  public final double getX() {
    return x;
  }

  public final void setX(final double x) {
    this.x = x;
  }

  public final double getY() {
    return y;
  }

  public final void setY(final double y) {
    this.y = y;
  }

  //Calculates distance between two points
  public final double distance(final Point2D other) {
    final double deltaX = StrictMath.pow(x - other.x, 2.0);
    final double deltaY = StrictMath.pow(y - other.y, 2.0);
    return Math.sqrt(deltaX + deltaY);
  }

  public final Point2D offsetPoint(final double l, final double angle) {
    return new Point2D(this.x + (l * StrictMath.cos(angle)), this.y + (l * StrictMath.sin(angle)));
  }

  public final String toString() {
    return String.format("x=%f, y=%f", x, y);
  }
}