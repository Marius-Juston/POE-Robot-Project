package org.waltonrobotics.beziercurve.controller;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.converter.NumberStringConverter;
import org.waltonrobotics.beziercurve.Helper;
import org.waltonrobotics.beziercurve.curve.BezierCurve;
import org.waltonrobotics.beziercurve.curve.Curve;
import org.waltonrobotics.beziercurve.point.ControlPoint;
import org.waltonrobotics.beziercurve.point.Point;
import org.waltonrobotics.beziercurve.point.WayPoint;

public class BezierTabContentController implements Initializable {

  public static final HashMap<Point, BezierTabContentController> points = new HashMap<>();
  private static final String NETWORK_TABLE = "SmartDashboard";
  public final ContextMenu POINT_CONTEXT_MENU = Helper.createTemplateContextMenu();
  public final ContextMenu TABLE_CONTEXT_MENU = Helper.createTemplateContextMenu();
  private final int teamNumber = 2974;
  private final Alert warningAlert = new Alert(AlertType.WARNING);
  private final Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
  public SplitPane splitPane;
  public TextField smartDashboardKeyField;
  public Button sendButton;
  public TextField ipAddressTextField;
  private String[] ipAddresses;
  @FXML
  private Pane anchorPane;
  @FXML
  private TableView<Point> pointTable;
  @FXML
  private TableColumn<Point, String> pointNameColumn;
  @FXML
  private TableColumn<Point, Number> pointCenterXColumn;
  @FXML
  private TableColumn<Point, Number> pointCenterYColumn;
  private List<Curve> curves;
  private NetworkTable networkTable;
  private MenuItem removePoint = new MenuItem("Remove Point");

  private String[] setIpAddresses(String... extraIpAddresses) {
    String[] ipAddresses = new String[5 + extraIpAddresses.length];
    ipAddresses[0] = "10." + teamNumber / 100 + "." + teamNumber % 100 + ".2";
    ipAddresses[1] = "172.22.11.2";
    ipAddresses[2] = "roboRIO-" + teamNumber + "-FRC.local";
    ipAddresses[3] = "roboRIO-" + teamNumber + "-FRC.lan";
    ipAddresses[4] = "roboRIO-" + teamNumber + "-FRC.frc-field.local";

    System.arraycopy(extraIpAddresses, 0, ipAddresses,
        ipAddresses.length - extraIpAddresses.length, extraIpAddresses.length);

    NetworkTable.setIPAddress(ipAddresses);
    return ipAddresses;
  }

  private NetworkTable getNetworkTable(String networkTableName) {
    NetworkTable networkTable = NetworkTable.getTable(networkTableName);
    networkTable.addConnectionListener(new IRemoteConnectionListener() {
      @Override
      public void connected(IRemote iRemote) {
        ipAddressTextField.setDisable(true);
        confirmationAlert.setContentText("Managed to connect to " + NETWORK_TABLE);
        confirmationAlert.showAndWait();
      }

      @Override
      public void disconnected(IRemote iRemote) {
        ipAddressTextField.setDisable(false);

        warningAlert.setContentText("Disconnected from " + NETWORK_TABLE);
        warningAlert.showAndWait();

      }
    }, true);

    return networkTable;
  }

  public List<Curve> getCurves() {
    return curves;
  }

  public void setRemovePointOnAction(EventHandler<ActionEvent> event) {
    removePoint.setOnAction(event);
  }

  private void initContextMenus() {
    POINT_CONTEXT_MENU.getItems().add(removePoint);

    MenuItem addPoint = new MenuItem("Add point");
    addPoint.setOnAction(event -> {
      addPointIfDoesNotExist(
          createControlPoint(anchorPane.getWidth() / 2, anchorPane.getHeight() / 2), curves.get(0));
      drawCurves();
    });

    MenuItem removePoint = new MenuItem("Remove Points");
    removePoint.setOnAction(event -> {
      List<Point> points = new ArrayList<>();

      List<Integer> integerList = new ArrayList<>(
          pointTable.getSelectionModel().getSelectedIndices());

      List<Point> items = pointTable.getItems();

      for (int i = integerList.size() - 1; i >= 0; i--) {
        int index = integerList.get(i);

        points.add(items.get(index));
        items.remove(items.get(index));
      }

      getCurves().forEach(curve -> curve.getPoints().removeAll(points));

      anchorPane.getChildren().removeAll(points);

      drawCurves();
    });

    MenuItem moveUp = new MenuItem("Move Up");

    MenuItem moveDown = new MenuItem("Move Down");

    TABLE_CONTEXT_MENU.getItems().addAll(addPoint, removePoint, moveUp, moveDown);

    pointTable.setContextMenu(TABLE_CONTEXT_MENU);
  }

