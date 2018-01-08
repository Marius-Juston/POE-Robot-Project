package org.curvedrawer.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.curvedrawer.Main;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Converter;
import org.curvedrawer.util.PathGroup;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

public class CurveDrawerTabController implements Initializable {
    private final ObservableMap<Path, Pose[]> pathPoints = FXCollections.observableHashMap();
    private final Map<Integer, Path> pathHashMap = new HashMap<>(10);
    private final Map<Path, PathGroup> pathGroupHashMap = new HashMap<>(1);
    private final SimpleDoubleProperty pressedX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty pressedY = new SimpleDoubleProperty();
    private final SimpleIntegerProperty selectedPath = new SimpleIntegerProperty(-1);
    private final SimpleBooleanProperty isDragging = new SimpleBooleanProperty(false);
    @FXML
    private Pane pane;
    @FXML
    private Pane drawingPane;
    @FXML
    private Accordion pathsViewer;
    @FXML
    private Button sendButton;

    @FXML
    private void sendCurveToSmartDashboard() {
        Path path = getSelectedPaths();
        Pose[] poses = pathPoints.get(path);
        String converted = Converter.posesToString(poses);
        String nameOfPath = pathsViewer.getPanes().get(selectedPath.get()).getText();
        Main.networkTable.putString(nameOfPath, converted);
    }

    private Path createPath() {
        Entry<String, Path> path = askForPath();

        if (path != null) {
            addPath(path.getKey(), path.getValue());

            return path.getValue();
        }

        return null;
    }

    @FXML
    private void createPoint(MouseEvent mouseEvent) {
        if ((mouseEvent.getButton() == MouseButton.PRIMARY) && !isDragging.get()) {

            Point point = new Point(drawingPane.parentToLocal(mouseEvent.getX(), mouseEvent.getY()));

            if ((selectedPath.get() == -1) || pathsViewer.getPanes().isEmpty()) {

                Path path = createPath();

                if (path != null) {
                    path.addPoints(point);
                }
            } else {
                Path path = pathHashMap.get(selectedPath.get());
                path.addPoints(point);
            }
        }
    }

    private Entry<String, Path> askForPath() {
        return PathSelectorController.getPathChoice(getUsedPathNames());
    }

    private String[] getUsedPathNames() {
        return pathsViewer.getPanes().stream().map(TitledPane::getText).toArray(String[]::new);
    }

    private void addPath(String pathName, Path path) {
        if ((path != null) || (pathName != null)) {
            assert path != null;
            PathGroup pathGroup = new PathGroup(pathName, path, drawingPane);

            pathsViewer.getPanes().add(pathGroup.getTitlePane());
            pathsViewer.setExpandedPane(pathGroup.getTitlePane());

            int selection = pathsViewer.getPanes().size() - 1;

            pathHashMap.put(selection, path);
            selectedPath.set(selection);

            sendButton.setDisable(false);

            pathPoints.put(path, path.createPathPoses());
            System.out.println(pathPoints.get(path).length + "\t" + path.getNumberOfSteps());

            pathGroupHashMap.put(path, pathGroup);
            drawingPane.getChildren().add(pathGroup);


            path.getPoints().addListener((ListChangeListener<Point>) c -> {
                if (c.next()) {
                    if (c.wasRemoved()) {
                        pathGroup.removePoints(c.getRemoved());
                    } else if (c.wasAdded()) {
                        pathGroup.addPoints(c.getAddedSubList());
                    }

                    pathPoints.put(path, path.createPathPoses());
                    System.out.println(pathPoints.get(path).length + "\t" + path.getNumberOfSteps());
                }
            });
        }
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        pathPoints.addListener((MapChangeListener<Path, Pose[]>) c -> {
            if (c.wasAdded()) {
                if (pathGroupHashMap.containsKey(c.getKey())) {
                    PathGroup pathGroup = pathGroupHashMap.get(c.getKey());
                    Pose[] poses = c.getValueAdded();
                    pathGroup.addPoses(poses);
                }
            }
            if (c.wasRemoved()) {
                if (pathGroupHashMap.containsKey(c.getKey())) {
                    PathGroup pathGroup = pathGroupHashMap.get(c.getKey());

                    Pose[] poses = c.getValueRemoved();

                    pathGroup.removePoses(poses);
                }
            }

        });

        ContextMenu pathViewerContextMenu = new ContextMenu();

        MenuItem removePath = new MenuItem("Remove Path");
        removePath.setOnAction(event -> removePaths(getSelectedPaths()));

        pathViewerContextMenu.getItems().add(removePath);

        pathsViewer.setContextMenu(pathViewerContextMenu);

        pane.addEventFilter(MouseEvent.DRAG_DETECTED, e -> isDragging.set(true));
        pane.addEventFilter(ScrollEvent.SCROLL, this::handleScroll);
    }

