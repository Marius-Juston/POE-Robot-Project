package org.curvedrawer.util;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import org.curvedrawer.Main;
import org.curvedrawer.controller.SettingController;

public class Pose {
    private final SimpleDoubleProperty x;

    private final SimpleDoubleProperty y;

    private final SimpleDoubleProperty angle;
    private final SimpleDoubleProperty scaledX;
    private final SimpleDoubleProperty scaledY;

    public Pose(double x, double y, double angle) {
        this.x = new SimpleDoubleProperty(x);

        scaledX = new SimpleDoubleProperty();
        scaledX.bind(DoubleExpression.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(DoubleExpression.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.angle = new SimpleDoubleProperty(angle);
    }

    public final double getScaledX() {
        return scaledX.get();
    }

    public final SimpleDoubleProperty scaledXProperty() {
        return scaledX;
    }

    public final double getScaledY() {
        return scaledY.get();
    }

    public final SimpleDoubleProperty scaledYProperty() {
        return scaledY;
    }

    public final double getX() {
        return x.get();
    }

    public final SimpleDoubleProperty xProperty() {
        return x;
    }

    public final double getY() {
        return y.get();
    }

    public final SimpleDoubleProperty yProperty() {
        return y;
    }

    public final double getAngle() {
        return angle.get();
    }

    public final SimpleDoubleProperty angleProperty() {
        return angle;
    }

    /**
     * Offsets a point along a perpendicular line from a tangent line
     *
     * @param distance - the distance to offset the point by
     * @return the offset point
     */
    public Pose offsetPerpendicular(double distance) {
        double angleOfDT = StrictMath.atan(getAngle());
        double offsetX = distance * StrictMath.cos(angleOfDT + (Math.PI / 2.0)); // Finds point at distance along perpendicular
        // line
        double offsetY = distance * StrictMath.sin(angleOfDT + (Math.PI / 2.0));

        return new Pose(getX() + offsetX, getY() + offsetY, angleOfDT);
    }

    @Override
    public final String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                '}';
    }
}
