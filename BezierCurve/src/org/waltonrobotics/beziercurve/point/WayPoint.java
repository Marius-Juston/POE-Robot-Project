package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.waltonrobotics.beziercurve.Config.WayPointSetting;

public class WayPoint extends Point {

  public WayPoint(double centerX, double centerY
  ) {
    super(centerX, centerY, WayPointSetting.COLOR);
  }

  public WayPoint(double centerX, double centerY, Color color) {
    super(centerX, centerY, color);
  }

  public WayPoint(Point2D point2D, Color color) {
    super(point2D, color);
  }

  public WayPoint(Point2D point2D) {
    super(point2D);
  }
}
