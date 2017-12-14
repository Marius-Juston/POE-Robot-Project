package org.curvedrawer.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.curvedrawer.Main;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Converter;
import org.curvedrawer.util.PathGroup;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

public class CurveDrawerTabController implements Initializable {
    private final ObservableMap<Path, Pose[]> pathPoints = FXCollections.observableHashMap();
    private final Map<Integer, Path> pathHashMap = new HashMap<>(10);
    @FXML
    private SplitPane splitPane;
    @FXML
    private Group drawingPane;
    @FXML
    private Accordion pathsViewer;
    @FXML
    private Button sendButton;
    private SimpleIntegerProperty selectedPath;

    private Map<Path, PathGroup> pathGroupHashMap = new HashMap<>();

    public final void sendCurveToSmartDashboard() {
        Path path = getSelectedPaths();

        Pose[] poses = pathPoints.get(path);

        String converted = Converter.posesToString(poses);

        System.out.println(converted);

        String nameOfPath = pathsViewer.getPanes().get(selectedPath.get()).getText();

        System.out.println('\'' + nameOfPath + '\'');

        Main.networkTable.putString(nameOfPath, converted);
    }

    private void createPath() {
        Entry<String, Path> path = askForPath();

        if (path != null) {
            addPath(path.getKey(), path.getValue());
        }
    }

    public final void createPoint(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (selectedPath.get() == -1) {
                createPath();
            } else {
                Path path = pathHashMap.get(selectedPath.get());

                Point point = new Point(mouseEvent.getX() / Main.SCALE_FACTOR, mouseEvent.getY() / Main.SCALE_FACTOR);

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
            TitledPane titledPane = new TitledPane(pathName, new PathTable(path));

            pathsViewer.getPanes().add(titledPane);
            pathsViewer.setExpandedPane(titledPane);

            int selection = pathsViewer.getPanes().size() - 1;

            pathHashMap.put(selection, path);
            selectedPath.set(selection);

            sendButton.setDisable(false);

            pathPoints.put(path, getScaledPathPoints(path));
            pathGroupHashMap.put(path, new PathGroup());
            drawingPane.getChildren().add(pathGroupHashMap.get(path));


            path.getPoints().addListener((ListChangeListener<Point>) c -> {
                if (c.next()) {
                    PathGroup pathGroup = pathGroupHashMap.get(path);


                    if (c.wasRemoved()) {
                        pathGroup.removePoints(c.getRemoved());
                    } else if (c.wasAdded()) {
                        pathGroup.addPoints(c.getAddedSubList());
                    }

                    pathPoints.put(path, getScaledPathPoints(path));
                }
            });
        }
    }

    public Pose[] getScaledPathPoints(Path path)
    {
        return Arrays.stream(path.createPathPoses()).map(pose -> pose.multiply(Main.SCALE_FACTOR)).toArray(Pose[]::new);
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        selectedPath = new SimpleIntegerProperty(-1);

        splitPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && (event.getCode() == KeyCode.N)) {
                createPath();
            }
        });

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
    }

    private Path getSelectedPaths() //TODO make it so that you can select many paths
    {
        return pathHashMap.get(selectedPath.get());
    }
}
