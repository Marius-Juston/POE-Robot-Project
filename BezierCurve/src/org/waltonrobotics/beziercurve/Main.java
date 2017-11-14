package org.waltonrobotics.beziercurve;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

  private TabPane tabPane;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    tabPane = FXMLLoader.load(getClass().getResource(
        "menu.fxml"));

    primaryStage.setTitle("Bezier Curve Creator");
    primaryStage.setScene(new Scene(tabPane));
    primaryStage.show();

    tabPane.setOnKeyPressed(event ->
    {
      String character = event.getText();

      if (event.isControlDown() && character.equals("s")) {
        System.out.println("SAVED");
      }
    });

    addTab(createBezierTab("Bezier Test"));
  }

  private void addTab(Tab tab) {
    tabPane.getTabs().add(tab);
  }

  private Tab createBezierTab(String name) throws IOException {

    Parent tabContentController = FXMLLoader.load(getClass().getResource("tab.fxml"));

    Tab tab = new Tab(name);
    tab.setContent(tabContentController);

    return tab;
  }
}
