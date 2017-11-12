import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ControlPoint extends Circle {

  public ControlPoint(double centerX, double centerY, BezierTab bezierTab) {

    super(centerX, centerY, 3, Color.BLUE);

    setOnMouseDragged(event -> {
      setCenterX(event.getX());
      setCenterY(event.getY());

      bezierTab.drawBezienCurve(false);
    });

//    hoverProperty().add;
  }

  public ControlPoint(Point2D point2D, BezierTab bezierTab)
  {
   this(point2D.getX() , point2D.getY(), bezierTab);
  }

  public Point2D getCenter()
  {
    return new Point2D(getCenterX(), getCenterY());
  }
}
