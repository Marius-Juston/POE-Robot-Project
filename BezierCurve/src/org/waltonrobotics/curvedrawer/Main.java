package org.waltonrobotics.curvedrawer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.waltonrobotics.curvedrawer.util.Config.Resources;

public class Main extends Application {

  private TabPane tabPane;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    tabPane = FXMLLoader.load(getClass().getResource(
        Resources.MENU));

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

    primaryStage.setOnCloseRequest(event -> NetworkTable.shutdown());

    addTab(createCurveTab("Bezier Test"));
  }

  private void addTab(Tab tab) {
    tabPane.getTabs().add(tab);
  }

  private Tab createCurveTab(String name) throws IOException {

    Parent tabContentController = FXMLLoader.load(getClass().getResource(Resources.TAB));

    Tab tab = new Tab(name);
    tab.setContent(tabContentController);

    return tab;
  }
}
