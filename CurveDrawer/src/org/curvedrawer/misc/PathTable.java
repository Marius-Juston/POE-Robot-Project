package org.curvedrawer.misc;

import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Point;

public class PathTable extends TableView<Point> {
    public PathTable(Path path) {
        setEditable(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initializeNumberColumn("X", "x",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setX(cellEditEvent.getNewValue().doubleValue()));

        initializeNumberColumn("Y", "y",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setY(cellEditEvent.getNewValue().doubleValue()));

        initializeStringColumn("Name", "name", cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                cellEditEvent.getTablePosition().getRow())
                .setName(cellEditEvent.getNewValue()));

        setItems(path.getPoints());
    }

    private void initializeNumberColumn(String columnName, String property,
                                        EventHandler<TableColumn.CellEditEvent<Point, Number>> eventHandler) {

        TableColumn<Point, Number> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }

    private void initializeStringColumn(String columnName, String property, EventHandler<TableColumn.CellEditEvent<Point, String>> eventHandler) {
        TableColumn<Point, String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }
}
