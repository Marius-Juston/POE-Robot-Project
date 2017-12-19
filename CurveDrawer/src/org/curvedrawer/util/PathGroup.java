package org.curvedrawer.util;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import org.curvedrawer.misc.CirclePoint;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;

import java.util.HashMap;
import java.util.List;

public class PathGroup extends Group {
    private final Node drawingPane;
    private HashMap<Pose, CirclePoint> circlePoseHashMap;
    private HashMap<Point, CirclePoint> circlePointHashMap;
    private TitledPane titlePane;

    private SimpleBooleanProperty hasPointSelected = new SimpleBooleanProperty(false);

    public PathGroup(String pathName, Path path, Node drawingPane) {
        this.drawingPane = drawingPane;
        this.circlePointHashMap = new HashMap<>();
        this.circlePoseHashMap = new HashMap<>();

        PathTable pathTable = new PathTable(path);
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
        });

        titlePane = new TitledPane(pathName, pathTable);

    }

    public void addPoints(Point... points) {
        for (Point point : points) {
            CirclePoint circlePoint = new CirclePoint(point, this);
            circlePointHashMap.put(point, circlePoint);
            circlePoint.setScaleX(1 / drawingPane.getScaleX());
            circlePoint.setScaleY(1 / drawingPane.getScaleY());

            drawingPane.scaleXProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleX(1 / newValue.doubleValue()));
            drawingPane.scaleYProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleY(1 / newValue.doubleValue()));

            getChildren().add(0, circlePoint);
            circlePoint.toFront();
        }
    }

    public void addPoses(Pose... poses) {
        for (Pose pose : poses) {
            CirclePoint circlePoint = new CirclePoint(pose, this);
            circlePoseHashMap.put(pose, circlePoint);

            circlePoint.setScaleX(1 / drawingPane.getScaleX());
            circlePoint.setScaleY(1 / drawingPane.getScaleY());

            drawingPane.scaleXProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleX(1 / newValue.doubleValue()));
            drawingPane.scaleYProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleY(1 / newValue.doubleValue()));


            getChildren().add(Math.max(0, getChildren().size() - 1), circlePoint);
            circlePoint.toFront();
        }
    }

    public CirclePoint getPoint(Point point) {
        return circlePointHashMap.get(point);
    }


    public CirclePoint getPose(Pose pose) {
        return circlePoseHashMap.get(pose);
    }

    public void removePoses(Pose... poses) {
        for (Pose pose : poses) {
            getChildren().remove(circlePoseHashMap.remove(pose));
        }
    }

    public void removePoints(Point... points) {
        for (Point point : points) {
            getChildren().remove(circlePointHashMap.remove(point));
        }
    }

    public void removePoints(List<? extends Point> removed) {
        removePoints(removed.toArray(new Point[removed.size()]));
    }

    public void addPoints(List<? extends Point> addedSubList) {
        addPoints(addedSubList.toArray(new Point[addedSubList.size()]));
    }

    public TitledPane getTitlePane() {
        return titlePane;
    }

    public boolean isHasPointSelected() {
        return hasPointSelected.get();
    }

    public void setHasPointSelected(boolean hasPointSelected) {
        this.hasPointSelected.set(hasPointSelected);
    }

    public SimpleBooleanProperty hasPointSelectedProperty() {
        return hasPointSelected;
    }
}
