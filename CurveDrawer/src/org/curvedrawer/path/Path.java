package org.curvedrawer.path;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.util.Collections;
import java.util.List;

public abstract class Path {
    private final SimpleListProperty<Point> points; // observable list containing points that define the path
    private final SimpleIntegerProperty numberOfSteps; // resolution of the path (how many poses to be created when making the path)

    /**
     * Initializes instance variables
     *
     * @param numberOfSteps resolution of the path (how many poses to be created when creating the path)
     * @param points        defining points that define the path
     */
    Path(int numberOfSteps, Point... points) {
        this.points = new SimpleListProperty<>(FXCollections.observableArrayList(p -> new Observable[]{p.xProperty(), p.yProperty()}));
        this.points.setAll(points);

        this.numberOfSteps = new SimpleIntegerProperty(numberOfSteps);
    }

    /**
     * Method to be used to offset a poses array a certain distance
     *
     * @param poses          posses to be offset
     * @param offsetDistance distance to be offset by
     * @return an array of offset posses
     */
    public static Pose[] offsetPosesPerpendicularly(Pose[] poses, double offsetDistance) {
        Pose[] offsetPoses = new Pose[poses.length];

        for (int i = offsetPoses.length - 1; i >= 0; i--) {
            offsetPoses[i] = poses[i].offsetPerpendicular(offsetDistance);
        }

        return offsetPoses;
    }

    /**
     * Returns he number of step for the path
     *
     * @return the number of steps the path will take when creating the path
     */
    public final int getNumberOfSteps() {
        return numberOfSteps.get();
    }

    /**
     * Sets the "resolution" of the path by setting the number of steps to take when looping
     *
     * @param numberOfSteps new number of steps to take when looping
     */
    public final void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps.set(numberOfSteps);
    }

    /**
     * Returns the number of steps property
     *
     * @return number of steps property
     */
    public final SimpleIntegerProperty numberOfStepsProperty() {
        return numberOfSteps;
    }

    /**
     * Returns the defining points of the path
     *
     * @return an observable list of the defining points of the path
     */
    public final ObservableList<Point> getPoints() {
        return points.get();
    }

    /**
     * Sets the defining points of the path
     *
     * @param points points observable list to set the defining points to be
     */
    public final void setPoints(ObservableList<Point> points) {
        this.points.set(points);
    }

    /**
     * Returns the points property
     *
     * @return points property
     */
    public final List<Point> pointsProperty() {
        return Collections.unmodifiableList(points);
    }

    /**
     * Adds points to the list of defining points
     *
     * @param points points to add
     */
    public final void addPoints(Point... points) {
        getPoints().addAll(points);
    }

    /**
     * Remove points that define the path
     *
     * @param points points to remove from the list
     */
    private void removePoints(Point... points) {
        getPoints().removeAll(points);
    }


    /**
     * Remove points that define the path
     *
     * @param points points to remove from the list
     */
    public final void removePoints(List<Point> points) {
        removePoints(points.toArray(new Point[points.size()]));
    }

    /**
     * Finds the points that define the path the robot follows
     *
     * @return an array of points that holds the points along the path
     */
    public abstract Pose[] createPathPoses();

    @Override
    public String toString() {
        return "Path{" +
                "points=" + points +
                ", numberOfSteps=" + numberOfSteps +
                '}';
    }
}
