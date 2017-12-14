package org.curvedrawer.misc;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.curvedrawer.Main;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

public class CirclePoint extends Circle {
    public CirclePoint(Point point) {
        super(5, Color.BLUE);

        centerXProperty().bind(point.xProperty().multiply(Main.SCALE_FACTOR));
        centerYProperty().bind(point.yProperty().multiply(Main.SCALE_FACTOR));

        setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                point.setX(mouseEvent.getX() / Main.SCALE_FACTOR);
                point.setY(mouseEvent.getY() / Main.SCALE_FACTOR);
            }
        });
    }

    public CirclePoint(double centerX, double centerY) {
        super(centerX, centerY, 2, Color.RED);
    }

    public CirclePoint(Pose pose) {
        this(pose.getX(), pose.getY());
    }
}
