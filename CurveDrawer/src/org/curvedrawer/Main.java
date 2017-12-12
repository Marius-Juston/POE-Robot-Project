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

public class Main extends Application {
    private TabPane tabPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        tabPane = new TabPane();
        tabPane.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setPrefSize(640.0, 480.0);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.setOnKeyPressed(event ->
        {
            String character = event.getText();

            if (event.isControlDown() && character.equals("s")) {
                System.out.println("SAVED"); //TODO make it save the current progress
            }
        });

        addTab(createDrawingTab("Curve Drawer"));

        primaryStage.setTitle("Bezier Curve Creator");
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> NetworkTable.shutdown());
    }

    private void addTab(Tab tab) {
        tabPane.getTabs().add(tab);

        if (tabPane.getTabs().size() > 1) {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        }
    }

    private Tab createDrawingTab(String name) throws IOException {
        Parent tabContentController = FXMLLoader.load(getClass().getResource("/assets/fxml/curve_drawing_tab.fxml"));

        Tab tab = new Tab(name);
        tab.setContent(tabContentController);
        tab.setOnClosed(event -> {
            if (tabPane.getTabs().size() <= 1) {
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            }
        });


        return tab;
    }

}
