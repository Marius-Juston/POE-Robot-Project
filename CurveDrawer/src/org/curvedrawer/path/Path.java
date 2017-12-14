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
    private final SimpleListProperty<Point> points;
    private final SimpleIntegerProperty numberOfSteps;


    Path(int numberOfSteps, Point... points) {
        this.points = new SimpleListProperty<>(FXCollections.observableArrayList(p -> new Observable[]{p.xProperty(), p.yProperty()}));
        this.points.setAll(points);

        this.numberOfSteps = new SimpleIntegerProperty(numberOfSteps);
    }

    private static Pose[] offsetPosesPerpendicularly(Pose[] poses, double offsetDistance) {
        Pose[] offsetPoses = new Pose[poses.length];

        for (int i = 0; i < offsetPoses.length; i++) {
            offsetPoses[i] = poses[i].offsetPerpendicular(offsetDistance);
        }

        return offsetPoses;
    }

    public final int getNumberOfSteps() {
        return numberOfSteps.get();
    }

    public final void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps.set(numberOfSteps);
    }

    public final SimpleIntegerProperty numberOfStepsProperty() {
        return numberOfSteps;
    }

    public final ObservableList<Point> getPoints() {
        return points.get();
    }

    public final void setPoints(ObservableList<Point> points) {
        this.points.set(points);
    }

    public final List<Point> pointsProperty() {
        return Collections.unmodifiableList(points);
    }

    public final void addPoints(Point... points) {
        getPoints().addAll(points);
    }

    /**
     * Finds the points that define the path the robot follows
     *
     * @return an array of points that holds the points along the path
     */
    public abstract Pose[] createPathPoses();

    @Override
    public final String toString() {
        return "Path{" +
                "points=" + points +
                ", numberOfSteps=" + numberOfSteps +
                '}';
    }
}
