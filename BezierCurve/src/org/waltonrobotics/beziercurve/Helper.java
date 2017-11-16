package org.waltonrobotics.beziercurve;

import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Window;
import org.waltonrobotics.beziercurve.controller.BezierTabContentController;
import org.waltonrobotics.beziercurve.point.Point;

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

  public static void setLineLocation(Line line, Point start, Point end) {
    setLineLocation(line, start.getCenter(), end.getCenter());
  }

  public static void initPoint(Point point, BezierTabContentController bezierTabContentController) {
    Tooltip tooltip = new Tooltip();
    tooltip.setText(String.format("X:%-3.3f Y:%-3.3f", point.getCenterX(), point.getCenterY()));

    Window window = point.getScene().getWindow();

    point.centerXProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabContentController.drawBezierCurve();
          tooltip
              .setText(String.format("X:%-3.3f Y:%-3.3f", point.getCenterX(), point.getCenterY()));
        });
    point.centerYProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabContentController.drawBezierCurve();
          tooltip
              .setText(String.format("X:%-3.3f Y:%-3.3f", point.getCenterX(), point.getCenterY()));
        });

    point.setOnMouseEntered(event -> {
      tooltip.show(window,
          event.getX() + window.getX() - tooltip.getWidth() / 2.0,
          event.getY() + window.getY());
    });

    final boolean[] dragging = {false};
    point.setOnMouseExited(
        event -> {
          if (!dragging[0]) {
            tooltip.hide();
          }
        }
    );

    point.setOnMouseReleased(event -> dragging[0] = false);

    point.setOnMouseDragged(event -> {
      dragging[0] = true;

      point.setCenterX(event.getX());
      point.setCenterY(event.getY());

      tooltip.setX(event.getX() + window.getX() - tooltip.getWidth() / 2.0);
      tooltip.setY(event.getY() + window.getY());
    });

  }
}
