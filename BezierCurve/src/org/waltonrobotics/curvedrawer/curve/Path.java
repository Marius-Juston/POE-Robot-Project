package org.waltonrobotics.curvedrawer.curve;

import javafx.beans.property.SimpleStringProperty;
import org.waltonrobotics.curvedrawer.util.Point;

/**
 * Defines the methods used by a motion path
 *
 * @author Russell Newton, Walton Robotics
 */
public abstract class Path {

  private final int numberOfSteps;
  private final double robotLength;
  private SimpleStringProperty name;

  public Path(int numberOfSteps, double robotLength, String name) {
    this.numberOfSteps = numberOfSteps;
    this.robotLength = robotLength;
    this.name = new SimpleStringProperty(name);

  }

  public int getNumberOfSteps() {
    return numberOfSteps;
  }

  public double getRobotLength() {
    return robotLength;
  }

  /**
   * Finds the points that define the path the robot follows
   *
   * @return an array of points that holds the points along the path
   */
  public abstract Point[] getPathPoints();

  abstract void setPathPoints(Point... pathPoints);

  /**
   * Finds the points that define the path the left side of the robot follows
   *
   * @return an array of points that holds the points along the path
   */
  public abstract Point[] getLeftPath();

  /**
   * Finds the points that define the path the right side of the robot follows
   *
   * @return an array of points that holds the points along the path
   */
  public abstract Point[] getRightPath();

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public SimpleStringProperty nameProperty() {
    return name;
  }

  @Override
  public String toString() {
    return "Path{" +
        "name=" + name +
        '}';
  }
}
