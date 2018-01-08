package org.curvedrawer.util;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.curvedrawer.Main;

/**
 * This class is meant to be utilized to define a key points that define a path such as knots for splines and control points for bezier curves.
 * This has an x,y and a name
 */
public class Point {
    private final SimpleDoubleProperty x; // x coordinates
    private final SimpleDoubleProperty scaledX; // scaled x coordinates

    private final SimpleDoubleProperty y; // y coordinates
    private final SimpleDoubleProperty scaledY; //scaled y coordinates

    private final SimpleStringProperty name; // name of the point

    /**
     * Initializes the instance variables
     *
     * @param x    x coordinate
     * @param y    y coordinate
     * @param name name coordinate
     */
    public Point(double x, double y, String name) {
        this.x = new SimpleDoubleProperty(x);

        scaledX = new SimpleDoubleProperty();
        scaledX.bind(DoubleExpression.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(DoubleExpression.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.name = new SimpleStringProperty(name);
    }

    /**
     * Initializes the instance variables
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(double x, double y) {
        this(x, y, null);
    }

    /**
     * Initializes the instance variables
     *
     * @param point2D point2D to pass in. It will use the x and y coordinates of the point 2D as its own.
     */
    public Point(Point2D point2D) {
        this(point2D.getX(), point2D.getY());
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
     * Sets the x coordinate
     *
     * @param x new x coordinate for the point
     */
    public final void setX(double x) {
        this.x.set(x);
    }

    /**
     * Returns the x coordinate property
     *
     * @return x property
     */
    public final ObservableValue xProperty() {
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
     * Sets the y coordinate
     *
     * @param y new y coordinate value
     */
    public final void setY(double y) {
        this.y.set(y);
    }

    /**
     * Returns the y coordinate property
     *
     * @return y property
     */
    public final ObservableValue yProperty() {
        return y;
    }

    /**
     * Returns the name of the point
     *
     * @return name of the point
     */
    public final String getName() {
        return name.get();
    }

    /**
     * Sets the name of the point
     *
     * @param name new name for the point
     */
    public final void setName(String name) {
        this.name.set(name);
    }

    /**
     * Returns the name property of the point
     *
     * @return name property of the point
     */
    public final SimpleStringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", scaledX=" + scaledX +
                ", y=" + y +
                ", scaledY=" + scaledY +
                ", name=" + name +
                '}';
    }
}
