package org.curvedrawer.util;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
        scaledX.bind(ReadOnlyDoubleWrapper.doubleExpression(this.x.divide(Main.SCALE_FACTOR)));

        this.y = new SimpleDoubleProperty(y);
        scaledY = new SimpleDoubleProperty();
        scaledY.bind(ReadOnlyDoubleWrapper.doubleExpression(this.y.divide(Main.SCALE_FACTOR)));

        this.name = new SimpleStringProperty(name);
    }

    public Point(double x, double y) {
        this(x, y, null);
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

    public final double getX() {
        return x.get();
    }

    public final void setX(double x) {
        this.x.set(x);
    }

    public final SimpleDoubleProperty xProperty() {
        return x;
    }

    public final double getY() {
        return y.get();
    }

    public final void setY(double y) {
        this.y.set(y);
    }

    public final SimpleDoubleProperty yProperty() {
        return y;
    }

    public final String getName() {
        return name.get();
    }

    public final void setName(String name) {
        this.name.set(name);
    }

    public final SimpleStringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", name=" + name +
                '}';
    }
}
