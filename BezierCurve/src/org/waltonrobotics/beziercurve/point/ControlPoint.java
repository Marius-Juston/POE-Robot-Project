package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ControlPoint extends Point {


  public ControlPoint(double centerX, double centerY) {
    super(centerX, centerY);
  }

  public ControlPoint(double centerX, double centerY, Color color) {
    super(centerX, centerY, color);
  }

  public ControlPoint(Point2D point2D, Color color) {
    super(point2D, color);
  }

  public ControlPoint(Point2D point2D) {
    super(point2D);
  }
}
