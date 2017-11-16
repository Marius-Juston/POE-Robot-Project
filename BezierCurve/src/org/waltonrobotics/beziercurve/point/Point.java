package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.waltonrobotics.beziercurve.Config;
import org.waltonrobotics.beziercurve.Config.ControlPointSetting;

public abstract class Point extends Circle {

  private String name;

  public Point(double centerX, double centerY,
      Color color) {
    super(centerX, centerY, Config.ControlPointSetting.RADIUS, color);
    setName("Point");
  }

  public Point(double centerX, double centerY) {

    this(centerX, centerY, ControlPointSetting.COLOR);
  }

  public Point(Point2D point2D, Color color) {
    this(point2D.getX(), point2D.getY(), color);
  }

  public Point(Point2D point2D) {
    this(point2D, ControlPointSetting.COLOR);
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Point) {
      Point point = (Point) obj;

      return getLayoutBounds().contains(point.getLayoutBounds());
    }
    return false;
  }

  public Point2D getCenter() {
    return new Point2D(getCenterX(), getCenterY());
  }

  public void select() {
    setStyle(null);
  }

  public void unselect() {
    setStyle(null);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
