package org.curvedrawer.misc;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import org.curvedrawer.Main;
import org.curvedrawer.util.Point;

public class PathTable extends TableView<Point> {

    public PathTable(ObservableList<Point> points) {
        ContextMenu pathTableContextMenu = new ContextMenu();
        MenuItem addPoint = new MenuItem("Add Point");
        addPoint.setOnAction(event -> points.add(new Point(0, 0)));

        MenuItem removePoint = new MenuItem("Remove Points");
        removePoint.setOnAction(event -> points.removeAll(getSelectionModel().getSelectedItems()));


        pathTableContextMenu.getItems().addAll(addPoint, removePoint);
        setContextMenu(pathTableContextMenu);

        setEditable(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initializeNumberColumn("X", "scaledX",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setX(cellEditEvent.getNewValue().doubleValue() * Main.SCALE_FACTOR.get()));

        initializeNumberColumn("Y", "scaledY",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setY(cellEditEvent.getNewValue().doubleValue() * Main.SCALE_FACTOR.get()));

        initializeStringColumn("Name", "name", cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                cellEditEvent.getTablePosition().getRow())
                .setName(cellEditEvent.getNewValue()));

        setItems(points);
    }

    private void initializeNumberColumn(String columnName, String property,
                                        EventHandler<CellEditEvent<Point, Number>> eventHandler) {

        TableColumn<Point, Number> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }

    private void initializeStringColumn(String columnName, String property, EventHandler<CellEditEvent<Point, String>> eventHandler) {
        TableColumn<Point, String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }
}
