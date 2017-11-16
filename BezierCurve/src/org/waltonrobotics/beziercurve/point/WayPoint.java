package org.waltonrobotics.beziercurve.point;

import org.waltonrobotics.beziercurve.Config.WayPointSetting;

public class WayPoint extends Point {

  public WayPoint(double centerX, double centerY
  ) {
    super(centerX, centerY, WayPointSetting.COLOR);
  }

  public ControlPoint createControlPoint() {
    return null;
  }
}
