package org.curvedrawer.util;

import javafx.scene.Group;
import org.curvedrawer.misc.CirclePoint;

import java.util.HashMap;
import java.util.List;

public class PathGroup extends Group {
    private HashMap<Pose, CirclePoint> circlePoseHashMap;
    private HashMap<Point, CirclePoint> circlePointHashMap;

    public PathGroup() {
        this.circlePointHashMap = new HashMap<>();
        this.circlePoseHashMap = new HashMap<>();
    }

    public void addPoints(Point... points) {
        for (Point point : points) {
            CirclePoint circlePoint = new CirclePoint(point);
            circlePointHashMap.put(point, circlePoint);

            getChildren().add(0, circlePoint);
            circlePoint.toFront();
        }
    }

    public void addPoses(Pose... poses) {
        for (Pose pose : poses) {
            CirclePoint circlePoint = new CirclePoint(pose);
            circlePoseHashMap.put(pose, circlePoint);

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
}
