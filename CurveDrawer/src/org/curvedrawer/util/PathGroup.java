package org.curvedrawer.util;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private final Node drawingPane;
    private final Map<Pose, CirclePoint> circlePoseHashMap;
    private final Map<Point, CirclePoint> circlePointHashMap;
    private final TitledPane titlePane;

    private final SimpleBooleanProperty hasPointSelected = new SimpleBooleanProperty(false);

    private final DoubleExpression X_SCALE;
    private final DoubleExpression Y_SCALE;

    private static final SimpleDoubleProperty ONE = new SimpleDoubleProperty(1.0);

    public PathGroup(String pathName, Path path, Node drawingPane) {
        this.drawingPane = drawingPane;
        circlePointHashMap = new HashMap<>(10);
        circlePoseHashMap = new HashMap<>(Main.NUMBER_OF_STEPS);

        PathTable pathTable = new PathTable(path.getPoints());
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

        X_SCALE = DoubleExpression.doubleExpression(ONE.divide(drawingPane.scaleXProperty()));
        Y_SCALE = DoubleExpression.doubleExpression(ONE.divide(drawingPane.scaleYProperty()));
    }

    public final void addPoints(Point... points) {
        for (Point point : points) {
            CirclePoint circlePoint = new CirclePoint(point, this);
            circlePointHashMap.put(point, circlePoint);

            circlePoint.scaleXProperty().bind(X_SCALE);
            circlePoint.scaleYProperty().bind(Y_SCALE);

//            circlePoint.setScaleX(1.0 / drawingPane.getScaleX());
//            circlePoint.setScaleY(1.0 / drawingPane.getScaleY());

//            drawingPane.scaleXProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleX(1.0 / newValue.doubleValue()));
//            drawingPane.scaleYProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleY(1.0 / newValue.doubleValue()));

            getChildren().add(0, circlePoint);
            circlePoint.toFront();
        }
    }

    public final void addPoses(Pose... poses) {
        for (Pose pose : poses) {
            CirclePoint circlePoint = new CirclePoint(pose, this);
            circlePoseHashMap.put(pose, circlePoint);

            circlePoint.scaleXProperty().bind(X_SCALE);
            circlePoint.scaleYProperty().bind(Y_SCALE);

//            circlePoint.setScaleX(1.0 / drawingPane.getScaleX());
//            circlePoint.setScaleY(1.0 / drawingPane.getScaleY());

//            drawingPane.scaleXProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleX(1.0 / newValue.doubleValue()));
//            drawingPane.scaleYProperty().addListener((observable, oldValue, newValue) -> circlePoint.setScaleY(1.0 / newValue.doubleValue()));


            getChildren().add(Math.max(0, getChildren().size() - 1), circlePoint);
            circlePoint.toFront();
        }
    }

    public final CirclePoint getPoint(Point point) {
        return circlePointHashMap.get(point);
    }


    public final CirclePoint getPose(Pose pose) {
        return circlePoseHashMap.get(pose);
    }

    public final void removePoses(Pose... poses) {
        for (Pose pose : poses) {
            getChildren().remove(circlePoseHashMap.remove(pose));
        }
    }

    public final void removePoints(Point... points) {
        for (Point point : points) {
            getChildren().remove(circlePointHashMap.remove(point));
        }
    }

    public final void removePoints(List<? extends Point> removed) {
        removePoints(removed.toArray(new Point[removed.size()]));
    }

    public final void addPoints(List<? extends Point> addedSubList) {
        addPoints(addedSubList.toArray(new Point[addedSubList.size()]));
    }

    public final TitledPane getTitlePane() {
        return titlePane;
    }

    public final boolean isHasPointSelected() {
        return hasPointSelected.get();
    }

    public final void setHasPointSelected(boolean hasPointSelected) {
        this.hasPointSelected.set(hasPointSelected);
    }

    public final SimpleBooleanProperty hasPointSelectedProperty() {
        return hasPointSelected;
    }
}
