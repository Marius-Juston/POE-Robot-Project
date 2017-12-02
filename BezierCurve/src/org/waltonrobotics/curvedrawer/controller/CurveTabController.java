package org.waltonrobotics.curvedrawer.controller;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;
import org.waltonrobotics.curvedrawer.curve.Path;
import org.waltonrobotics.curvedrawer.util.Point;


public class CurveTabController implements Initializable {

  private final Alert warningAlert = new Alert(AlertType.WARNING);
  private final Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
  public Button sendButton;
  public SplitPane splitPane;
  public TextField smartDashboardKeyField;
  public TextField ipAddressTextField;
  public TreeTableView pathTable;

  @FXML
  private Pane pane;
  private int selectedPath = -1;

  @FXML

  private TableView<Path> createTable(Path path) {
    TableView<Path> pointTable = new TableView<>();
    pointTable.setEditable(true);
    pointTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    pointTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    TableColumn xColumn = initializeNumberColumn("x",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setX(cellEditEvent.getNewValue().doubleValue()));

    TableColumn yColumn = initializeNumberColumn("y",
        cellEditEvent -> cellEditEvent.getTableView().getItems().get(
            cellEditEvent.getTablePosition().getRow())
            .setY(cellEditEvent.getNewValue().doubleValue()));

    pointTable.getColumns().addAll(xColumn, yColumn);

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
  }

  public void createPoint(MouseEvent mouseEvent) {
    Path path;

    Point point = new Point(mouseEvent.getX(), mouseEvent.getY());

    if (selectedPath < 0) {
      path = createPath(point);

      if (path == null) {
        return;
      }
    }
  }

  public Path createPath(Point point) {
    return PathSelectorController.getPathChoice(new String[0]); //TODO
  }

  public void createPath() {

  }


  public void sendCurveToSmartDashboard(ActionEvent actionEvent) {

  }
}
