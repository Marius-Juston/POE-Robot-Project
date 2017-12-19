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
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.*;
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
    private final Map<Path, PathGroup> pathGroupHashMap = new HashMap<>();
    private final SimpleDoubleProperty pressedX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty pressedY = new SimpleDoubleProperty();
    @FXML
    private Pane pane;
    @FXML
    private Pane drawingPane;
    @FXML
    private Accordion pathsViewer;
    @FXML
    private Button sendButton;
    private SimpleIntegerProperty selectedPath;
    private SimpleBooleanProperty isDragging = new SimpleBooleanProperty(false);

    @FXML
    private void sendCurveToSmartDashboard() {
        Path path = getSelectedPaths();

        Pose[] poses = pathPoints.get(path);

        String converted = Converter.posesToString(poses);

        System.out.println(converted);

        String nameOfPath = pathsViewer.getPanes().get(selectedPath.get()).getText();

        System.out.println('\'' + nameOfPath + '\'');

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
        if (mouseEvent.getButton() == MouseButton.PRIMARY && !isDragging.get()) {

            double x = ((mouseEvent.getX() / drawingPane.getScaleX()) - drawingPane.getTranslateX()); //FIXME make it so that the x scale does not affect the x coordinate (position the x where it should be on the screen)
            double y = ((mouseEvent.getY() / drawingPane.getScaleY()) - drawingPane.getTranslateY()); //FIXME make it so that y scale does not affect the y coordinate (position the y where it should be on the screen)

            Point point = new Point(x, y);

            if (selectedPath.get() == -1 || pathsViewer.getPanes().isEmpty()) {

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
                }
            });
        }
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        selectedPath = new SimpleIntegerProperty(-1);

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

        drawingPane.setStyle("-fx-border-color: black");

    }

    private void handleScroll(ScrollEvent scrollEvent) {
        double direction = Math.signum(scrollEvent.getDeltaY());
        direction *= Main.ZOOM_FACTOR.get();


        drawingPane.setScaleX(Math.max(Main.ZOOM_FACTOR.get(), drawingPane.getScaleX() + direction));
        drawingPane.setScaleY(Math.max(Main.ZOOM_FACTOR.get(), drawingPane.getScaleY() + direction));

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
        System.out.println(event.getCode());

        if (event.isControlDown()) {
            if ((event.getCode() == KeyCode.N)) {
                createPath();
            } else if (event.getCode() == KeyCode.UP) {
                selectedPath.set(Math.max(selectedPath.getValue() - 1, 0));
            } else if (event.getCode() == KeyCode.DOWN) {
                selectedPath.set(Math.min(selectedPath.getValue() + 1, pathsViewer.getPanes().size() - 1));
            } else if (event.getCode() == KeyCode.DIGIT0) {
                drawingPane.setScaleX(1);
                drawingPane.setScaleY(1);
            }
        }
    }

    private Path getSelectedPaths() //TODO make it so that you can select many paths
    {
        return pathHashMap.get(selectedPath.get());
    }

    @FXML
    private void pan(MouseEvent event) {
        if (!anyPointIsSelected()) {
            drawingPane.setTranslateX(drawingPane.getTranslateX() + event.getX() - pressedX.get());
            drawingPane.setTranslateY(drawingPane.getTranslateY() + event.getY() - pressedY.get());

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

}
