package org.waltonrobotics.beziercurve.point;

import org.waltonrobotics.beziercurve.Config.WayPointSetting;
import org.waltonrobotics.beziercurve.controller.BezierTabContentController;

public class WayPoint extends Point {

  public WayPoint(double centerX, double centerY,
      BezierTabContentController bezierTabController) {
    super(centerX, centerY, bezierTabController, WayPointSetting.COLOR);
  }

  public ControlPoint createControlPoint() {
    return null;
  }
}
