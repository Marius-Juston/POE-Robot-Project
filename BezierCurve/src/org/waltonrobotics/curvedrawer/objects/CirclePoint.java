package org.waltonrobotics.curvedrawer.objects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;
import org.waltonrobotics.curvedrawer.curve.Path;
import org.waltonrobotics.curvedrawer.util.Config.PointSetting;
import org.waltonrobotics.curvedrawer.util.Point;

public class CirclePoint extends Circle {

  private SimpleDoubleProperty angleProperty;
  private Set<Path> pathsBelongingTo;

  public CirclePoint(Point point, Path... path) {
    super(point.getX(), point.getY(), PointSetting.RADIUS, PointSetting.COLOR);

    pathsBelongingTo = new HashSet<>();
    Collections.addAll(pathsBelongingTo, path);

    angleProperty = new SimpleDoubleProperty();
    angleProperty.add(point.derivativeProperty());

    point.xProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
    centerXProperty().bind(point.xProperty());
    centerYProperty().bind(point.yProperty());
  }

  public boolean shouldRender() {
    return !pathsBelongingTo.isEmpty();
  }


  public void removePath(Path path) {
    pathsBelongingTo.remove(path);
  }

  public void addPath(Path path) {
    pathsBelongingTo.add(path);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Circle) {
      Circle other = (Circle) obj;

      return other.getCenterX() == getCenterX() && other.getCenterY() == getCenterY();
    } else if (obj instanceof Point) {
      Point other = (Point) obj;

      return other.getX() == getCenterX() && other.getY() == getCenterY()
          && angleProperty.getValue() == other.getDerivative();
    }

    return false;
  }

  @Override
  public String toString() {
    return "CirclePoint{" +
        "angleProperty=" + angleProperty +
        ", pathsBelongingTo=" + pathsBelongingTo +
        "} " + super.toString();
  }

  @Override
  public int hashCode() {
    int result = angleProperty != null ? angleProperty.hashCode() : 0;
    result = 31 * result + (pathsBelongingTo != null ? pathsBelongingTo.hashCode() : 0);
    return result;
  }
}
