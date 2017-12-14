package org.curvedrawer.util;

public class Pose {
    private final double x;
    private final double y;
    private final double angle;

    public Pose(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getAngle() {
        return angle;
    }

    /**
     * Offsets a point along a perpendicular line from a tangent line
     *
     * @param distance - the distance to offset the point by
     * @return the offset point
     */
    public final Pose offsetPerpendicular(double distance) {
        double angleOfDT = StrictMath.atan(angle);
        double offsetX = distance * StrictMath.cos(angleOfDT + (Math.PI / 2)); // Finds point at distance along perpendicular
        // line
        double offsetY = distance * StrictMath.sin(angleOfDT + (Math.PI / 2));

        return new Pose(x + offsetX, y + offsetY, angleOfDT);
    }

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                '}';
    }
}
