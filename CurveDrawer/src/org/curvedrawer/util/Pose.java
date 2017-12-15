package org.curvedrawer.util;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import org.curvedrawer.Main;

public class Pose {
    private final SimpleDoubleProperty x;

    private final SimpleDoubleProperty y;

    private final SimpleDoubleProperty angle;
    private final SimpleDoubleProperty scaledX;
    private final SimpleDoubleProperty scaledY;

    public Pose(double x, double y, double angle) {
        this.x = new SimpleDoubleProperty(x);

        scaledX = new SimpleDoubleProperty();
        scaledX.bind(ReadOnlyDoubleWrapper.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(ReadOnlyDoubleWrapper.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.angle = new SimpleDoubleProperty(angle);
    }

    public double getScaledX() {
        return scaledX.get();
    }

    public SimpleDoubleProperty scaledXProperty() {
        return scaledX;
    }

    public double getScaledY() {
        return scaledY.get();
    }

    public SimpleDoubleProperty scaledYProperty() {
        return scaledY;
    }

    public double getX() {
        return x.get();
    }

    public SimpleDoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public SimpleDoubleProperty yProperty() {
        return y;
    }

    public double getAngle() {
        return angle.get();
    }

    public SimpleDoubleProperty angleProperty() {
        return angle;
    }

    /**
     * Offsets a point along a perpendicular line from a tangent line
     *
     * @param distance - the distance to offset the point by
     * @return the offset point
     */
    public final Pose offsetPerpendicular(double distance) {
        double angleOfDT = StrictMath.atan(getAngle());
        double offsetX = distance * StrictMath.cos(angleOfDT + (Math.PI / 2)); // Finds point at distance along perpendicular
        // line
        double offsetY = distance * StrictMath.sin(angleOfDT + (Math.PI / 2));

        return new Pose(getX() + offsetX, getY() + offsetY, angleOfDT);
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
