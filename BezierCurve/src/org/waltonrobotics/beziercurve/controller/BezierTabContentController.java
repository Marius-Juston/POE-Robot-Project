package org.waltonrobotics.beziercurve.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.converter.NumberStringConverter;
import org.waltonrobotics.beziercurve.BezierCurve;
import org.waltonrobotics.beziercurve.Helper;
import org.waltonrobotics.beziercurve.point.ControlPoint;
import org.waltonrobotics.beziercurve.point.Point;
import org.waltonrobotics.beziercurve.point.WayPoint;

public class BezierTabContentController implements Initializable {

  public SplitPane splitPane;
  @FXML
  private AnchorPane anchorPane;
  @FXML
  private TableView<Point> pointTable;
  @FXML
  private TableColumn<Point, String> pointNameColumn;
  @FXML
  private TableColumn<Point, Number> pointCenterXColumn;
  @FXML
  private TableColumn<Point, Number> pointCenterYColumn;

  private BezierCurve bezierCurve;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    bezierCurve = new BezierCurve(50);

    pointNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    pointNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    pointNameColumn.setOnEditCommit(t -> t.getTableView().getItems().get(
        t.getTablePosition().getRow()).setName(t.getNewValue()));

    pointCenterXColumn.setCellValueFactory(new PropertyValueFactory<>("centerX"));
    pointCenterXColumn
        .setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    pointCenterXColumn.setOnEditCommit(t -> t.getTableView().getItems().get(
        t.getTablePosition().getRow()).setCenterX(t.getNewValue().doubleValue()));

    pointCenterYColumn.setCellValueFactory(new PropertyValueFactory<>("centerY"));
    pointCenterYColumn
        .setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    pointCenterYColumn.setOnEditCommit(t -> t.getTableView().getItems().get(
        t.getTablePosition().getRow()).setCenterY(t.getNewValue().doubleValue()));
  }

  private List<ControlPoint> getControlPoints() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof ControlPoint)
        .map(ControlPoint.class::cast).collect(Collectors.toList());
  }


  public List<ControlPoint> getWayPoints() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof WayPoint)
        .map(ControlPoint.class::cast).collect(Collectors.toList());
  }

  public void createPoint(MouseEvent mouseEvent) {
    MouseButton mouseButton = mouseEvent.getButton();

    if (mouseButton == MouseButton.PRIMARY) {
      ControlPoint controlPoint = createControlPoint(mouseEvent.getX(), mouseEvent.getY());

      addPointIfDoesNotExist(controlPoint);
    } else if (mouseButton == MouseButton.MIDDLE) {
      WayPoint wayPoint = createWayPoint(mouseEvent.getX(), mouseEvent.getY());
      addPointIfDoesNotExist(wayPoint);
    }

    drawBezierCurve();
  }

  private void addPointIfDoesNotExist(Point point) {
    ObservableList<Node> children = anchorPane.getChildren();

    if (!children.contains(point)) {
      children.add(point);

      pointTable.getItems().add(point);
    }
  }

  public List<Point> getPoints() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof Point)
        .map(Point.class::cast).collect(Collectors.toList());
  }

  public void drawBezierCurve() {
    clearLines();

    bezierCurve.setPoints(getControlCenters());
    List<Point2D> point2DS = bezierCurve.getCurvePoints();

    if (point2DS.size() > 1) {
      for (int i = 0; i < point2DS.size() - 1; i++) {
        Line line = Helper.createLine();
        Helper.setLineLocation(line, point2DS.get(i), point2DS.get(i + 1));

        anchorPane.getChildren().add(0, line);
      }
    }
  }

  private void clearLines() {
    removeLines(getLines());
  }

  private void removeLines(List<Line> lines) {
    anchorPane.getChildren().removeAll(lines);
  }

  private ControlPoint createControlPoint(double x, double y) {
    return new ControlPoint(x, y, this);
  }

  private List<Point2D> getControlCenters() {
    return getControlPoints().stream().map(Point::getCenter).collect(Collectors.toList());
  }

  private WayPoint createWayPoint(double x, double y) {
    return new WayPoint(x, y, this);
  }

  private List<Line> getLines() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof Line)
        .map(Line.class::cast).collect(Collectors.toList());
  }
}
