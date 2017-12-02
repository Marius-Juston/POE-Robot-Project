package org.usfirst.frc.team2974.robot.controllers;

public class Point2D {

  private double x;
  private double y;

  public Point2D(double x, double y) {
    this.x = x;
    this.y = y;

  }

  /**
   * @param pose0 initial point
   * @param p how far the robot has gone
   * @param pose1 final point in the tiny sequence
   * @param q how far it has yet to go
   */
  public static Point2D interpolate(Point2D pose0, double p, Point2D pose1, double q) {
    return new Point2D(pose0.x * p + pose1.x * q, pose0.y * p + pose1.y * q);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  //Calculates distance between two points
  public double distance(Point2D other) {
    double deltaX = Math.pow(x - other.x, 2);
    double deltaY = Math.pow(y - other.y, 2);
    return Math.sqrt(deltaX + deltaY);
  }

  public Point2D offsetPoint(double l, double angle) {
    return new Point2D(this.x + l * Math.cos(angle), this.y + l * Math.sin(angle));
  }

  public String toString() {
    return String.format("x=%f, y=%f", x, y);
  }
}