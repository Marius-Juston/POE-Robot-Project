package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.waltonrobotics.beziercurve.Config;
import org.waltonrobotics.beziercurve.Config.ControlPointSetting;
import org.waltonrobotics.beziercurve.controller.BezierTabContentController;

public abstract class Point extends Circle {

  public Point(double centerX, double centerY, BezierTabContentController bezierTabController,
      Color color) {

    super(centerX, centerY, Config.ControlPointSetting.RADIUS, color);

    select();

    Tooltip tooltip = new Tooltip();
//    Tooltip.install(this, tooltip);
//    tooltip.setAutoHide(true);
    tooltip.setText(String.format("X:%-3.0f Y:%-3.0f", getCenterX(), getCenterY()));
    setOnMouseEntered(event ->
    {
      tooltip.show(this.getScene().getWindow(),
          event.getX() + getScene().getWindow().getX() - tooltip.getWidth() / 2.0,
          event.getY() + getScene().getWindow().getY());
    });

    final boolean[] dragging = {false};
    setOnMouseExited(
        event -> {
          if (!dragging[0]) {
            tooltip.hide();
          }
        }
    );

    setOnMouseReleased(event -> {
      dragging[0] = false;
    });

    setOnMouseDragged(event -> {
      dragging[0] = true;

      setCenterX(event.getX());
      setCenterY(event.getY());

      tooltip.setX(event.getX() + getScene().getWindow().getX() - tooltip.getWidth() / 2.0);
      tooltip.setY(event.getY() + getScene().getWindow().getY());

      tooltip.setText(String.format("X:%-3.0f Y:%-3.0f", getCenterX(), getCenterY()));
      bezierTabController.drawBezierCurve();
    });

//    hoverProperty().add;
  }

  public Point(double centerX, double centerY, BezierTabContentController bezierTabController) {

    this(centerX, centerY, bezierTabController, ControlPointSetting.COLOR);
  }


  public Point(Point2D point2D, BezierTabContentController bezierTabController, Color color) {
    this(point2D.getX(), point2D.getY(), bezierTabController, color);
  }

  public Point(Point2D point2D, BezierTabContentController bezierTabController) {
    this(point2D, bezierTabController, ControlPointSetting.COLOR);
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

  }

  public void unselect() {
    setStyle(null);
  }
}
