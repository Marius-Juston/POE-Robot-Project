package org.waltonrobotics.curvedrawer.curve;

import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.waltonrobotics.curvedrawer.util.Point;

/**
 * Defines the methods used by a motion path
 *
 * @author Russell Newton, Walton Robotics
 */
public abstract class Path {

  private final int numberOfSteps;
  private final double robotLength;
  private final SimpleListProperty<Point> pathPoints;
  private final SimpleListProperty<Point> leftPoints;
  private final SimpleListProperty<Point> rightPoints;
  private SimpleStringProperty name;
  private SimpleListProperty<Point> creationPoints;
  private Color color;


  public Path(int numberOfSteps, double robotLength, String name, Color color,
      Point... creationPoints) {
    this.numberOfSteps = numberOfSteps;
    this.robotLength = robotLength;
    this.name = new SimpleStringProperty(name);
    this.color = color;

    this.creationPoints = new SimpleListProperty<>(FXCollections.observableArrayList(
        p -> new Observable[]{p.xProperty(), p.yProperty(), p.derivativeProperty()}));

    setCreationPoints(creationPoints);

    this.creationPoints.addListener((observable, oldValue, newValue) -> update());

    pathPoints = new SimpleListProperty<Point>();
    leftPoints = new SimpleListProperty<Point>();
    rightPoints = new SimpleListProperty<>();

    createPathPoints(creationPoints);
    update();
  }

  public SimpleListProperty<Point> pathPointsProperty() {
    return pathPoints;
  }

  public void setPathPoints(ObservableList<Point> pathPoints) {
    this.pathPoints.set(pathPoints);
  }

  public void setPathPoints(Point... pathPoints) {
    this.pathPoints.set(FXCollections.observableArrayList(pathPoints));
  }

  public void setLeftPoints(Point... pathPoints) {
    this.pathPoints.set(FXCollections.observableArrayList(pathPoints));
  }

  public void setRightPoints(Point... pathPoints) {
    this.pathPoints.set(FXCollections.observableArrayList(pathPoints));
  }

  public ObservableList<Point> getLeftPoints() {
    return leftPoints.get();
  }

  public void setLeftPoints(ObservableList<Point> leftPoints) {
    this.leftPoints.set(leftPoints);
  }

  public SimpleListProperty<Point> leftPointsProperty() {
    return leftPoints;
  }

  public ObservableList<Point> getRightPoints() {
    return rightPoints.get();
  }

  public void setRightPoints(ObservableList<Point> rightPoints) {
    this.rightPoints.set(rightPoints);
  }

  public SimpleListProperty<Point> rightPointsProperty() {
    return rightPoints;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public ObservableList<Point> getCreationPoints() {
    return creationPoints.get();
  }

  public void setCreationPoints(Point... creationPoints) {
    this.creationPoints.setAll(creationPoints);
  }

  public void setCreationPoints(ObservableList<Point> creationPoints) {
    this.creationPoints.set(creationPoints);
  }

  public SimpleListProperty<Point> creationPointsProperty() {
    return creationPoints;
  }

  public int getNumberOfSteps() {
    return numberOfSteps;
  }

  public double getRobotLength() {
    return robotLength;
  }

  public ObservableList<Point> getPathPoints() {
    return pathPoints.get();
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public SimpleStringProperty nameProperty() {
    return name;
  }

  public abstract void update();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Path path = (Path) o;

    return numberOfSteps == path.numberOfSteps && Double.compare(path.robotLength, robotLength) == 0
        && (name != null ? name.equals(path.name) : path.name == null) && (creationPoints != null
        ? creationPoints.equals(path.creationPoints) : path.creationPoints == null) && (
        color != null ? color.equals(path.color) : path.color == null);

  }

  public abstract void createPathPoints(Point... creationPoints);

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = numberOfSteps;
    temp = Double.doubleToLongBits(robotLength);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (creationPoints != null ? creationPoints.hashCode() : 0);
    result = 31 * result + (color != null ? color.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Path{" +
        "name=" + name +
        '}';
  }
}