    private void handleScroll(ScrollEvent scrollEvent) {
        zoom(scrollEvent.getDeltaY());
    }

    private void zoom(double direction) {
        zoom(direction, Main.ZOOM_FACTOR.get());
    }

    private void zoom(double direction, double amount) {
        double direction1 = Math.signum(direction) * amount;

        drawingPane.setScaleX(Math.max(amount, drawingPane.getScaleX() + direction1));
        drawingPane.setScaleY(Math.max(amount, drawingPane.getScaleY() + direction1));
    }

    private void removePath(Path path) {
        PathGroup pathGroup = pathGroupHashMap.get(path);
        pathsViewer.getPanes().remove(pathGroup.getTitlePane());
        drawingPane.getChildren().remove(pathGroup);
    }

    private void removePaths(Path... paths) {
        for (Path path : paths)
            removePath(path);
    }

    @FXML
    private void handleKeyPresses(KeyEvent event) { //TODO make it so that you do not need to manually focus the vBox or tab to be able to get input from user
        if (event.isControlDown()) {
            switch (event.getCode()) {
                case N:
                    createPath();
                    break;
                case UP:
                    selectedPath.set(Math.max(selectedPath.getValue() - 1, 0));
                    break;
                case DOWN:
                    selectedPath.set(Math.min(selectedPath.getValue() + 1, pathsViewer.getPanes().size() - 1));
                    break;
                case DIGIT0:
                    drawingPane.setScaleX(1.0);
                    drawingPane.setScaleY(1.0);
                    break;
            }
        }
    }

    private Path getSelectedPaths() //TODO make it so that you can select many paths
    {
        return pathHashMap.get(selectedPath.get());
    }

    @FXML
    private void pan(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && !anyPointIsSelected()) {
            drawingPane.setTranslateX((drawingPane.getTranslateX() + event.getX()) - pressedX.get());
            drawingPane.setTranslateY((drawingPane.getTranslateY() + event.getY()) - pressedY.get());

            pressedX.set(event.getX());
            pressedY.set(event.getY());

            event.consume();
        }
    }

    private boolean anyPointIsSelected() {
        return pathGroupHashMap.values().stream().anyMatch(PathGroup::isHasPointSelected);
    }

    @FXML
    private void getMouseLocation(MouseEvent event) {
        pressedX.set(event.getX());
        pressedY.set(event.getY());

        isDragging.set(false);
    }

    @Override
    public String toString() {
        return "CurveDrawerTabController{" +
                "pathPoints=" + pathPoints +
                ", pathHashMap=" + pathHashMap +
                ", pathGroupHashMap=" + pathGroupHashMap +
                ", pressedX=" + pressedX +
                ", pressedY=" + pressedY +
                ", selectedPath=" + selectedPath +
                ", isDragging=" + isDragging +
                ", pane=" + pane +
                ", drawingPane=" + drawingPane +
                ", pathsViewer=" + pathsViewer +
                ", sendButton=" + sendButton +
                '}';
    }
}
