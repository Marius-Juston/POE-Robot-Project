package org.curvedrawer.util;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import org.curvedrawer.Main;

/**
 * This class is supposed to represent a position on a path. It has a x,y and a heading (angle).
 */
public class Pose {
    private final SimpleDoubleProperty x; //x coordinates

    private final SimpleDoubleProperty y; // y coordinates

    private final SimpleDoubleProperty angle; //angle value
    private final SimpleDoubleProperty scaledX; // scaled x
    private final SimpleDoubleProperty scaledY; // scaled y

    /**
     * Initializes the instance variables
     *
     * @param x     x coordinate
     * @param y     y coordinate
     * @param angle angle coordinate
     */
    public Pose(double x, double y, double angle) {
        this.x = new SimpleDoubleProperty(x);

        scaledX = new SimpleDoubleProperty();
        scaledX.bind(DoubleExpression.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(DoubleExpression.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.angle = new SimpleDoubleProperty(angle);
    }

    /**
     * Gets the scaled x. The scaled x is the x coordinate divided by the scale factor
     *
     * @return the scaled x coordinate
     */
    public final double getScaledX() {
        return scaledX.get();
    }

    /**
     * Returns the scaledX property
     *
     * @return scaledX property
     */
    public final SimpleDoubleProperty scaledXProperty() {
        return scaledX;
    }


    /**
     * Gets the scaled y. The scaled y is the y coordinate divided by the scale factor
     *
     * @return the scaled y coordinate
     */
    public final double getScaledY() {
        return scaledY.get();
    }

    /**
     * Returns the scaledY property
     *
     * @return scaledY property
     */
    public final SimpleDoubleProperty scaledYProperty() {
        return scaledY;
    }

    /**
     * Returns the x coordinate
     *
     * @return x coordinate of the pose
     */
    public final double getX() {
        return x.get();
    }

    /**
     * Returns the x coordinate property
     *
     * @return x property
     */
    public final SimpleDoubleProperty xProperty() {
        return x;
    }


    /**
     * Returns the y coordinate
     *
     * @return y coordinate of the pose
     */
    public final double getY() {
        return y.get();
    }


    /**
     * Returns the y coordinate property
     *
     * @return y property
     */
    public final SimpleDoubleProperty yProperty() {
        return y;
    }

    /**
     * Returns the angle of the pose
     *
     * @return pose angle in radians
     */
    public final double getAngle() {
        return angle.get();
    }

    /**
     * Returns the angle property
     *
     * @return angle property
     */
    public final SimpleDoubleProperty angleProperty() {
        return angle;
    }

    /**
     * Offsets a point along a perpendicular line from a tangent line given the angle of the pose
     *
     * @param distance - the distance to offset the point by
     * @return the offset point
     */
    public final Pose offsetPerpendicular(double distance) {
        double angleOfDT = StrictMath.atan(getAngle());
        double offsetX = distance * StrictMath.cos(angleOfDT + (Math.PI / 2.0)); // Finds point at distance along perpendicular
        // line
        double offsetY = distance * StrictMath.sin(angleOfDT + (Math.PI / 2.0));

        return new Pose(getX() + offsetX, getY() + offsetY, angleOfDT);
    }

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", scaledX=" + scaledX +
                ", scaledY=" + scaledY +
                '}';
    }
}
