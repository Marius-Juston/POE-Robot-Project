import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class BezierTab extends Tab{

  private int points = 50; // the start count as a point

  private final AnchorPane anchorPane;

  public BezierTab(String text) {
    super(text);

    anchorPane = new AnchorPane();
    anchorPane.setMaxSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);
    anchorPane.setMinSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);
    anchorPane.setPrefSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);

    anchorPane.getChildren().addAll();
    setContent(anchorPane);

    anchorPane.setOnMouseClicked(event -> {
      ControlPoint circle = new ControlPoint(event.getX(), event.getY(), this);

      Optional<Node> touched = anchorPane.getChildren().stream()
          .filter(node -> node instanceof ControlPoint && node.intersects(circle.getLayoutBounds()))
          .findFirst();

      if (touched.isPresent()) {
        ControlPoint touchingPoint = (ControlPoint) touched.get();

      } else {
        anchorPane.getChildren().add(circle);

        drawBezienCurve(anchorPane.getChildren(), false);
      }
    });
  }

  private class Path{
    private final List<Point2D> point2DS;
    private final Point2D path;

    public Path(List<Point2D> point2DS, Point2D path) {
      this.point2DS = point2DS;
      this.path = path;
    }

    public List<Point2D> getPoint2DS() {
      return point2DS;
    }

    public Point2D getPath() {
      return path;
    }
  }

  public void drawBezienCurve(ObservableList<Node> drawingPlace, boolean drawInterpolationLines)
  {
    drawingPlace.removeAll(drawingPlace.filtered(node -> node instanceof Line));

    List<Point2D> path = new ArrayList<>();

    for (double i = 0 /*make this number = 1 if you want to exclude teh start point*/ ; i <= this.points; i++) {
      List<Point2D> point2Ds = drawingPlace.stream()
          .filter(node -> node instanceof ControlPoint).map(node -> ((ControlPoint) node).getCenter()).collect(
              Collectors.toList());

      List<List<Point2D>> points = new ArrayList<>();
      points.add(point2Ds);


      Path pathAtT = createBezienCurve(i/ this.points, points);
      path.add(pathAtT.getPath());

      if(drawInterpolationLines)
        for (List<Point2D> point2DList : points)
          drawLines(drawingPlace, point2DList, Color.YELLOW);
    }

    System.out.println(path.size());

    drawLines(drawingPlace, path, Color.RED);
  }

  public void drawBezienCurve(boolean drawInterpolationLines)
  {
    drawBezienCurve(anchorPane.getChildren(),drawInterpolationLines);
  }

  public Path createBezienCurve(double t,  List<List<Point2D>> points)
  {
    List<Point2D> lookThrought = points.get(points.size() - 1);

    while(lookThrought.size() >= 2)
    {
      List<Point2D> points1 = new ArrayList<>(lookThrought.size() - 1);
      points.add(points1);

      for (int i = 0; i < lookThrought.size() - 1; i++) {
        double x = (1 - t) * lookThrought.get(i).getX() + t * lookThrought.get(i + 1).getX();
        double y = (1 - t) * lookThrought.get(i).getY() + t * lookThrought.get(i + 1).getY();
        points1.add(new Point2D(x, y));
      }

      lookThrought = points1;
    }

    return new Path(lookThrought, points.get(points.size() - 1).get(points.get(points.size() - 1).size() - 1));
  }

  public void drawLines(ObservableList<Node> nodes, List<Point2D> points,
      Color color)
  {


    for (int i = 0; i < points.size() - 1; i++)
    {
      Line line = new Line();
      line.setStroke(color);
      line.setStrokeWidth(3);

      Point2D center = points.get(i);

      line.setStartX(center.getX());
      line.setStartY(center.getY());

      center = points.get(i + 1);

      line.setEndX(center.getX());
      line.setEndY(center.getY());

      nodes.add(line);
    }
  }
}
