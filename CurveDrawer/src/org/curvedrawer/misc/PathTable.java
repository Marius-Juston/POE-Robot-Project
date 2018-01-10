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

    /**
     * Initializes a TableView that observers the defining points of a path
     *
     * @param points points of the path to observe
     */
    public PathTable(ObservableList<Point> points) {
        /////////////////////// CONTEXT MENU INITIALIZATION    //////////////////////////
        ContextMenu pathTableContextMenu = new ContextMenu();
        MenuItem addPoint = new MenuItem("Add Point");
        addPoint.setOnAction(event -> points.add(new Point(0, 0)));

        MenuItem removePoint = new MenuItem("Remove Points");
        removePoint.setOnAction(event -> points.removeAll(getSelectionModel().getSelectedItems()));


        pathTableContextMenu.getItems().addAll(addPoint, removePoint);
        setContextMenu(pathTableContextMenu);
        ///////////////////////////////////////////////////////////////////////////////////

        setEditable(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Adds column to observe the scaled X property of points
        initializeNumberColumn("X", "scaledX",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setX(cellEditEvent.getNewValue().doubleValue() * Main.SCALE_FACTOR.get()));

        //Adds column to observe the scaled Y property of points
        initializeNumberColumn("Y", "scaledY",
                cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow())
                        .setY(cellEditEvent.getNewValue().doubleValue() * Main.SCALE_FACTOR.get()));

        //Adds column to observe the name property of points
        initializeStringColumn("Name", "name", cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                cellEditEvent.getTablePosition().getRow())
                .setName(cellEditEvent.getNewValue()));

        setItems(points);
    }

    /**
     * Initializes columns that hold in number
     *
     * @param columnName   name of the column
     * @param property     name of the property the column should be observing
     * @param eventHandler event handler that handles when a value from the column is changed
     */
    private void initializeNumberColumn(String columnName, String property,
                                        EventHandler<CellEditEvent<Point, Number>> eventHandler) {

        TableColumn<Point, Number> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }

    /**
     * Initializes columns that hold in strings
     *
     * @param columnName   name of the column
     * @param property     name of the property the column should be observing
     * @param eventHandler event handler that handles when a value from the column is changed
     */
    private void initializeStringColumn(String columnName, String property, EventHandler<CellEditEvent<Point, String>> eventHandler) {
        TableColumn<Point, String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(eventHandler);

        column.setText(columnName);

        getColumns().add(column);
    }
}
