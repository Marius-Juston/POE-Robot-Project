package org.curvedrawer.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.curvedrawer.Main;
import org.curvedrawer.path.Path;
import org.curvedrawer.path.PathType;
import org.curvedrawer.util.Point;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.ResourceBundle;

public class PathSelectorController implements Initializable { //TODO make this better and more efficient

    private static String[] existingPathNames;
    private static Path selectedPath;
    @FXML
    private TextField pathName;
    @FXML
    private ChoiceBox<String> pathTypeSelection;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    /**
     * Creates a popup for selecting the path and the path name
     *
     * @param existingPathNames existing path name in the tabbed panes in the table view
     * @return a HashMap entry with as key the name of the path and as key the associated path
     */
    public static Entry<String, Path> getPathChoice(String[] existingPathNames) {
        PathSelectorController.existingPathNames = existingPathNames.clone();

        try {
            Parent parent = FXMLLoader
                    .load(PathSelectorController.class.getResource("/assets/fxml/path_selector.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            String text = ((TextInputControl) ((Pane) parent.getChildrenUnmodifiable().get(0)).getChildren().get(0)).getText();

            if (selectedPath == null)
                return null;

            return new SimpleEntry<>(text, selectedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Closes a window given an event
     *
     * @param event event source window to close
     */
    private static void closeWindow(Event event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }


    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        if (PathType.values().length > 0) {

            pathTypeSelection.getItems().addAll(Arrays.stream(PathType.values()).map(PathType::name).toArray(String[]::new));
            pathTypeSelection.valueProperty().setValue(PathType.values()[0].name());
            pathTypeSelection.valueProperty().addListener(
                    (observable, oldValue, newValue) -> allowOkButtonToChangeState(pathName.getText(),
                            newValue));
        }

        cancelButton.setOnAction(PathSelectorController::closeWindow);
    }

    /**
     * Converts the String selected path to a path type and creates an instance of the Path from that. closes the window after that
     *
     * @param event action event executed when pressing the ok button
     */
    @FXML
    private void getPathAndClose(ActionEvent event) {
        try {
            selectedPath = (Path) PathType.valueOf(pathTypeSelection.getValue())
                    .getAssociatedClass().getDeclaredConstructors()[0].newInstance(Main.NUMBER_OF_STEPS, new Point[0]);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        closeWindow(event);
    }

    /**
     * Method to turn the ok button to be disabled or enabled given the current text in the TextField and the path type selected.
     *
     * @param fieldText current text in displayed in the TextField
     * @param pathType  path type selected by the dropdown
     */
    private void allowOkButtonToChangeState(String fieldText, String pathType) {
        if (fieldText.isEmpty() || (pathType == null)) {
            okButton.setDisable(true);
        } else {
            okButton.setDisable(false);
        }

    }

    /**
     * Handles key event for the TextField. Checks if the new word has already been used already or not if it has already been used the TextField text will change to be red.
     *
     * @param event the textField key event
     */
    @FXML
    private void handleInput(KeyEvent event) {
        String text =
                pathName.getText().concat((Character.isAlphabetic(event.getCharacter().charAt(0)) ? event
                        .getCharacter() : event.getText()));

        String finalText = text;
        if (Arrays.stream(existingPathNames).anyMatch(s -> s.equals(finalText))) {
            pathName.setStyle("-fx-text-fill: red;");
            text = "";
        } else {
            pathName.setStyle("-fx-text-fill: black;");
        }

        allowOkButtonToChangeState(text, pathTypeSelection.getValue());

    }

    @Override
    public String toString() {
        return "PathSelectorController{" +
                "pathName=" + pathName +
                ", pathTypeSelection=" + pathTypeSelection +
                ", okButton=" + okButton +
                ", cancelButton=" + cancelButton +
                '}';
    }
}
