package org.waltonrobotics.curvedrawer.controller;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.converter.NumberStringConverter;
import org.waltonrobotics.curvedrawer.curve.Path;
import org.waltonrobotics.curvedrawer.objects.CirclePoint;
import org.waltonrobotics.curvedrawer.util.Helper;
import org.waltonrobotics.curvedrawer.util.Point;


public class CurveTabController implements Initializable {

  private final Alert warningAlert = new Alert(AlertType.WARNING);
  private final Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
  private final List<Shape> shapeHashSet = new ArrayList<>();
  public Button sendButton;
  public SplitPane splitPane;
  public TextField smartDashboardKeyField;
  public TextField ipAddressTextField;
  public Accordion pathPane;
  public HBox creationPane;
  @FXML
  private Pane drawingPane;
  private SimpleIntegerProperty selectedPath = new SimpleIntegerProperty(-1);

  private TableView<Point> createTable(Path path) {
    TableView<Point> pointTable = new TableView<>();

    pointTable.setEditable(true);
    pointTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    pointTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    TableColumn xColumn = initializeNumberColumn("x",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setX(cellEditEvent.getNewValue().doubleValue()));
    xColumn.setText("X");

    TableColumn yColumn = initializeNumberColumn("y",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setY(cellEditEvent.getNewValue().doubleValue()));
    yColumn.setText("Y");

    pointTable.getColumns().addAll(xColumn, yColumn);

    pointTable.getItems().addAll(path.getCreationPoints());

    return pointTable;
  }

  private TableColumn initializeNumberColumn(String property,
      EventHandler<CellEditEvent<Point, Number>> eventHandler) {

    TableColumn<Point, Number> column = new TableColumn<>();
    column.setCellValueFactory(new PropertyValueFactory<>(property));
    column
        .setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    column.setOnEditCommit(eventHandler);

    return column;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    selectedPath.addListener((observable, oldValue, newValue) -> {

      TitledPane pane = pathPane.getPanes().get((Integer) newValue);
      pane.setStyle(pane.getStyle().replace("normal", "bold"));
      pane.setStyle(pane.getStyle().replace("false", "true"));

      if (oldValue.intValue() != -1) {
        pane = pathPane.getPanes().get((Integer) oldValue);
        pane.setStyle(pane.getStyle().replace("bold", "normal"));
        pane.setStyle(pane.getStyle().replace("true", "false"));
      }
    });

    creationPane.addEventFilter(ScrollEvent.ANY, this::changePathSelection);
  }

  public void createPoint(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      Path path;

      Point point = new Point(mouseEvent.getX(), mouseEvent.getY());

      if (selectedPath.get() < 0) {
        path = createPath(point);

        if (path == null) {
          return;
        } else {
          addPathToTable(path);
          selectedPath.set(0);
        }
      } else {
        Path selectedPath = getCurrentSelectedPath();

        selectedPath.getCreationPoints().add(point);
      }
    }
  }

  public Color getNewColor() {
    //TODO make it smarter than this so then there is less change in getting a similar color
    return Color
        .rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
  }

  public void addPathToTable(Path path) {
    TableView tableView = createTable(path);
    TitledPane titledPane = new TitledPane();
    titledPane.setText(path.getClass().getSimpleName() + "- " + path.getName());

    titledPane.setContent(tableView);
    path.setColor(getNewColor());

    titledPane.setStyle(
        "-fx-text-fill:" + Helper.colorToString(path.getColor())
            + "; -fx-font-weight: normal;-fx-underline: true;");

    path.getCreationPoints().addListener((ListChangeListener<? super Point>) c -> {
      addPath(path);
    });

    addPath(path);

    pathPane.getPanes().add(titledPane);
  }

  private final HashMap<String, Path> pathHashMap = new HashMap<>();

  private void drawCreationPoints(Path path) {
  }

  private void addPath(Path path) {
   pathHashMap.put(path.getName(), path);

    drawingPane.getChildren().clear();

    for (Path p: pathHashMap.values())
    {
      drawingPane.getChildren().addAll(
          p.getCreationPoints().stream().map(point -> new CirclePoint(point, p)).collect(Collectors.toList())
      );
    }
  }

  public Path createPath(Point point) {
    Path path = createPath();

    if (path != null) {
      path.createPathPoints(point);
    }

    return path;
  }

  public Path createPath() {
    return PathSelectorController.getPathChoice(pathPane.getPanes().stream().map(
        titledPane -> titledPane.getText().substring(titledPane.getText().indexOf(" ") + 1))
        .toArray(String[]::new));
  }

  public void sendCurveToSmartDashboard(ActionEvent actionEvent) {

  }

  public void changePathSelection(ScrollEvent scrollEvent) {
    if (selectedPath.get() != -1) {
      int change = Math
          .min(Math.max(0, selectedPath.get() - ((int) Math.signum(scrollEvent.getDeltaY()))),
              pathPane.getPanes().size() - 1);
      selectedPath.set(change);
    }
  }

  public void viewCreatePath(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.MIDDLE) {
      Path path = createPath();

      if (path != null) {
        addPathToTable(path);
        selectedPath.set(selectedPath.getValue() + 1);
      }
    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      if (selectedPath.get() != -1) {
        pathPane.setExpandedPane(
            pathPane.getExpandedPane() == null ? pathPane.getPanes().get(selectedPath.get())
                : null);
      }
    }
  }

  public Path getCurrentSelectedPath() {
    String title = pathPane.getPanes().get(selectedPath.get()).getText();

    title = title.substring(title.indexOf(" ") + 1);

    return pathHashMap.get(title);
  }
}
