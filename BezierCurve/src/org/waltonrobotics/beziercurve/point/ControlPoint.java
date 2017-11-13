package org.waltonrobotics.beziercurve.point;

import org.waltonrobotics.beziercurve.controller.BezierTabContentController;

public class ControlPoint extends Point {


  public ControlPoint(double centerX, double centerY,
      BezierTabContentController bezierTabController) {
    super(centerX, centerY, bezierTabController);
  }
}
