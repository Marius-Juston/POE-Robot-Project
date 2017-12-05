package org.usfirst.frc.team2974.robot.controllers;

public class Point2D {

    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Interpolates pose0 and pose1.
     *
     * @param point0 initial point
     * @param p how far the robot has gone
     * @param point1 final point in the tiny sequence
     * @param q how far it has yet to go
     * @return the interpolated point
     */
    public static Point2D interpolate(Point2D point0, double p, Point2D point1, double q) {
        return new Point2D((point0.x * p) + (point1.x * q), (point0.y * p) + (point1.y * q));
    }

    public final double getX() {
        return x;
    }

    public final void setX(double x) {
        this.x = x;
    }

    public final double getY() {
        return y;
    }

    public final void setY(double y) {
        this.y = y;
    }

    /**
     * Calculates distance between two points
     *
     * @param other another point
     * @return distance from this to other
     */
    public final double distance(Point2D other) {
        double deltaX = Math.pow(x - other.x, 2.0);
        double deltaY = Math.pow(y - other.y, 2.0);
        return Math.sqrt(deltaX + deltaY);
    }

    /**
     * Offsets point with {@code l} along {@code angle}.
     *
     * @param l distance to offset by
     * @param angle angle to offset with
     * @return offset point
     */
    public final Point2D offsetPoint(double l, double angle) {
        return new Point2D(x + (l * Math.cos(angle)), y + (l * Math.sin(angle)));
    }

    public final String toString() {
        return String.format("Point2D{x=%f, y=%f}", x, y);
    }
}