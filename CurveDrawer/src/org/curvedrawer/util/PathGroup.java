package org.curvedrawer.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import org.curvedrawer.Main;
import org.curvedrawer.misc.CirclePoint;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathGroup extends Group {

    private final Map<Pose, CirclePoint> circlePoseHashMap; // Map of poses to their assigned CirclePoint instance
    private final Map<Point, CirclePoint> circlePointHashMap; // Map of points to their assigned CirclePoint instance
    private final TitledPane titlePane; //TitlePane where the points are listed in a table
    private final SimpleBooleanProperty hasPointSelected = new SimpleBooleanProperty(false); // boolean property that turn true when a CirclePoint is supposedly selected.
    private final DoubleExpression X_SCALE; // x scale property for all circlePoints
    private final DoubleExpression Y_SCALE; // y scale property for all circlePoints


    /**
     * Initialized instance variables and sets up the PathTable
     *
     * @param pathName    the name of the path
     * @param path        the path to observe
     * @param drawingPane node to display the circlePoints to
     */
    public PathGroup(String pathName, Path path, Node drawingPane) {
        circlePointHashMap = new HashMap<>(10);
        circlePoseHashMap = new HashMap<>(Main.NUMBER_OF_STEPS);


        PathTable pathTable = new PathTable(path.getPoints()); // creates the PathTable for the path
        pathTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Point>) event -> {
            if (event.next()) {
                if (event.wasAdded()) {
                    for (Point point : event.getAddedSubList())
                        circlePointHashMap.get(point).selected(true);
                }
                if (event.wasRemoved()) {

                    for (Point point : event.getRemoved())
                        circlePointHashMap.get(point).selected(false);
                }
            }
        }); // notifies this class if a point is selected on the PathTable

        titlePane = new TitledPane(pathName, pathTable);

        X_SCALE = DoubleExpression.doubleExpression(Bindings.divide(1, drawingPane.scaleXProperty())); // creates binding for x scale
        Y_SCALE = DoubleExpression.doubleExpression(Bindings.divide(1, drawingPane.scaleYProperty())); // creates binding for y scale
    }


    /**
     * Adds points to the drawing pane and HashMap and binds the scale properties of the CirclePoint
     *
     * @param points points to convert and add to the drawing pane
     */
    public final void addPoints(Point... points) {
        for (Point point : points) {
            CirclePoint circlePoint = new CirclePoint(point, this);
            circlePointHashMap.put(point, circlePoint);

            circlePoint.scaleXProperty().bind(X_SCALE);
            circlePoint.scaleYProperty().bind(Y_SCALE);

            getChildren().add(0, circlePoint);
            circlePoint.toFront();
        }
    }


    /**
     * Adds poses to the drawing pane and HashMap and binds the scale properties of the CirclePoint
     *
     * @param poses points to convert and add to the drawing pane
     */
    public final void addPoses(Pose... poses) {
        for (Pose pose : poses) {
            CirclePoint circlePoint = new CirclePoint(pose, this);
            circlePoseHashMap.put(pose, circlePoint);

            circlePoint.scaleXProperty().bind(X_SCALE);
            circlePoint.scaleYProperty().bind(Y_SCALE);

            getChildren().add(Math.max(0, getChildren().size() - 1), circlePoint);
            circlePoint.toFront();
        }
    }

    /**
     * Returns the associated CirclePoint given its point
     *
     * @param point point to find the associated CirclePoint
     * @return the associated CirclePoint given the point. If none exist null.
     */
    public final CirclePoint getPoint(Point point) {
        return circlePointHashMap.get(point);
    }


    /**
     * Returns the associated CirclePoint given its pose
     *
     * @param pose pose to find the associated CirclePoint
     * @return the associated CirclePoint given the pose. If none exist null.
     */
    public final CirclePoint getPose(Pose pose) {
        return circlePoseHashMap.get(pose);
    }

    /**
     * Removes the poses from the group and from the poses HashMap
     *
     * @param poses poses to be removed
     */
    public final void removePoses(Pose... poses) {
        for (Pose pose : poses) {
            getChildren().remove(circlePoseHashMap.remove(pose));
        }
    }


    /**
     * Removes the points from the group and from the points HashMap
     *
     * @param points points to be removed
     */
    public final void removePoints(Point... points) {
        for (Point point : points) {
            getChildren().remove(circlePointHashMap.remove(point));
        }
    }

    /**
     * Removes the points from the group and from the points HashMap
     *
     * @param points points to be removed
     */
    public final void removePoints(List<? extends Point> points) {
        removePoints(points.toArray(new Point[points.size()]));
    }

    /**
     * Adds points to the drawing pane and HashMap and binds the scale properties of the CirclePoint
     *
     * @param points points to convert and add to the drawing pane
     */
    public final void addPoints(List<? extends Point> points) {
        addPoints(points.toArray(new Point[points.size()]));
    }

    /**
     * Returns the TitlePane that holds the PathTable
     *
     * @return returns the TitlePane
     */
    public final TitledPane getTitlePane() {
        return titlePane;
    }

    /**
     * Returns true if there is a point selected, false otherwise.
     *
     * @return returns if a point is selected
     */
    public final boolean isHasPointSelected() {
        return hasPointSelected.get();
    }

    /**
     * Sets if a point is being selected or not
     *
     * @param hasPointSelected if point is being selected or not
     */
    public final void setHasPointSelected(boolean hasPointSelected) {
        this.hasPointSelected.set(hasPointSelected);
    }

    /**
     * Returns point selected property
     *
     * @return returns if point is being selected property
     */
    public final SimpleBooleanProperty hasPointSelectedProperty() {
        return hasPointSelected;
    }

    @Override
    public String toString() {
        return "PathGroup{" +
                "circlePoseHashMap=" + circlePoseHashMap +
                ", circlePointHashMap=" + circlePointHashMap +
                ", titlePane=" + titlePane +
                ", hasPointSelected=" + hasPointSelected +
                ", X_SCALE=" + X_SCALE +
                ", Y_SCALE=" + Y_SCALE +
                "} " + super.toString();
    }
}
