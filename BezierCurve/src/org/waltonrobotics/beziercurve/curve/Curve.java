package org.waltonrobotics.beziercurve.curve;

import java.util.List;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.waltonrobotics.beziercurve.point.Point;

public abstract class Curve {

  ObservableList<Point> points;

  public Curve() {
    this(new SimpleListProperty<>());
  }

  public Curve(ObservableList<Point> point2DS) {
    points = point2DS;
  }

  public void addListChangeListener(
      ListChangeListener<Point> listChangeListener) {

    points.addListener(listChangeListener);
  }

  public void addPoint(Point point2D) {
    points.add(point2D);
  }

  public void addAllPoints(List<Point> point2D) {
    points.addAll(point2D);
  }

  public void removePoint(Point point2D) {
    points.remove(point2D);
  }

  public void removeAllPoints(List<Point> point2D) {
    points.removeAll(point2D);
  }

  public void setPoints(List<Point> point2D) {
    points.setAll(point2D);
  }
}
