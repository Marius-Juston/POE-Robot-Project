package org.waltonrobotics.curvedrawer.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.waltonrobotics.curvedrawer.curve.Path;
import org.waltonrobotics.curvedrawer.curve.PathType;
import org.waltonrobotics.curvedrawer.util.Config.Curve;
import org.waltonrobotics.curvedrawer.util.Config.ROBOT;
import org.waltonrobotics.curvedrawer.util.Config.Resources;
import org.waltonrobotics.curvedrawer.util.Point;

public class PathSelectorController implements Initializable {

  private static String[] existingPathNames;
  private static Path selectedPath;
  public TextField pathName;
  public ChoiceBox<PathType> pathTypeSelection;
  public Button okButton;
  public Button cancelButton;

  public static Path getPathChoice(String[] existingPathNames) {
    PathSelectorController.existingPathNames = existingPathNames;

    try {
      Parent parent = FXMLLoader
          .load(PathSelectorController.class.getResource(Resources.PATH_SELECTOR));

      Stage stage = new Stage();
      stage.setScene(new Scene(parent));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return selectedPath;
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
        pathName.setStyle("-fx-text-inner-color: red;");
        text = "";
      } else {
        pathName.setStyle("-fx-text-inner-color: black;");
      }

      allowOkButtonToChangeState(text, pathTypeSelection.getValue());
    });

    cancelButton.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    okButton.setOnAction(event -> {
      try {
        PathSelectorController.selectedPath = (Path) pathTypeSelection.getValue()
            .getAssociatedClass().getDeclaredConstructors()[0].newInstance(
            Curve.RESOLUTION, ROBOT.ROBOT_WIDTH, pathName.getText(), new Point[0]);

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
