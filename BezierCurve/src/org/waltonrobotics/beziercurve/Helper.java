package org.waltonrobotics.beziercurve;

import com.sun.javafx.util.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.waltonrobotics.beziercurve.controller.BezierTabContentController;
import org.waltonrobotics.beziercurve.curve.Curve;
import org.waltonrobotics.beziercurve.point.Point;

public class Helper {

  private static final String pointCoordinateToStringFormat = "X:%-3.3f Y:%-3.3f";

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
    setTooltipText(tooltip, point);

    point.centerXProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabContentController.drawCurves();
          setTooltipText(tooltip, point);
//          showPopup(tooltip, point, Side.TOP, 0, 10);
        });
    point.centerYProperty().addListener(
        (observable, oldValue, newValue) -> {
          bezierTabContentController.drawCurves();
          setTooltipText(tooltip, point);
//          showPopup(tooltip, point, Side.TOP, 0, 10);
        });

//    point.setOnMouseClicked(event -> {
//      if (event.getButton() == MouseButton.SECONDARY)
//      {
//        bezierTabContentController.TABLE_CONTEXT_MENU.show(point, Side.TOP, 0,0);
//        bezierTabContentController.setRemovePointOnAction(event1 -> {bezierTabContentController.getCurves().forEach(curve -> {
//          if (curve.getPoints().contains(point))
//            curve.getPoints().remove(point);
//        });
//
//        bezierTabContentController.drawCurves();
//        });
//      }
//    });

    point.setOnMouseEntered(event -> {
//      showPopup(tooltip, point, Side.TOP, 0, 10);
    });

    final boolean[] dragging = {false};
    point.setOnMouseExited(
        event -> {
          if (!dragging[0]) {
//            tooltip.hide();
          }
        }
    );

    point.setOnMouseReleased(event -> dragging[0] = false);

    point.setOnMouseDragged(event -> {
      dragging[0] = true;

      point.setCenterX(event.getX());
      point.setCenterY(event.getY());

//      showPopup(tooltip, point, Side.TOP, 0, 10);
    });
  }

  private static void setTooltipText(Tooltip tooltip, Point point) {
    tooltip
        .setText(
            String.format(pointCoordinateToStringFormat, point.getCenterX(), point.getCenterY()));
  }


  public static ContextMenu createTemplateContextMenu() {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem menuItem = new MenuItem("Settings");

    contextMenu.getItems().add(menuItem);

    return contextMenu;
  }

  // Coppied from ContextMenu show method
  private static void showPopup(PopupControl tooltip, Node anchor, Side side, double dx,
      double dy) {
    if (anchor == null) {
      return;
    }

    tooltip.getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
    // to the old HPos/VPos API here, as Utils can not refer to Side in the
    // charting API.
    HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
    VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;

    // translate from anchor/hpos/vpos/dx/dy into screenX/screenY
    Point2D point = Utils.pointRelativeTo(anchor,
        tooltip.prefWidth(-1), tooltip.prefHeight(-1),
        hpos, vpos, dx, dy, true);
    tooltip.show(anchor, point.getX(), point.getY());
  }

  public static String toString(Curve curve, boolean point2D) {
    List<Point> pointList = curve.getPoints();

    StringJoiner stringJoiner = new StringJoiner(" ");

    stringJoiner.add(String.valueOf(point2D));

    if (!point2D) {
      for (Point point : pointList) {
        stringJoiner.add(String.valueOf(point.getCenterX()));
        stringJoiner.add(String.valueOf(point.getCenterY()));
        stringJoiner.add(point.getClass().getName());
      }
    } else {
      for (Point point : pointList) {
        stringJoiner.add(String.valueOf(point.getCenterX()));
        stringJoiner.add(String.valueOf(point.getCenterY()));
      }
    }

    return stringJoiner.toString();
  }

  public static <T> ObservableList<T> extractPoints(String pointsString) {
    String[] coordinates = pointsString.split(" ");

    List<T> points = new ArrayList<>();

    if (coordinates.length > 1) {

      boolean isPoint2DList = Boolean.parseBoolean(coordinates[0]);

      if (isPoint2DList) {
        for (int i = 1; i < coordinates.length; i += 2) {
          double x = Double.parseDouble(coordinates[i]);
          double y = Double.parseDouble(coordinates[i + 1]);

          points.add((T) new Point2D(x, y));
        }
      } else {
        for (int i = 1; i < coordinates.length; i += 3) {
          double x = Double.parseDouble(coordinates[i]);
          double y = Double.parseDouble(coordinates[i + 1]);
          String pointInstance = coordinates[i + 2];

          points.add((T) createPointInstance(x, y, pointInstance));
        }
      }
    }

    return FXCollections.observableArrayList(points);
  }

  private static final Point createPointInstance(double x, double y, String className) {
    Point point = null;
    try {
      Class[] constructorArguments = {double.class, double.class};

      Class pointClass = Class.forName(className);
      Constructor constructor = pointClass.getDeclaredConstructor(constructorArguments);

      point = (Point) constructor.newInstance(x, y);
    } catch (InstantiationException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return point;
  }
}