package org.curvedrawer.util;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.curvedrawer.Main;

public class Point {
    private final SimpleDoubleProperty x;
    private final SimpleDoubleProperty scaledX;

    private final SimpleDoubleProperty y;
    private final SimpleDoubleProperty scaledY;

    private final SimpleStringProperty name;

    public Point(double x, double y, String name) {
        this.x = new SimpleDoubleProperty(x);

        scaledX = new SimpleDoubleProperty();
        scaledX.bind(DoubleExpression.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(DoubleExpression.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.name = new SimpleStringProperty(name);
    }

    public Point(double x, double y) {
        this(x, y, null);
    }

    public Point(Point2D point2D) {
        this(point2D.getX(), point2D.getY());
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

    public double getX() {
        return x.get();
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public ObservableValue xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public ObservableValue yProperty() {
        return y;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    @Override
    public final String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", name=" + name +
                '}';
    }
}
