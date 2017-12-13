package org.curvedrawer.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Point;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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

    public void sendCurveToSmartDashboard(ActionEvent actionEvent) {
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
            TitledPane titledPane = new TitledPane(pathName, new PathTable(path));

            pathsViewer.getPanes().add(titledPane);
            pathsViewer.setExpandedPane(titledPane);

            int selection = pathsViewer.getPanes().size() - 1;

            pathHashMap.put(selection, path);
            selectedPath.set(selection);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedPath = new SimpleIntegerProperty(-1);
        splitPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.N) {
                this.createPath();
            }
        });
    }
}
