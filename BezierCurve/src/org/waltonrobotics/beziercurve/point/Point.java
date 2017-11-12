package org.waltonrobotics.beziercurve.point;

import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.waltonrobotics.beziercurve.Config;
import org.waltonrobotics.beziercurve.Config.ControlPointSetting;
import org.waltonrobotics.beziercurve.controller.BezierTabContentController;

public abstract class Point extends Circle {

  private String name;

  public Point(double centerX, double centerY, BezierTabContentController bezierTabController,
      Color color) {
    super(centerX, centerY, Config.ControlPointSetting.RADIUS, color);

    name = "Point hello";

    Tooltip tooltip = new Tooltip();
    tooltip.setText(String.format("X:%-3.3f Y:%-3.3f", getCenterX(), getCenterY()));

    centerXProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabController.drawBezierCurve();
          tooltip.setText(String.format("X:%-3.3f Y:%-3.3f", getCenterX(), getCenterY()));
        });
    centerYProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabController.drawBezierCurve();
          tooltip.setText(String.format("X:%-3.3f Y:%-3.3f", getCenterX(), getCenterY()));
        });

    setOnMouseEntered(event ->
        tooltip.show(this.getScene().getWindow(),
            event.getX() + getScene().getWindow().getX() - tooltip.getWidth() / 2.0,
            event.getY() + getScene().getWindow().getY()));

    final boolean[] dragging = {false};
    setOnMouseExited(
        event -> {
          if (!dragging[0]) {
            tooltip.hide();
          }
        }
    );

    setOnMouseReleased(event -> dragging[0] = false);

    setOnMouseDragged(event -> {
      dragging[0] = true;

      setCenterX(event.getX());
      setCenterY(event.getY());

      tooltip.setX(event.getX() + getScene().getWindow().getX() - tooltip.getWidth() / 2.0);
      tooltip.setY(event.getY() + getScene().getWindow().getY());
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
