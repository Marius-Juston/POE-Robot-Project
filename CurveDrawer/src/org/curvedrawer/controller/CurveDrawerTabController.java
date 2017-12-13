package org.curvedrawer.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.curvedrawer.Main;
import org.curvedrawer.misc.CirclePoint;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Converter;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CurveDrawerTabController implements Initializable {
    public SplitPane splitPane;
    public ScrollPane pathScrollPane;
    @FXML
    private Pane drawingPane;
    @FXML
    private Accordion pathsViewer;
    @FXML
    private Button sendButton;

    private HashMap<Integer, Path> pathHashMap = new HashMap<>();
    private SimpleIntegerProperty selectedPath;
    private Path selectedPaths;

    public void sendCurveToSmartDashboard(ActionEvent actionEvent) {
        Path path = getSelectedPaths();

        Pose[] poses = pathPoints.get(path);

        String converted = Converter.posesToString(poses);

        System.out.println(converted);

        String nameOfPath = pathsViewer.getPanes().get(selectedPath.get()).getText();

        Main.networkTable.putString(nameOfPath, converted);
    }

    public void createPath() {
        Map.Entry<String, Path> path = askForPath();

        if (path != null) {
            addPath(path.getKey(), path.getValue());
        }
    }

    public void createPoint(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (selectedPath.get() == -1) {
                createPath();
            } else {
                Path path = pathHashMap.get(selectedPath.get());

                Point point = new Point(mouseEvent.getX(), mouseEvent.getY());

                path.addPoints(point);
            }
        }
    }

    private Map.Entry<String, Path> askForPath() {
        return PathSelectorController.getPathChoice(getUsedPathNames());
    }

    private String[] getUsedPathNames() {
        return pathsViewer.getPanes().stream().map(TitledPane::getText).toArray(String[]::new);
    }

    public void addPath(String pathName, Path path) {
        if (path != null || pathName != null) {
            assert path != null;
            TitledPane titledPane = new TitledPane(pathName, new PathTable(path));

            pathsViewer.getPanes().add(titledPane);
            pathsViewer.setExpandedPane(titledPane);

            int selection = pathsViewer.getPanes().size() - 1;

            pathHashMap.put(selection, path);
            selectedPath.set(selection);

            sendButton.setDisable(false);

            pathPoints.put(path, path.createPathPoses());

            path.getPoints().addListener((ListChangeListener<Point>) c -> {
                if (c.next()) {
                    if (c.wasRemoved()) {
                        drawingPane.getChildren().removeAll(c.getRemoved().stream().map(CirclePoint::new).collect(Collectors.toList()));
                    } else if (c.wasAdded()) {
                        drawingPane.getChildren().addAll(c.getAddedSubList().stream().map(CirclePoint::new).collect(Collectors.toList()));
                    }
                    System.out.println(c.wasPermutated());
                    System.out.println(c.wasUpdated());
                }
            });
        }
    }

    private final ObservableMap<Path , Pose[]> pathPoints = FXCollections.observableHashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedPath = new SimpleIntegerProperty(-1);

        splitPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.N) {
                this.createPath();
            }
        });

        pathPoints.addListener((MapChangeListener<? super Path, ? super Pose[]>) change -> {

            System.out.println(pathPoints);
            System.out.println(change.getMap());
        });
    }

    private Path getSelectedPaths() //TODO make it so that you can select many paths
    {
        return pathHashMap.get(selectedPath.get());
    }
}
