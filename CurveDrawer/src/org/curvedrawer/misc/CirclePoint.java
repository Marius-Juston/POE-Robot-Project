package org.curvedrawer.misc;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

public class CirclePoint extends Circle {
    public CirclePoint(Point point) {
        super(5, Color.BLUE);


        centerXProperty().bind(point.xProperty());
        centerYProperty().bind(point.yProperty());

        setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                point.setX(mouseEvent.getX());
                point.setY(mouseEvent.getY());
            }
        });

        hoverProperty().addListener((observable, oldValue, newValue) -> selected(newValue));
    }

    public CirclePoint(double centerX, double centerY) {
        super(centerX, centerY, 2, Color.RED);
    }

    public CirclePoint(Pose pose) {
        this(pose.getX(), pose.getY());
    }

    public void selected(boolean isSelected) {
        if (isSelected) {
            setId("circle-selected");
        } else {
            setId("circle-unselected");
        }
    }
}
