package org.curvedrawer.misc;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.curvedrawer.util.PathGroup;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

public class CirclePoint extends Circle {
    private final PathGroup pathGroup;

    public CirclePoint(Point point, PathGroup pathGroup) {
        super(5, Color.BLUE);
        this.pathGroup = pathGroup;

        centerXProperty().bind(point.xProperty());
        centerYProperty().bind(point.yProperty());

        setOnMouseDragged(mouseEvent -> {

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                point.setX(mouseEvent.getX());
                point.setY(mouseEvent.getY());
                pathGroup.setHasPointSelected(true);
            }
        });

        hoverProperty().addListener((observable, oldValue, newValue) -> selected(newValue));
    }

    public CirclePoint(double centerX, double centerY, PathGroup pathGroup) {
        super(centerX, centerY, 2, Color.RED);
        this.pathGroup = pathGroup;
    }

    public CirclePoint(Pose pose, PathGroup pathGroup) {
        this(pose.getX(), pose.getY(), pathGroup);
    }

    public void selected(boolean isSelected) {
        if (isSelected) {
            setScaleX(getScaleX() * 2);
            setScaleY(getScaleY() * 2);
            pathGroup.setHasPointSelected(true);

        } else {
            setScaleX(getScaleX() / 2);
            setScaleY(getScaleY() / 2);

            pathGroup.setHasPointSelected(false);
        }
    }
}
