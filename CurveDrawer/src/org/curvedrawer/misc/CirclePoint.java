package org.curvedrawer.misc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.curvedrawer.util.Point;

public class CirclePoint extends Circle {
    public CirclePoint(Point point) {
        super(5, Color.BLUE);

        centerXProperty().bind(point.xProperty());
        centerYProperty().bind(point.yProperty());
    }
}
