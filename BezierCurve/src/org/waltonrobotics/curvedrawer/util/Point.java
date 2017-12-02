package org.waltonrobotics.curvedrawer.util;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;

/**
 * Used to define a util in space with an x, y, and a derivative
 *
 * @author Russell Newton, Walton Robotics
 */
public class Point {

  private SimpleDoubleProperty x;
  private SimpleDoubleProperty y;
  private SimpleDoubleProperty derivative;

  /**
   * Used to create a util
   */
  public Point(double x, double y, double derivative) {
    this.x = new SimpleDoubleProperty(x);
    this.y = new SimpleDoubleProperty(y);
    this.derivative = new SimpleDoubleProperty(derivative);
  }

  /**
   * Can be used to create a util without specifying a derivative
   */
  public Point(double x, double y) {
    this(x, y, 0);
  }

  public double getX() {
    return x.get();
  }

  public void setX(double x) {
    this.x.set(x);
  }

  public ObservableDoubleValue xProperty() {
    return x;
  }

  public double getY() {
    return y.get();
  }

  public void setY(double y) {
    this.y.set(y);
  }

  public ObservableDoubleValue yProperty() {
    return y;
  }

  public double getDerivative() {
    return derivative.get();
  }

  public void setDerivative(double derivative) {
    this.derivative.set(derivative);
  }

  public ObservableDoubleValue derivativeProperty() {
    return derivative;
  }

  /**
   * Offsets a util along a perpendicular line from a tangent line
   *
   * @param dtAtPoint - the derivative of the util
   * @param distance - the distance to offset the util by
   * @return the offset util
   */
  public Point offsetPerpendicular(double dtAtPoint, double distance) {
    double angleOfDT = Math.atan(dtAtPoint);
    double offsetX =
        distance * Math.cos(angleOfDT + Math.PI / 2); // Finds util at distance along perpendicular
    // line
    double offsetY = distance * Math.sin(angleOfDT + Math.PI / 2);

    return new Point(this.getX() + offsetX, this.getY() + offsetY, angleOfDT);
  }

}
