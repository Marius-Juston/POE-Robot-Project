package org.waltonrobotics.beziercurve;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Helper {

  public static Line createLine() {
    return createLine(Config.InBetweenLineSetting.STROKE_COLOR);
  }

  private static Line createLine(Color color) {
    Line line = new Line();
    line.setStroke(color);
    line.setStrokeWidth(Config.InBetweenLineSetting.STROK_WIDTH);

    return line;
  }

  public static void setLineLocation(Line line, Point2D start, Point2D end) {
    line.setStartX(start.getX());
    line.setStartY(start.getY());
    line.setEndX(end.getX());
    line.setEndY(end.getY());
  }
}
