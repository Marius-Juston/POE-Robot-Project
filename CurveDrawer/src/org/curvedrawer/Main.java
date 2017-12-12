package org.curvedrawer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) throws IOException {
        tabPane = FXMLLoader.load(getClass().getResource(
                "./src/fxml/menu.fxml"));

        primaryStage.setTitle("Bezier Curve Creator");
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();

        tabPane.setOnKeyPressed(event ->
        {
            String character = event.getText();

            if (event.isControlDown() && character.equals("s")) {
                System.out.println("SAVED"); //TODO make it save the current progress
            }
        });

        primaryStage.setOnCloseRequest(event -> NetworkTable.shutdown());

        addTab(createDrawingTab("Bezier Test"));
    }

    private void addTab(Tab tab) {
        tabPane.getTabs().add(tab);
        tabPane.
    }

    private Tab createDrawingTab(String name) throws IOException {
        Parent tabContentController = FXMLLoader.load(getClass().getResource("./src/fxml/drawing_tab.fxml"));

        Tab tab = new Tab(name);
        tab.setContent(tabContentController);

        return tab;
    }

}
