package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ArrowPoint extends Point {

  public ArrowPoint(double centerX, double centerY) {
    super(centerX, centerY);
  }

  public ArrowPoint(double centerX, double centerY, Color color) {
    super(centerX, centerY, color);
  }

  public ArrowPoint(Point2D point2D, Color color) {
    super(point2D, color);
  }

  public ArrowPoint(Point2D point2D) {
    super(point2D);
  }
}
