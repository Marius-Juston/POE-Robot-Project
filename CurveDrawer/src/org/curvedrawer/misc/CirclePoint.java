package org.curvedrawer.misc;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.curvedrawer.util.PathGroup;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

public class CirclePoint extends Circle {
    private final PathGroup pathGroup; //path group this CirclePoint belongs to

    private final double RADIUS; //default radius of the CirclePoint

    /**
     * Constructor to use when making a CirclePoint from a Point
     *
     * @param point     point to use to locate the CirclePoint in the x,y coordinate plane
     * @param pathGroup pathGroup the CirclePoint belongs to
     */
    public CirclePoint(Point point, PathGroup pathGroup) {
        super(5.0, Color.BLUE);
        RADIUS = getRadius();

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

    /**
     * Constructor to use when creating a pose
     *
     * @param centerX   centerX coordinate
     * @param centerY   centerY coordinate
     * @param pathGroup pathGroup the CirclePoint belongs to
     */
    private CirclePoint(double centerX, double centerY, PathGroup pathGroup) {
        super(centerX, centerY, 2.0, Color.RED);
        RADIUS = getRadius();

        this.pathGroup = pathGroup;
    }

    /**
     * Constructor to use when creating a pose
     *
     * @param pose      pose to use when making the placing the CirclePoint
     * @param pathGroup pathGroup the CirclePoint belongs to
     */
    public CirclePoint(Pose pose, PathGroup pathGroup) {
        this(pose.getX(), pose.getY(), pathGroup);
    }

    /**
     * Selects the circle if it is true and doubles the radius all the while notifying the path group that the circle has been selected otherwise resets the radius to be its default size and notifies the path group that it is no longer selected
     *
     * @param isSelected true if selected false otherwise
     */
    public final void selected(boolean isSelected) {
        if (isSelected) {

            setRadius(RADIUS * 2.0);
            pathGroup.setHasPointSelected(true);

        } else {
            setRadius(RADIUS);

            pathGroup.setHasPointSelected(false);
        }
    }

    @Override
    public String toString() {
        return "CirclePoint{" +
                "pathGroup=" + pathGroup +
                ", RADIUS=" + RADIUS +
                "} " + super.toString();
    }
}