  private void initSmartDashboard() {
    NetworkTable.setTeam(teamNumber);
    NetworkTable.setClientMode();
    ipAddresses = setIpAddresses("10.12.34.19");

    networkTable = getNetworkTable(NETWORK_TABLE);

    if (!ipAddresses[ipAddresses.length - 1].isEmpty()) {
      ipAddressTextField.setText(ipAddresses[ipAddresses.length - 1]);
    }

    ipAddressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        ipAddresses = setIpAddresses(newValue);
      }
    });

    smartDashboardKeyField.textProperty()
        .addListener((observable, oldValue, newValue) -> sendButton.setDisable(newValue.isEmpty()));

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    curves = new ArrayList<>();
    curves.add(new BezierCurve());
    curves.add(new BezierCurve());

    initSmartDashboard();
    initTable();
    initContextMenus();
  }

  private void initTable() {
    pointTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    initializeStringColumn(pointNameColumn, "name",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow()).setName(cellEditEvent.getNewValue()));

    initializeNumberColumn(pointCenterXColumn, "centerX",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setCenterX(cellEditEvent.getNewValue().doubleValue()));

    initializeNumberColumn(pointCenterYColumn, "centerY",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setCenterY(cellEditEvent.getNewValue().doubleValue()));
  }

  private void initializeNumberColumn(TableColumn<Point, Number> column, String property,
      EventHandler<CellEditEvent<Point, Number>> eventHandler) {
    column.setCellValueFactory(new PropertyValueFactory<>(property));
    column
        .setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    column.setOnEditCommit(eventHandler);
  }

  private void initializeStringColumn(TableColumn<Point, String> column, String property,
      EventHandler<CellEditEvent<Point, String>> eventHandler) {
    column.setCellValueFactory(new PropertyValueFactory<>(property));
    column.setCellFactory(TextFieldTableCell.forTableColumn());
    column.setOnEditCommit(eventHandler);
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
      addPointIfDoesNotExist(controlPoint, curves.get(0));

      drawCurves();

    } else if (mouseButton == MouseButton.MIDDLE) {
      WayPoint wayPoint = createWayPoint(mouseEvent.getX(), mouseEvent.getY());
      addPointIfDoesNotExist(wayPoint, curves.get(1));
    }
  }

  private void addPointIfDoesNotExist(Point point, Curve bezierCurve) {
    ObservableList<Node> children = anchorPane.getChildren();

    if (!children.contains(point)) {
      children.add(point);

      pointTable.getItems().add(point);
      bezierCurve.addPoint(point);
      Helper.initPoint(point, this);
    }
  }

  public List<Point> getPoints() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof Point)
        .map(Point.class::cast).collect(Collectors.toList());
  }

  public void drawCurves() {
    clearLines();

    for (Curve bezierCurve : curves) {
      if (bezierCurve.getPoints().size() > 1) {
        List<Point> point2DS = bezierCurve.getCurvePoints(50);

        if (point2DS.size() > 1) {
          for (int i = 0; i < point2DS.size() - 1; i++) {
            Line line = Helper.createLine();
            Helper.setLineLocation(line, point2DS.get(i), point2DS.get(i + 1));

            anchorPane.getChildren().add(0, line);
          }
        }
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
    return new ControlPoint(x, y);
  }

  private List<Point2D> getControlCenters() {
    return getControlPoints().stream().map(Point::getCenter).collect(Collectors.toList());
  }

  private WayPoint createWayPoint(double x, double y) {
    return new WayPoint(x, y);
  }

  private List<Line> getLines() {
    return anchorPane.getChildren().stream().filter(node -> node instanceof Line)
        .map(Line.class::cast).collect(Collectors.toList());
  }

  public void sendBezierCurveToSmartDashboard(ActionEvent actionEvent) {
    if (!smartDashboardKeyField.getText().isEmpty()) {
      if (networkTable.isConnected()) {
        String pointsString = Helper.toString(curves.get(0), true);

        networkTable.putString(smartDashboardKeyField.getText(), pointsString);

        confirmationAlert.setContentText(
            "Sent points to SmartDashboard retrive them using the " + smartDashboardKeyField
                .getText());
        confirmationAlert.showAndWait();
      } else {

        warningAlert.setContentText(
            "Unable to connect to SmartDashboard using these Ip Addresses:\n" + Arrays
                .toString(ipAddresses));
        warningAlert.showAndWait();
      }
    }
  }
}
