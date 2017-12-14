package org.curvedrawer.util;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Point {
    private final SimpleDoubleProperty x;
    private final SimpleDoubleProperty y;
    private final SimpleStringProperty name;

    public Point(double x, double y, String name) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.name = new SimpleStringProperty(name);
    }

    public Point(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        name = new SimpleStringProperty(null);
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
