package org.curvedrawer.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.curvedrawer.Main;
import org.curvedrawer.path.Path;
import org.curvedrawer.path.PathType;
import org.curvedrawer.util.Point;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PathSelectorController implements Initializable { //TODO make this better and more efficient

    private static String[] existingPathNames;
    private static Path selectedPath;
    public TextField pathName;
    public ChoiceBox<PathType> pathTypeSelection;
    public Button okButton;
    public Button cancelButton;

    public static AbstractMap.SimpleEntry<String, Path> getPathChoice(String[] existingPathNames) {
        PathSelectorController.existingPathNames = existingPathNames;

        try {
            Parent parent = FXMLLoader
                    .load(PathSelectorController.class.getResource("/assets/fxml/path_selector.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            String text = ((TextField) ((HBox) parent.getChildrenUnmodifiable().get(0)).getChildren().get(0)).getText();

            return new AbstractMap.SimpleEntry<>(text, selectedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PathType.values().length > 0) {

            pathTypeSelection.getItems().addAll(PathType.values());
            pathTypeSelection.valueProperty().setValue(PathType.values()[0]);
            pathTypeSelection.valueProperty().addListener(
                    (observable, oldValue, newValue) -> allowOkButtonToChangeState(pathName.getText(),
                            newValue));
        }

        pathName.setOnKeyTyped(event ->
        {
            String text =
                    pathName.getText() + (Character.isAlphabetic(event.getCharacter().charAt(0)) ? event
                            .getCharacter() : event.getText());

            String finalText = text;
            if (Arrays.stream(existingPathNames).anyMatch(s -> s.equals(finalText))) {
                pathName.setStyle("-fx-text-fill: red;");
                text = "";
            } else {
                pathName.setStyle("-fx-text-fill: black;");
            }

            allowOkButtonToChangeState(text, pathTypeSelection.getValue());
        });

        cancelButton.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
        okButton.setOnAction(event -> {
            try {
                PathSelectorController.selectedPath = (Path) pathTypeSelection.getValue()
                        .getAssociatedClass().getDeclaredConstructors()[0].newInstance(Main.NUMBER_OF_STEPS, new Point[0]);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
    }


    private void allowOkButtonToChangeState(String fieldText, PathType pathType) {
        if (fieldText.isEmpty() || pathType == null) {
            okButton.setDisable(true);
        } else {
            okButton.setDisable(false);
        }

    }
}
