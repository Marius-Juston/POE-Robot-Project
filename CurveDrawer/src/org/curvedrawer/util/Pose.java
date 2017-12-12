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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    /**
     * Offsets a point along a perpendicular line from a tangent line
     *
     * @param distance - the distance to offset the point by
     * @return the offset point
     */
    public Pose offsetPerpendicular(double distance) {
        double angleOfDT = Math.atan(angle);
        double offsetX = distance * Math.cos(angleOfDT + Math.PI / 2); // Finds point at distance along perpendicular
        // line
        double offsetY = distance * Math.sin(angleOfDT + Math.PI / 2);

        return new Pose(this.x + offsetX, this.y + offsetY, angleOfDT);
    }
}
